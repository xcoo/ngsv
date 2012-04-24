# -*- coding: utf-8 -*-

from sql import *

import json


class Bed:

    def __init__(self, db):
        self.db = db

    def get_by_filename(self, filename):

        sql = u"SELECT bedId, fileName, createdDate, itemRgb from bed where fileName = '%s'" % escape(filename)

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

        SQL_TEMPLATE = u"INSERT INTO bed (fileName, createdDate, trackName, description, visibility, itemRgb) VALUES ('%s', %d, '%s', '%s', %d, %d)"

        sql = SQL_TEMPLATE % (escape(filename),
                              get_time(),
                              escape(json.dumps(trackname)),
                              escape(json.dumps(description)),
                              visibility,
                              itemRgb)

        self.db.execute(sql)
