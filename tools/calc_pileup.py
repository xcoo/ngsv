# -*- coding: utf-8 -*-

#
#   ngsv
#   http://github.com/xcoo/ngsv
#   Copyright (C) 2012, Xcoo, Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

import sys
import os.path
import time
import pysam

from sam.data.sql import SQLDB
from sam.data.sam import Sam
from sam.data.chromosome import Chromosome
from sam.data.samhistogram import SamHistogram
from sam.data.histogrambin import HistogramBin

from config import *

def calc_histogram(samfile, chromosome, samId, binsize, samHistogramId, db):
    
    hist_bin_data = HistogramBin(db)

    binstart = 0
    binsum = 0
    binvalues = []
    column = 0

    print 'ChrID: %d, ChrName: %s' % (chromosome[0], chromosome[1])
    print '',
    
    for pileupcolumn in samfile.pileup(str(chromosome[1])):

        # Sum up counts within the region
        if pileupcolumn.pos >= binsize * (column + 1):
            hist_bin_data.append(samHistogramId, binsum, binstart, chromosome[0])

            print '\r',
            print '%013d: %07d' % (binstart, binsum),
            
            binstart = binsize * (column + 1)
            binvalues.append(binsum)
            binsum = 0
            column += 1

        # Fill in zero-coverage areas
        while column < pileupcolumn.pos / binsize:
            hist_bin_data.append(samHistogramId, 0, binstart, chromosome[0])
            
            binstart = binsize * (column + 1)
            binvalues.append(0)
            column += 1
        
        binsum += pileupcolumn.n

    print

    return binvalues

def calc_histogram_sum(samfile, chromosomes, samId, binSize, bins, db):

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

        print 'ChrID: %d, ChrName: %s' % (c[0], c[1])
        
        sam_hist10     = sam_hist_data.get_by_samid_binSize(samId, binSize*10)
        sam_hist100    = sam_hist_data.get_by_samid_binSize(samId, binSize*100)
        sam_hist1000   = sam_hist_data.get_by_samid_binSize(samId, binSize*1000)
        sam_hist10000  = sam_hist_data.get_by_samid_binSize(samId, binSize*10000)
        sam_hist100000 = sam_hist_data.get_by_samid_binSize(samId, binSize*100000)
    
        start = 0

        count = 0
        for b in bins[c[0]]:
            if count != 0:
                if count % 10 == 0:
                    hist_bin_data.append(sam_hist10['hist_id'], sum10, (count - 10) * binSize, c[0])
                    sum10 = 0
                if count % 100 == 0:
                    hist_bin_data.append(sam_hist100['hist_id'], sum100,  (count - 100) * binSize, c[0])
                    sum100 = 0
                if count % 1000 == 0:
                    hist_bin_data.append(sam_hist1000['hist_id'], sum1000, (count - 1000) * binSize, c[0])
                    sum1000 = 0
                if count % 10000 == 0:
                    hist_bin_data.append(sam_hist10000['hist_id'], sum10000, (count - 10000) * binSize, c[0])
                    sum10000 = 0
                if count % 100000 == 0:
                    hist_bin_data.append(sam_hist100000['hist_id'], sum100000, (count - 100000) * binSize, c[0])
                    sum100000 = 0

            sum10 += b
            sum100 += b
            sum1000 += b
            sum10000 += b
            sum100000 += b
                    
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
        print 'Error : please load "%s" first' % filename

    samId = sam['id']
    sam_hist_data.append(samId, binSize)
    sam_hist = sam_hist_data.get_by_samid(samId)

    print 'Calculate base histograms'
    
    for c in chromosomes:
        bins[c[0]] = calc_histogram(samfile, c, samId, binSize, sam_hist['hist_id'], db)
        
    if bins is not None:
        print 'Calculate extended histograms'
        calc_histogram_sum(samfile, chromosomes, samId, binSize, bins, db)
        #print "calc ended %s" % c[1]

def main():
    if len(sys.argv) < 2:
        print("No input files")
        sys.exit()

    start = time.time()
        
    files = sys.argv[1:]

    db = SQLDB(SAM_DB_NAME, SQLDB_HOST, SQLDB_USER, SQLDB_PASSWD)

    for f in files:
        run(f, 1000, db)

    print 'Process time: %d sec' % (time.time() - start)

if __name__ == '__main__':
    main()



