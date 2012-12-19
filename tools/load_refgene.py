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
import gzip

from cache import Cache

from sam.data.sql import SQLDB
from sam.data.chromosome import Chromosome
from sam.data.refgene import RefGene

from config import *


def _update_database(fp, refgene_data, chromosome_data):

    count = 0

    for line in fp:
        binsize, name, cyto_chr, cyto_strand, txStart, txEnd, cdsStart, cdsEnd, exonCount, exonStarts, exonEnds, score, geneName, cdsStartStat, cdsEndStat, exonFrames = line[:-1].split('\t')

        c_name = cyto_chr
        c_name = c_name.replace('Chr', '')
        c_name = c_name.replace('chr', '')
        c_name = c_name.replace('.', '')

        c = chromosome_data.get_by_name(c_name)

        if c is None:
            chromosome_data.append(c_name)
            c = chromosome_data.get_by_name(c_name)

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

        refgene_data.append(int(binsize),
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

    print "loaded %d refGenes" % count


def load():

    db = SQLDB(SAM_DB_NAME, SQLDB_HOST, SQLDB_USER, SQLDB_PASSWD)

    refgene_data = RefGene(db)
    chromosome_data = Chromosome(db)

    if refgene_data.count() > 0:
        print "RefGene is already loaded"
        sys.exit()

    url = "http://hgdownload.cse.ucsc.edu/goldenPath/hg19/database/refGene.txt.gz"

    print "begin to download from %s" % url

    c = Cache(url)
    c.load()

    print "updating database"

    f = gzip.open(c.name)

    _update_database(f, refgene_data, chromosome_data)

    f.close()


if __name__ == '__main__':
    load()
