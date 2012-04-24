# -*- coding: utf-8 -*-

from sql import *


class SamHistogram:

    def __init__(self, db):

        self.db = db


    def get_by_samid(self, samId):

        sql = u"SELECT samId, binSize, samHistogramId FROM samHistogram WHERE samId = '%d' ORDER BY createdDate DESC LIMIT 1" % samId

        result = self.db.execute(sql)

        if len(result) > 0:
            return {'id': result[0][0],
                    'size': result[0][1],
                    'hist_id': result[0][2]}
        else:
            return None


    def get_by_samid_binSize(self, samId, binSize):

        sql = u"SELECT samId, binSize, samHistogramId FROM samHistogram WHERE samId = '%d' AND binSize = '%d' ORDER BY createdDate DESC LIMIT 1" % (samId, binSize)

        result = self.db.execute(sql)

        if len(result) > 0:
            return {'id': result[0][0],
                    'size': result[0][1],
                    'hist_id': result[0][2]}
        else:
            return None



    def append(self, samId, binSize):

        sql = u"INSERT INTO samHistogram (samId, binSize, createdDate) VALUES (%d, %d, %d)" % (samId, binSize, get_time())

        self.db.execute(sql)
