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

import sys
#import threading
MAX = sys.maxint

import MySQLdb

import datetime
import time


def escape(s):
    return MySQLdb.escape_string(s)


def get_time():
    d = datetime.datetime.today()
    return int(time.mktime(d.timetuple()))


class SQLDB(object):

    def __init__(self, db_name=None, host="localhost",
                 user="root", passwd="root", charset="utf8"):

        self.db_name = db_name
        self.host = host
        self.user = user
        self.passwd = passwd
        self.charset = charset

        self._db = MySQLdb.connect(host=host,
                                   user=user, passwd=passwd,
                                   db=db_name, charset=charset)
#        self._cursor_mutex = threading.Semaphore(1)

    def execute(self, sql, fetch_length=MAX):
#        self._cursor_mutex.acquire()

        result = None

        try:
            c = self._db.cursor()
            c.execute(sql)

            if fetch_length > 0:
                result = c.fetchmany(fetch_length)
            else:
                result = None

#            self._db.commit()

            c.close()

        except:
            raise

        finally:
            pass
#            self._cursor_mutex.release()

        return result

    def commit(self):

        self._db.commit()

    def executemany(self, sql, values):

        try:
            c = self._db.cursor()
            c.executemany(sql, values)

            c.close()

        except:
            raise

        finally:
            pass
