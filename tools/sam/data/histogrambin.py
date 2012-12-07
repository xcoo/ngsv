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


class HistogramBin:

    def __init__(self, db):

        self._db = db
        self._buf = []


    def append(self, samHistogramId, count, position, chrId):

        SQL_TEMPLATE = u"INSERT INTO histogram_bin (sam_histogram_id, value, position, chr_id) VALUES (%d, %d, %d, %d)"

        sql = SQL_TEMPLATE % (samHistogramId,
                              count, 
                              position,
                              chrId)

        self._db.execute(sql)

    def appendbuf(self, samHistogramId, count, position, chrId):
        self._buf.append((samHistogramId, count, position, chrId))

    def lenbuf(self):
        return len(self._buf)
        
    def flush(self):
        if len(self._buf) > 0:
            sql = u"INSERT INTO histogram_bin (sam_histogram_id, value, position, chr_id) VALUES (%s, %s, %s, %s)"
            self._db.executemany(sql, self._buf)
            del self._buf[:]
