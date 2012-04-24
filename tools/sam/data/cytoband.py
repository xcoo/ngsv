# -*- coding: utf-8 -*-

from sql import *

import json


class CytoBand:

    def __init__(self, db):
        self.db = db

    def append(self, chrId, chrStart, chrEnd, name, gieStain):

        SQL_TEMPLATE = u"INSERT INTO cytoBand (chrId, chrStart, chrEnd, name, gieStain) VALUES (%d, %d, %d, '%s', '%s')"

        sql = SQL_TEMPLATE % (chrId,
                              chrStart,
                              chrEnd,
                              escape(name),
                              escape(gieStain))

        self.db.execute(sql)
