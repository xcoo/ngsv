from distutils.core import setup
from distutils.extension import Extension
from Cython.Distutils import build_ext

ext_modules = [Extension('ngsv.cypileup', ['cypileup.pyx'])]

setup(
  name = 'ngsv cypileup',
  cmdclass = { 'build_ext': build_ext },
  ext_modules = ext_modules
)
