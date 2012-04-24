#definition of bed : http://genome.ucsc.edu/FAQ/FAQformat#format1
#reffered from http://www.python-forum.org/pythonforum/viewtopic.php?f=3&t=21356"

from sys import version
from itertools import izip_longest

if version[:1] != '2':
    v_py3x = True
else:
    v_py3x = False

    v_modecheck3x = lambda x: x.writable


class BedWriter(object):
    """A class for writing data in the BED format.
    You must pass it a open stream or file in the "w" mode, or it will raise
    an exception...
    There are a number of delimiters avalible for writing, the default is ','

    Create a writer then pass infomation in ether a dictionary or a list.
    e.g. w = BEDWriter(open('test.txt','w'))
    w.write(["chr22", "2000", "6000",
    "cloneB", "900 - 2000",
    "6000", "0", "2", "433",
    "399", "0","3601"])"""

    def __init__(self, out, delimiter = ',', topline = False):
        try:
            assert type(out) == file
            if v_py3x:
                assert v_modecheck3x(out) == 1
            else:
                assert out.mode in ('w', 'a')
        except AssertionError:
            print "IOError: Not a file object and/or in write mode"
            raise

        self.out = out
        self.delim = '%s ' % delimiter
        if topline:
            self.out.write(self.delim.join(['chrom','chromStart','chromEnd',
                                                'name','score','strand',
                                                'thinkStart','thickEnd','itemRgb',
                                                'blockCount','blockStarts']) + '\n')

    def _close(self):
        self.out.close()

    def _flush(self):
        self.out.flush()

    def write_list(self, a_list):
        """Writes list or iterable to file,
        this must iterate in the correct order.

        This is the simplest way to use BEDWriter.
        """
        self.out.write(self.delim.join(a_list) + '\n')

    def write_string(self, string, split = ' '):
        """This writes a string, if your having trouble with this method
        change the split to match your string e.g.
        chr22 1000 5000 cloneA 960+1000 5000 0 2 567 488 0 3512
        is split with ' ' which is the default but
        chr22, 1000, 5000, cloneA, 960 + 1000, 5000, 0, 2, 567,488, 0,3512
        is split with ',' so it should be set as a kwarg.
        """
        self.out.write(self.delim.join(string.split(split))+ '\n')

    def write_dict(self, a_dict):
        """Writes a dict to file.
        Expects four keys, others are optional
        Do not use.
        """
        self.out.write(self.delim.join(a_dict.keys())+ '\n')


class BedReader(object):
    """BEDReader, for reading files that have the BED format.
    You must pass BEDReader a readable file object or stream."""

    def __init__(self, source, delimiter = '\t'):
        try:
            assert type(source) == file
            if v_py3x:
                assert v_modecheck3x(source) == 0
            else:
                assert source.mode in ('r')
        except AssertionError:
            print "IOError: Not a file object and/or in read mode"
            raise

        self.source = source
        self.delim = delimiter

    def yield_lines(self):
        for line in self.source:
            yield line

    def _return(self, form, line):
        if form == 'list':
            return line.split(self.delim)
        elif form == 'tuple':
            return tuple(line.split(self.delim))
        elif form == 'dict':
            line = line[:-1].split(self.delim)
            required = ['chrom','chromStart','chromEnd']
            optional = ['chrom','chromStart','chromEnd',
                        'name','score','strand','thickStart',
                        'thickEnd','itemRgb','blockCount',
                        'blockSizes', 'blockStarts']
            report = dict(zip(required, line))
            report.update(izip_longest(optional, line, fillvalue='0'))
            return report

    def return_all(self):
        lines = self.yield_lines()
        results = []
        for line in lines:
            results.append(self._return('dict',line))
        return results

    def get_line(self, line):
        return self._return('dict', line)
