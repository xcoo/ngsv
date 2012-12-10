#!/usr/bin/env python
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
from sam.util import trim_chromosome_name

from config import *

def calc_hist_all(samfile, samId, db):
    sam_hist = SamHistogram(db)
    hist_bin = HistogramBin(db)
    chromosome = Chromosome(db)

    bins = (
#        { 'size': 1,        'sum': 0, 'hist_id': 0, 'pos': 0 },
#        { 'size': 10,        'sum': 0, 'hist_id': 0, 'pos': 0 },
        { 'size': 100,       'sum': 0, 'hist_id': 0, 'pos': 0 },
        { 'size': 1000,      'sum': 0, 'hist_id': 0, 'pos': 0 },
        { 'size': 10000,     'sum': 0, 'hist_id': 0, 'pos': 0 },
        { 'size': 100000,    'sum': 0, 'hist_id': 0, 'pos': 0 },
        { 'size': 1000000,   'sum': 0, 'hist_id': 0, 'pos': 0 },
        { 'size': 10000000,  'sum': 0, 'hist_id': 0, 'pos': 0 },
        { 'size': 100000000, 'sum': 0, 'hist_id': 0, 'pos': 0 }
        )

    bufsize = 10000

    for b in bins:
        sam_hist.append(samId, b['size'])
        b['hist_id'] = sam_hist.get_by_samid_binSize(samId, b['size'])['hist_id']

    for r in samfile.references:
        c = chromosome.get_by_name(r)
        if c == None:
            continue
        print 'ChrID: %d, ChrName: %s' % (c['id'], c['name'])
        
        for p in samfile.pileup(str(c['name'])):

            for b in bins:
                if b['size'] == 1:
                    hist_bin.appendbuf(bin0_hist_id, p.n, p.pos, c[0])
                    if hist_bin.lenbuf() >= bufsize:
                        hist_bin.flush()
                else:
                    if p.pos >= b['pos'] + b['size']:
                        hist_bin.appendbuf(b['hist_id'], b['sum'], b['pos'], c['id'])

                        if hist_bin.lenbuf() >= bufsize:
                            hist_bin.flush()

                        if b['size'] == 1000:
                            print '%11d: %6d\r' % (b['pos'], b['sum']),                 
                    
                        b['sum'] = 0
                        b['pos'] = p.pos / b['size'] * b['size']

                    b['sum'] += p.n

        for b in bins:
            if b['sum'] != 0:
                hist_bin.appendbuf(b['hist_id'], b['sum'], b['pos'], c['id'])

        hist_bin.flush()
                
        for b in bins:
            b['sum'] = 0
            b['pos'] = 0

def calc_hist(samfile, chromosomes, samId, binsize, db):

    bins = {}
    
    sam_hist_data = SamHistogram(db)
    hist_bin_data = HistogramBin(db)

    binstart = 0
    binsum = 0
    binvalues = []
    column = 0

    bufsize = 10000

    sam_hist_data.append(samId, binsize)
    samHistogramId = sam_hist_data.get_by_samid(samId)['hist_id']

    for chromosome in chromosomes:

        print 'ChrID: %d, ChrName: %s' % (chromosome['id'], chromosome['name'])
    
        for pileupcolumn in samfile.pileup(str(chromosome['ref'])):

            # Sum up counts within the region
            if pileupcolumn.pos >= binsize * (column + 1):
                hist_bin_data.appendbuf(samHistogramId, binsum, binstart, chromosome['id'])

                if hist_bin_data.lenbuf() >= bufsize:
                    hist_bin_data.flush()

                print '%11d: %6d\r' % (binstart, binsum),
            
                binstart = binsize * (column + 1)
                binvalues.append(binsum)
                binsum = 0
                column += 1

            # Fill in zero-coverage areas
            # NOTE: Not insert zero values to DB
            while column < pileupcolumn.pos / binsize:            
                binstart = binsize * (column + 1)
                binvalues.append(0)
                column += 1
        
            binsum += pileupcolumn.n

        hist_bin_data.flush()

        bins[chromosome['id']] = binvalues
        
        binsum = 0
        binstart = 0
        column = 0

    return bins


