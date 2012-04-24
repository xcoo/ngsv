#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sys

from sam.data.sql import SQLDB
from sam.data.sam import Sam
from sam.data.chromosome import Chromosome
from sam.data.shortread import ShortRead
from sam.data.samhistogram import SamHistogram
from sam.data.histogrambin import HistogramBin

from config import *


def run(binSize, db):

    sam_data = Sam(db)
    chromosome_data = Chromosome(db)
    sam_hist_data = SamHistogram(db)

    sam = sam_data.get_all()
    chromosome = chromosome_data.get_all()

    for s in sam:
        samId = s[0]
        sam_hist_data.append(samId, binSize)
        sam_hist = sam_hist_data.get_by_samid(samId)

        for c in chromosome:
            chrId = c[0]
            print("samId: %d, chrId: %d" % (samId, chrId))
            calc_hist(binSize, samId, sam_hist['hist_id'], chrId, db)


def calc_hist(binSize, samId, samHistogramId, chrId, db):

    short_read_data = ShortRead(db)
    hist_bin_data   = HistogramBin(db)

    maxRefEnd = short_read_data.max(samId, chrId)
    if maxRefEnd is None:
        return

    for start in range(0, maxRefEnd, binSize):
        print("samId: %d, chrId: %d, position: %d" % (samId, chrId, start))
        end   = start + binSize
        count = short_read_data.count(samId, chrId, start, end)
        hist_bin_data.append(samHistogramId, count, start, chrId)


def main():

    db = SQLDB(SAM_DB_NAME, SQLDB_HOST, SQLDB_USER, SQLDB_PASSWD)

    if len(sys.argv) == 1:
        run(100,    db)
        run(1000,   db)
        run(10000,  db)
        run(100000, db)
    elif len(sys.argv) == 2:
        binSize = int(sys.argv[1])
        run(binSize, db)
    else:
        print("invalid arguments")
        sys.exit()


if __name__ == '__main__':
    main()
