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

import datetime
import math
import os
import re
import sys

from flask import Flask, Response
from flask import render_template, redirect, request, abort
from werkzeug import SharedDataMiddleware
from werkzeug import secure_filename

app = Flask(__name__)

try:
    app.config.from_envvar('NGSV_CONSOLE_CONFIG')
except RuntimeError:
    app.debug = True
    app.testing = True
    app.config['DB_URI'] = 'mysql://root:root@localhost/samdb?charset=utf8'
    app.config['UPLOAD_DIR'] = os.environ['TMPDIR']

app.wsgi_app = SharedDataMiddleware(app.wsgi_app, {
        '/': os.path.join(os.path.dirname(__file__), 'static')
        })

@app.route('/')
def root():
    return render_template('main.html')

@app.route('/api/upload-sam', methods=[ 'POST' ])
def upload_sam():
    file = request.files['file']
    if file and allowed_file(file.filename, [ 'sam', 'bam' ]):
        filename = secure_filename(file.filename)
        file.save(os.path.join(app.config['UPLOAD_DIR'], filename))
    return redirect('/')

@app.route('/api/upload-bed', methods=[ 'POST' ])
def upload_bed():
    file = request.files['file']
    if file and allowed_file(file.filename, [ 'bed' ]):
        filename = secure_filename(file.filename)
        file.save(os.path.join(app.config['UPLOAD_DIR'], filename))
    return redirect('/')

def allowed_file(filename, extensions):
    return '.' in filename and \
           filename.rsplit('.', 1)[1] in extensions

if __name__ == '__main__':
    app.run()
