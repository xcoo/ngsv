#!/usr/bin/env python
# -*- coding: utf-8 -*-

import sys
import os.path

import pysam

from sam.data.sql import SQLDB
from sam.data.sam import Sam
from sam.data.chromosome import Chromosome
from sam.data.shortread import ShortRead

from config import *


class FetchCallback:

    def __init__(self, sr_data, chr_data, sam_id, references):

        self.sr_data = sr_data
        self.chr_data = chr_data

        self.count = 0
        self.sam_id = sam_id
        self.references = references

        self.cmap = {}

    def __call__(self, sr):
        self.count += 1

        chr_name = self.references[sr.rname]

        if chr_name in self.cmap.keys():
            chr_id = self.cmap[chr_name]
        else:
            c = self.chr_data.get_by_name(chr_name)
            self.cmap[chr_name] = c['id']
            chr_id = c['id']

        if sr.aend == None and sr.alen == None:
            sr_length = sr.qstart + sr.qlen
            sr_end = sr.pos + sr.qstart + sr.qlen
        else:
            sr_length = sr.alen
            sr_end = sr.aend

        sr_start = sr.pos

        self.sr_data.append(self.sam_id, sr.qname, chr_id,
                            sr_start, sr_end, sr_length,
                            sr.qstart, sr.qend, sr.qlen,
                            long(sr.bin),
                            sr.flag,
                            sr.tags,
                            sr.seq)


def load(filepath, db):

    filename = os.path.basename(filepath)

    file_ext = filename.split('.')[-1]

    if file_ext != 'bam':
        print 'Error: not supported file format'
        return

    sam_data = Sam(db)
    chr_data = Chromosome(db)
    sr_data = ShortRead(db)

    if sam_data.get_by_filename(filename) is not None:
        print "Error : already loaded \"%s\"" % filename
#        return

    print "begin to load", filename

    # load sam
    samfile = pysam.Samfile(filepath)

    sam_data.append(filename,
                    samfile.header, samfile.lengths,
                    samfile.mapped,
                    samfile.nreferences, samfile.references)

    sam = sam_data.get_by_filename(filename)

    # load chromosome
    for c in samfile.references:
        chr_data.append(c)

    # load short reads
    c = FetchCallback(sr_data, chr_data,
                      sam['id'], samfile.references)

    count = 0

    for ref in samfile.references:
        c.count = 0
        samfile.fetch(reference=ref, callback=c)
        print "loaded %d reads for reference \'%s\'\r" % (c.count, ref)
        count += c.count
        db.commit()

    print "finsiehd loading %d reads" % count


def main():

    if len(sys.argv) < 2:
        print("No input files")
        sys.exit()

    files = sys.argv[1:]

    db = SQLDB(SAM_DB_NAME, SQLDB_HOST, SQLDB_USER, SQLDB_PASSWD)

    for f in files:
        load(f, db)

if __name__ == '__main__':
    main()
