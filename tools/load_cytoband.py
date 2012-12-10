#!/usr/bin/env python
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
import os.path

from sam.data.sql import SQLDB
from sam.data.chromosome import Chromosome
from sam.data.cytoband import CytoBand

from config import *


def load(filepath, db):

    filename = os.path.basename(filepath)

    cytoband_data = CytoBand(db)
    chr_data = Chromosome(db)

    print "begin to load", filename

    count = 0

    # load cytoband data
    for line in open(filepath, 'r'):
        cyto_chr, cyto_start, cyto_end, cyto_name, cyto_gie = line[:-1].split('\t')

        c_name = cyto_chr
        c_name = c_name.replace('Chr', '')
        c_name = c_name.replace('chr', '')
        c_name = c_name.replace('.', '')

        c = chr_data.get_by_name(c_name)

        if c is None:
            chr_data.append(c_name)
            c = chr_data.get_by_name(c_name)

        cytoband_data.append(c['id'],
                             long(cyto_start),
                             long(cyto_end),
                             cyto_name,
                             cyto_gie)

        count += 1

        print "loaded %d bands\r" % count,

    print "loaded %d bands" % count


def main():

    if len(sys.argv) < 2:
        print("No input files")
        sys.exit()

    f = sys.argv[1]

    db = SQLDB(SAM_DB_NAME, SQLDB_HOST, SQLDB_USER, SQLDB_PASSWD)

    load(f, db)

if __name__ == '__main__':
    main()