def calc_histogram_sum(samfile, chromosomes, samId, binSize, bins, db):

    sam_hist_data = SamHistogram(db)
    hist_bin_data = HistogramBin(db)

    sum10 = 0
    sum100 = 0
    sum1000 = 0
    sum10000 = 0
    sum100000 = 0

    sam_hist_data.append(samId, binSize * 10)
    sam_hist_data.append(samId, binSize * 100)
    sam_hist_data.append(samId, binSize * 1000)
    sam_hist_data.append(samId, binSize * 10000)
    sam_hist_data.append(samId, binSize * 100000)

    bufsize = 10000

    for c in chromosomes:

        print 'ChrID: %d, ChrName: %s' % (c['id'], c['name'])

        sam_hist10     = sam_hist_data.get_by_samid_binSize(samId, binSize * 10)
        sam_hist100    = sam_hist_data.get_by_samid_binSize(samId, binSize * 100)
        sam_hist1000   = sam_hist_data.get_by_samid_binSize(samId, binSize * 1000)
        sam_hist10000  = sam_hist_data.get_by_samid_binSize(samId, binSize * 10000)
        sam_hist100000 = sam_hist_data.get_by_samid_binSize(samId, binSize * 100000)

        start = 0

        count = 0
        for b in bins[c['id']]:
            if count != 0:
                if count % 10 == 0:
                    if sum10 != 0:
                        hist_bin_data.appendbuf(sam_hist10['hist_id'], sum10, (count - 10) * binSize, c['id'])
                    sum10 = 0
                if count % 100 == 0:
                    if sum100 != 0:
                        hist_bin_data.appendbuf(sam_hist100['hist_id'], sum100,  (count - 100) * binSize, c['id'])
                    sum100 = 0
                if count % 1000 == 0:
                    if sum1000 != 0:
                        hist_bin_data.appendbuf(sam_hist1000['hist_id'], sum1000, (count - 1000) * binSize, c['id'])
                    sum1000 = 0
                if count % 10000 == 0:
                    if sum10000 != 0:
                        hist_bin_data.appendbuf(sam_hist10000['hist_id'], sum10000, (count - 10000) * binSize, c['id'])
                    sum10000 = 0
                if count % 100000 == 0:
                    if sum100000 != 0:
                        hist_bin_data.appendbuf(sam_hist100000['hist_id'], sum100000, (count - 100000) * binSize, c['id'])
                    sum100000 = 0

            if hist_bin_data.lenbuf() >= bufsize:
                hist_bin_data.flush()

            sum10 += b
            sum100 += b
            sum1000 += b
            sum10000 += b
            sum100000 += b

            count += 1
            
        hist_bin_data.flush()


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

    chromosomes = []
    for ref in samfile.references:
        name = trim_chromosome_name(ref)

        c = chromosome_data.get_by_name(name)
        c['ref'] = ref

        chromosomes.append(c)

    filename = os.path.basename(filepath)
    sam = sam_data.get_by_filename(filename)

    if sam is None:
        print 'Error : please load "%s" first' % filename

    samId = sam['id']

    # Algorithm 1
    print 'Calculate base histograms'   
    bins = calc_hist(samfile, chromosomes, samId, binSize, db)
        
    if bins is not None:
        print 'Calculate extended histograms'
        calc_histogram_sum(samfile, chromosomes, samId, binSize, bins, db)
    
    # Algorithm 2
#    calc_hist_all(samfile, samId, db)

    samfile.close()


def main():
    if len(sys.argv) < 2:
        print("No input files")
        sys.exit()

    start1 = time.time()
    start2 = time.clock()

    files = sys.argv[1:]

    db = SQLDB(SAM_DB_NAME, SQLDB_HOST, SQLDB_USER, SQLDB_PASSWD)

    for f in files:
        run(f, 1000, db)

    print 'Real time: %d sec' % (time.time()  - start1)
    print 'CPU  time: %d sec' % (time.clock() - start2)

if __name__ == '__main__':
    main()
