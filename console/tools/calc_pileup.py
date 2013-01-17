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
import time
import pysam

import ngsv.cypileup

from sam.data.sql import SQLDB
from sam.data.sam import Sam
from sam.data.chromosome import Chromosome
from sam.util import trim_chromosome_name

from config import SQLDB_HOST, SQLDB_USER, SQLDB_PASSWD, SAM_DB_NAME

def load(filepath, db):
    filename = os.path.basename(filepath)

    file_ext = filename.split('.')[-1]

    if file_ext != 'bam':
        print 'Error: not supported file format'
        return

    print 'begin to load', filename

    samfile = pysam.Samfile(filepath)

    return samfile


def run(filepath, db):
    samfile = load(filepath, db)

    sam_data = Sam(db)
    chromosome_data = Chromosome(db)

    chromosomes = []
    for ref in samfile.references:
        name = trim_chromosome_name(ref)

        c = chromosome_data.get_by_name(name)
        c['ref'] = ref

        chromosomes.append(c)

    filename = os.path.basename(filepath)
    sam = sam_data.get_by_filename(filename)

    if sam is None:
        print 'Error : please load "%s" first' % filename

    samId = sam['id']

    # cypileup
    ngsv.cypileup.pileup(samfile, chromosomes, samId, db)

    samfile.close()


def main():
    if len(sys.argv) < 2:
        print("No input files")
        sys.exit()

    start1 = time.time()
    start2 = time.clock()

    files = sys.argv[1:]

    db = SQLDB(SAM_DB_NAME, SQLDB_HOST, SQLDB_USER, SQLDB_PASSWD)

    for f in files:
        run(f, db)

    print 'Real time: %d sec' % (time.time()  - start1)
    print 'CPU  time: %d sec' % (time.clock() - start2)

if __name__ == '__main__':
    main()
