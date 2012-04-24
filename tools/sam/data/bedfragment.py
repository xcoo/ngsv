# -*- coding: utf-8 -*-

from sql import *

import json


class BedFragment:

    def __init__(self, db):
        self.db = db

    def append(self, bedId, chrId,
               chrStart, chrEnd, name, score, strand,
               thickStart, thickEnd,
               itemR, itemG, itemB,
               blockCount, blockSizes, blockStarts):

        SQL_TEMPLATE = u"INSERT INTO bedFragment (bedId, chrId, chrStart, chrEnd, name, score, strand, thickStart, thickEnd, itemR, itemG, itemB, blockCount, blockSizes, blockStarts) VALUES (%d, %d, %d, %d, '%s', %d, %d, %d, %d, %d, %d, %d, %d, '%s', '%s')"

        sql = SQL_TEMPLATE % (bedId,
                              chrId,
                              chrStart,
                              chrEnd,
                              escape(name),
                              score,
                              strand,
                              thickStart,
                              thickEnd,
                              itemR,
                              itemG,
                              itemB,
                              blockCount,
                              escape(json.dumps(blockSizes)),
                              escape(json.dumps(blockStarts)))

        self.db.execute(sql)
