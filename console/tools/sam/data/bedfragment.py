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


class BedFragment:

    def __init__(self, db):
        self.db = db

    def append(self, bedId, chrId,
               chrStart, chrEnd, name, score, strand,
               thickStart, thickEnd,
               itemR, itemG, itemB,
               blockCount, blockSizes, blockStarts):

        SQL_TEMPLATE = u"INSERT INTO bed_fragment (bed_id, chr_id, chr_start, chr_end, name, score, strand, thick_start, thick_end, item_r, item_g, item_b, block_count, block_sizes, block_starts) VALUES (%d, %d, %d, %d, '%s', %d, %d, %d, %d, %d, %d, %d, %d, '%s', '%s')"

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
