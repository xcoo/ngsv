import sys
import os.path
import pysam

from sam.data.sql import SQLDB
from sam.data.sam import Sam
from sam.data.chromosome import Chromosome
from sam.data.shortread import ShortRead
from sam.data.samhistogram import SamHistogram
from sam.data.histogrambin import HistogramBin

from config import *

def calc_histogram(samfile, chromosome, samId, binsize, samHistogramId, db):
    
    short_read_data = ShortRead(db)
    hist_bin_data = HistogramBin(db)

    maxRefEnd = short_read_data.max(samId, chromosome[0])
    if maxRefEnd is None:
        return

    start = 0
    nextcol = start
    bincount = 0
    binstart = 0
    binsum = 0
    binvalues = []

    for pileupcolumn in samfile.pileup(chromosome[1], start, maxRefEnd):
        # Skip columns outside desired range
        if pileupcolumn.pos < start or pileupcolumn.pos > maxRefEnd:
            continue

        # Fill in zero-coverage areas
        while nextcol < pileupcolumn.pos:
            bincount += 1
            nextcol += 1
            if bincount == binsize:
                hist_bin_data.append(samHistogramId, binsum, binstart, chromosome[0])
                binstart = nextcol
                binvalues.append(binsum)
                bincount = 0
                binsum = 0

        binsum += pileupcolumn.n
        bincount += 1
        nextcol += 1

        if bincount == binsize:
            hist_bin_data.append(samHistogramId, binsum, binstart, chromosome[0])
            binstart = pileupcolumn.pos
            binvalues.append(binsum)
            bincount = 0
            binsum = 0

    # Fill in zero-coverage area at the end of the region
    while nextcol <= maxRefEnd:
        nextcol += 1
        bincount += 1

        if bincount == binsize:
            hist_bin_data.append(samHistogramId, binsum, binstart, chromosome[0])
            binstart = nextcol
            binvalues.append(binsum)
            bincount = 0
            binsum = 0

    return binvalues

def calc_histogram_sum(samfile, chromosomes, samId, binSize, bins, db):
    short_read_data = ShortRead(db)
    sam_hist_data = SamHistogram(db)
    hist_bin_data = HistogramBin(db)

    sum10 = 0
    sum100 = 0
    sum1000 = 0
    sum10000 = 0
    sum100000 = 0

    sam_hist_data.append(samId, binSize*10)
    sam_hist_data.append(samId, binSize*100)
    sam_hist_data.append(samId, binSize*1000)
    sam_hist_data.append(samId, binSize*10000)
    sam_hist_data.append(samId, binSize*100000)

    for c in chromosomes:
        maxRefEnd = short_read_data.max(samId, c[0])
        if maxRefEnd is None:
            return

        if binSize*100 < maxRefEnd:
            sam_hist10     = sam_hist_data.get_by_samid_binSize(samId, binSize*10)
        if binSize*100 < maxRefEnd:
            sam_hist100    = sam_hist_data.get_by_samid_binSize(samId, binSize*100)
        if binSize*1000 < maxRefEnd:
            sam_hist1000   = sam_hist_data.get_by_samid_binSize(samId, binSize*1000)
        if binSize*10000 < maxRefEnd:
            sam_hist10000  = sam_hist_data.get_by_samid_binSize(samId, binSize*10000)
        if binSize*100000 < maxRefEnd:
            sam_hist100000 = sam_hist_data.get_by_samid_binSize(samId, binSize*100000)
    
        start = 0

        count = 0
        for b in bins[c[0]]:
            sum10 += b
            sum100 += b
            sum1000 += b
            sum10000 += b
            sum100000 += b
            if count != 0:
                if count % 10 == 0:
                    if binSize*10 < maxRefEnd:
                        hist_bin_data.append(sam_hist10['hist_id'], sum10, (count - 10) * binSize, c[0])
                        sum10 = 0
                if count % 100 == 0:
                    if binSize*100 < maxRefEnd:
                        hist_bin_data.append(sam_hist100['hist_id'], sum100,  (count - 100) * binSize, c[0])
                        sum100 = 0
                if count % 1000 == 0:
                    if binSize*1000 < maxRefEnd:
                        hist_bin_data.append(sam_hist1000['hist_id'], sum1000, (count - 1000) * binSize, c[0])
                        sum1000 = 0
                if count % 10000 == 0:
                    if binSize*10000 < maxRefEnd:
                        hist_bin_data.append(sam_hist10000['hist_id'], sum10000, (count - 10000) * binSize, c[0])
                        sum10000 = 0
                if count % 100000 == 0:
                    if binSize*100000 < maxRefEnd:
                        hist_bin_data.append(sam_hist100000['hist_id'], sum100000, (count - 100000) * binSize, c[0])
                        sum100000 = 0
            count += 1


def load(filepath, db):
    filename = os.path.basename(filepath)

    file_ext = filename.split('.')[-1]

    if file_ext != 'bam':
        print 'Error: not supported file format'
        return

    print "begin to load", filename

    # load sam
    samfile = pysam.Samfile(filepath)
    
    return samfile


def run(filepath, binSize, db):
    bins = {}

    samfile = load(filepath, db)

    sam_data = Sam(db)
    chromosome_data = Chromosome(db)
    sam_hist_data = SamHistogram(db)

    chromosomes = chromosome_data.get_all()

    filename = os.path.basename(filepath)
    sam = sam_data.get_by_filename(filename) 
    if sam is None:
        print "Error : please load \"%s\" first" % filename

    samId = sam['id']
    sam_hist_data.append(samId, binSize)
    sam_hist = sam_hist_data.get_by_samid(samId)

    for c in chromosomes:
        bins[c[0]] = calc_histogram(samfile, c, samId, binSize, sam_hist['hist_id'], db)

    if bins is not None:
        calc_histogram_sum(samfile, chromosomes, samId, binSize, bins, db)
        #print "calc ended %s" % c[1]

def main():
    if len(sys.argv) < 2:
        print("No input files")
        sys.exit()

    files = sys.argv[1:]

    db = SQLDB(SAM_DB_NAME, SQLDB_HOST, SQLDB_USER, SQLDB_PASSWD)

    for f in files:
        run(f, 1000, db)


if __name__ == '__main__':
    main()



