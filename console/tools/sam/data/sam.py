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


class Sam:

    def __init__(self, db):
        self.db = db

    def get_by_filename(self, filename):

        sql = u"SELECT sam_id, file_name, created_date FROM sam WHERE file_name = '%s'" % escape(filename)

        result = self.db.execute(sql)

        if len(result) > 0:
            return {'id': result[0][0],
                    'name': result[0][1],
                    'created': result[0][2]}
        else:
            return None

    def get_all(self):
        
        sql = u"SELECT sam_id FROM sam"
        result = self.db.execute(sql)
        if len(result) > 0:
            return result
        else:
            return None


    def append(self, filename,
               header, lengths, mapped, nreferences, references):

        SQL_TEMPLATE = u"INSERT INTO sam (file_name, created_date, header, lengths, mapped, number_of_chromosomes, chromosomes) VALUES ('%s', %d, '%s', '%s', '%s', %d, '%s')"

        sql = SQL_TEMPLATE % (escape(filename),
                              get_time(),
                              escape(json.dumps(header)),
                              escape(json.dumps(lengths)),
                              mapped,
                              nreferences,
                              escape(json.dumps(references)))

        self.db.execute(sql)
