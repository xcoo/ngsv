# -*- coding: utf-8 -*-

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
