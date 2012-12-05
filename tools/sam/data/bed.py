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


class Bed:

    def __init__(self, db):
        self.db = db

    def get_by_filename(self, filename):

        sql = u"SELECT bed_id, file_name, created_date, item_rgb FROM bed WHERE file_name = '%s'" % escape(filename)

        result = self.db.execute(sql)

        if len(result) > 0:
            return {'id': result[0][0],
                    'name': result[0][1],
                    'created': result[0][2],
                    'rgb': result[0][3]}
        else:
            return None

    def append(self, filename,
               trackname, description, visibility, itemRgb):

        SQL_TEMPLATE = u"INSERT INTO bed (file_name, created_date, track_name, description, visibility, item_rgb) VALUES ('%s', %d, '%s', '%s', %d, %d)"

        sql = SQL_TEMPLATE % (escape(filename),
                              get_time(),
                              escape(json.dumps(trackname)),
                              escape(json.dumps(description)),
                              visibility,
                              itemRgb)

        self.db.execute(sql)
