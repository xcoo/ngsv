# -*- coding: utf-8 -*-

from sql import *


class HistogramBin:

    def __init__(self, db):

        self.db = db


    def append(self, samHistogramId, count, position, chrId):

        SQL_TEMPLATE = u"INSERT INTO histogramBin (samHistogramId, value, position, chrId) VALUES (%d, %d, %d, %d)"

        sql = SQL_TEMPLATE % (samHistogramId,
                              count, 
                              position,
                              chrId)

        self.db.execute(sql)
