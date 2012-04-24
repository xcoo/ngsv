# -*- coding: utf-8 -*-

from sql import *

import json


class Sam:

    def __init__(self, db):
        self.db = db

    def get_by_filename(self, filename):

        sql = u"SELECT samId, fileName, createdDate from sam where fileName = '%s'" % escape(filename)

        result = self.db.execute(sql)

        if len(result) > 0:
            return {'id': result[0][0],
                    'name': result[0][1],
                    'created': result[0][2]}
        else:
            return None

    def get_all(self):
        
        sql = u"SELECT samId FROM sam"
        result = self.db.execute(sql)
        if len(result) > 0:
            return result
        else:
            return None


    def append(self, filename,
               header, lengths, mapped, nreferences, references):

        SQL_TEMPLATE = u"INSERT INTO sam (fileName, createdDate, header, lengths, mapped, numberOfChromosomes, chromosomes) VALUES ('%s', %d, '%s', '%s', '%s', %d, '%s')"

        sql = SQL_TEMPLATE % (escape(filename),
                              get_time(),
                              escape(json.dumps(header)),
                              escape(json.dumps(lengths)),
                              mapped,
                              nreferences,
                              escape(json.dumps(references)))

        self.db.execute(sql)
