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

from sam.data.sql import SQLDB
from sam.data.chromosome import Chromosome
from sam.data.refGene import RefGene


from config import *


def load(filepath, db):

    filename = os.path.basename(filepath)

    refGene_data = RefGene(db)
    chr_data = Chromosome(db)

    print "begin to load", filename

    count = 0

    # load refGene data
    for line in open(filepath, 'r'):
        binsize, name, cyto_chr, cyto_strand, txStart, txEnd, cdsStart, cdsEnd, exonCount, exonStarts, exonEnds, score, geneName, cdsStartStat, cdsEndStat, exonFrames = line[:-1].split('\t')


        c_name = cyto_chr
        c_name = c_name.replace('Chr', '')
        c_name = c_name.replace('chr', '')
        c_name = c_name.replace('.', '')

        c = chr_data.get_by_name(c_name)

        if c is None:
            chr_data.append(c_name)
            c = chr_data.get_by_name(c_name)

        strand = 0
        if cyto_strand == '+':
            strand = 0
        elif cyto_strand == '-':
            strand = 1

        if cdsStartStat == 'none':
            startStat = 0
        elif cdsStartStat == 'unk':
            startStat = 1
        elif cdsStartStat == 'incmpl':
            startStat = 2
        elif cdsStartStat == 'cmpl':
            startStat = 3

        if cdsEndStat == 'none':
            endStat = 0
        elif cdsEndStat == 'unk':
            endStat = 1
        elif cdsEndStat == 'incmpl':
            endStat = 2
        elif cdsEndStat == 'cmpl':
            endStat = 3

        refGene_data.append(int(binsize),
                            name,
                            c['id'],
                            strand,
                            long(txStart),
                            long(txEnd),
                            long(cdsStart),
                            long(cdsEnd),
                            long(exonCount),
                            exonStarts,
                            exonEnds,
                            int(score),
                            geneName,
                            int(startStat),
                            int(endStat),
                            exonFrames)

        count += 1

        print "loaded %d refGenes\r" % count,

    print "loaded %d refGenes" % count


def main():

    if len(sys.argv) < 2:
        print("No input files")
        sys.exit()

    files = sys.argv[1:]

    db = SQLDB(SAM_DB_NAME, SQLDB_HOST, SQLDB_USER, SQLDB_PASSWD)

    for f in files:
        load(f, db)

if __name__ == '__main__':
    main()
