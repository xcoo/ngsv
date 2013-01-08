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

import os
import sys

from flask import Flask, Response
from flask import render_template, redirect, request, abort
from werkzeug import SharedDataMiddleware
from werkzeug import secure_filename

from celery.result import AsyncResult
from task_server.tasks import load_bam, load_bed

from config import Config

app = Flask(__name__)

try:
    ini = os.environ['NGSV_CONSOLE_CONFIG']
except KeyError:
    ini = os.path.join(os.path.dirname(__file__), '../config/ngsv.ini')

if not os.path.isfile(ini):
    sys.exit('Not found "ngsv.ini"')

conf = Config(ini)

app.debug = conf.debug
app.testing = conf.testing

app.wsgi_app = SharedDataMiddleware(app.wsgi_app, {
        '/': os.path.join(os.path.dirname(__file__), 'static')
        })

results = []

@app.route('/')
def root():
    bam_load_progress = 0
    hist_calc_progress = 0
    bed_load_progress = 0
    
    for r in results:
        if r.task_name == 'tasks.load_bam':
            if r.status == 'BAM_FINISH':
                bam_load_progress = '100%'
            if r.status == 'PROGRESS':
                bam_load_progress = '100%'
                hist_calc_progress = str(100 * r.result['current'] / r.result['total']) + '%'
            if r.status == 'SUCCESS':
                bam_load_progress = '100%'
                hist_calc_progress = '100%'
                
        if r.task_name == 'tasks.load_bed':
            if r.status == 'SUCCESS':
                bed_load_progress = '100%'
        
    return render_template('main.html',
                           bam_load_progress=bam_load_progress,
                           hist_calc_progress=hist_calc_progress,
                           bed_load_progress=bed_load_progress)

@app.route('/api/upload-bam', methods=[ 'POST' ])
def upload_bam():
    f = request.files['file']
    if f and allowed_file(f.filename, [ 'bam' ]):
        filename = secure_filename(f.filename)
        bam_file = os.path.join(conf.upload_dir, filename) 
        f.save(bam_file)

        r = load_bam.delay(bam_file, conf)
        results.append(r)

    return redirect('/')

@app.route('/api/upload-bed', methods=[ 'POST' ])
def upload_bed():
    f = request.files['file']
    if f and allowed_file(f.filename, [ 'bed' ]):
        filename = secure_filename(f.filename)
        bed_file = os.path.join(conf.upload_dir, filename) 
        f.save(bed_file)

        r = load_bed.delay(bed_file, conf)
        results.append(r)
        
    return redirect('/')

def allowed_file(filename, extensions):
    return '.' in filename and \
           filename.rsplit('.', 1)[1] in extensions

if __name__ == '__main__':
    app.run()
