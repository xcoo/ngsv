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

# -*- coding: utf-8 -*-

from sql import *

import json


class ShortRead:

    def __init__(self, db):
        self.db = db

    def append(self, samId, name, chrId,
               start, end, length,
               qstart, qend, qlength, qbin, qflag, qtags,
               seq):

        SQL_TEMPLATE = u"INSERT INTO shortRead (samId, name, chrId, refStart, refEnd, refLength, queryStart, queryEnd, queryLength, queryBin, queryFlag, queryTags, sequence) VALUES (%d, '%s', %d, %d, %d, %d, %d, %d, %d, %d, %d, '%s', '%s')"

        sql = SQL_TEMPLATE % (samId,
                              escape(name),
                              chrId,
                              start,
                              end,
                              length,
                              qstart,
                              qend,
                              qlength,
                              qbin,
                              qflag,
                              escape(json.dumps(qtags)),
                              escape(seq))

        self.db.execute(sql, fetch_length=0)


    def count(self, samId, chrId, start, end):

## simple count
        SQL_TEMPLATE = u"SELECT count(*) FROM shortRead WHERE samId = %d AND chrId = %d AND NOT (refStart < %d AND refEnd < %d OR %d <= refStart AND %d <= refEnd)"

        sql = SQL_TEMPLATE % (samId, chrId,
                              start, start,
                              end,   end)

        result = self.db.execute(sql)

## use FOUND_ROWS()
#       SQL_TEMPLATE = u"SELECT SQL_CALC_FOUND_ROWS shortReadId FROM shortRead WHERE samId = %d AND chrId = %d AND NOT (refStart < %d AND refEnd < %d OR %d <= refStart AND %d <= refEnd)"
#
#  	    sql = SQL_TEMPLATE % (samId, chrId,
#                              start, start,
#                              end,   end)
#
#       result = self.db.execute(sql)
#       result = self.db.execute(u"SELECT FOUND_ROWS()")

        if len(result) > 0:
            return result[0][0]
        else:
            return None


    def max(self, samId, chrId):

        SQL_TEMPLATE = u"SELECT refEnd FROM shortRead WHERE samId = %d AND chrId = %d ORDER BY refEnd DESC LIMIT 1"

        sql = SQL_TEMPLATE % (samId, chrId)

        result = self.db.execute(sql)

        if len(result) > 0:
            return result[0][0]
        else:
            return None
