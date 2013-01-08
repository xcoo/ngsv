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

import ConfigParser

class Config(object):

    def __init__(self, ini_file):
        conf = ConfigParser.SafeConfigParser()
        conf.read(ini_file)

        self.__db_host = conf.get('db', 'host')
        self.__db_user = conf.get('db', 'user')
        self.__db_password = conf.get('db', 'password')
        self.__db_name = conf.get('db', 'db_name')

        self.__console_debug = conf.get('console', 'debug')
        self.__console_testing = conf.get('console', 'testing')
        self.__console_upload_dir = conf.get('console', 'upload_dir')

    @property
    def db_host(self):
        return self.__db_host
    
    @property
    def db_user(self):
        return self.__db_user

    @property
    def db_password(self):
        return self.__db_password

    @property
    def db_name(self):
        return self.__db_name
    
    @property
    def debug(self):
        return self.__console_debug

    @property
    def testing(self):
        return self.__console_testing

    @property
    def upload_dir(self):
        return self.__console_upload_dir
