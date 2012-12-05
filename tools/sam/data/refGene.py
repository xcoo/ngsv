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

from sql import *

import json


class RefGene:

    def __init__(self, db):
        self.db = db

    def append(self, bin, name, chrId, strand, txStart, txEnd, 
               cdsStart, cdsEnd, exonCount, exonStarts, exonEnds, score,
               geneName, cdsStartStat, cdsEndStat, exonFrames):

        SQL_TEMPLATE = u"INSERT INTO ref_gene (bin, name, chr_id, strand, tx_start, tx_end, cds_start, cds_end, exon_count, exon_starts, exon_ends, score, gene_name, cds_start_stat, cds_end_stat, exon_frames) VALUES (%d, '%s', %d, %d, %d, %d, %d, %d, %d, '%s', '%s', %d, '%s', %d, %d, '%s')"

        sql = SQL_TEMPLATE % (bin, 
                              escape(name), 
                              chrId,
                              strand,
                              txStart,
                              txEnd, 
                              cdsStart,
                              cdsEnd,
                              exonCount,
                              escape(json.dumps(exonStarts)),
                              escape(json.dumps(exonEnds)),
                              score,
                              escape(geneName),
                              cdsStartStat,
                              cdsEndStat,
                              escape(json.dumps(exonFrames)))

        self.db.execute(sql)
