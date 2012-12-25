from distutils.core import setup
from distutils.extension import Extension
from Cython.Distutils import build_ext

ext_module = Extension('ngsv.cypileup', [ 'ngsv/cypileup.pyx' ])

setup(
  name = 'ngsv cypileup',
  version = '0.1.0',
  description = 'ngsv cypileup',
  url = 'http://www.xcoo.jp/',
  author = 'Xcoo, Inc.',
  ext_modules = [ ext_module ],
  cmdclass = { 'build_ext': build_ext },
  packages = [ 'ngsv' ]
)
