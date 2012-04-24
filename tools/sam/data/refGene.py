# -*- coding: utf-8 -*-

from sql import *

import json


class RefGene:

    def __init__(self, db):
        self.db = db

    def append(self, bin, name, chrId, strand, txStart, txEnd, 
               cdsStart, cdsEnd, exonCount, exonStarts, exonEnds, score,
               geneName, cdsStartStat, cdsEndStat, exonFrames):

        SQL_TEMPLATE = u"INSERT INTO refGene (bin, name, chrId, strand, txStart, txEnd, cdsStart, cdsEnd, exonCount, exonStarts, exonEnds, score, geneName, cdsStartStat, cdsEndStat, exonFrames) VALUES (%d, '%s', %d, %d, %d, %d, %d, %d, %d, '%s', '%s', %d, '%s', %d, %d, '%s')"

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
