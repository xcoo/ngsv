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
import urllib2
import tempfile


def _default_download_report(bytes_so_far, chunk_size, total_size):
    percent = float(bytes_so_far) / total_size
    percent = round(percent * 100, 2)

    sys.stdout.write("Downloaded %d of %d bytes (%0.2f%%)\r" %
                     (bytes_so_far, total_size, percent))
    sys.stdout.flush()

    if bytes_so_far >= total_size:
        sys.stdout.write('\n')


class Cache(object):

    def __init__(self, url, report_func=_default_download_report):
        self.response = urllib2.urlopen(url)
        self.report_func = report_func
        self.file = tempfile.NamedTemporaryFile()
        self.name = self.file.name

    def __del__(self):
        self.file.close()
        self.response.close()

    def load(self):
        self._chunk_read(self.response, report_hook=self.report_func)

    def _chunk_read(self, response, chunk_size=8192, report_hook=None):
        total_size = response.info().getheader('Content-Length').strip()
        total_size = int(total_size)
        bytes_so_far = 0

        while 1:
            chunk = response.read(chunk_size)

            self.file.write(chunk)

            bytes_so_far += len(chunk)

            if not chunk:
                break

            if report_hook:
                report_hook(bytes_so_far, chunk_size, total_size)

        self.file.seek(0)

        return bytes_so_far
