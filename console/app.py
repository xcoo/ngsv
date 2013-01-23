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

from flask import Flask
from flask import render_template, redirect, request
from werkzeug import SharedDataMiddleware
from werkzeug import secure_filename
from gevent import pywsgi
from geventwebsocket import WebSocketHandler, WebSocketError
from celery.result import BaseAsyncResult
from celery.task.control import inspect

from taskserver.tasks import load_bam, load_bed
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

tasks_info = []
ws_viewer_sockets = {}

def list_active_task():
    i = inspect()
    active = i.active()
    if active != None:
        for v in active.values():
            for t in v:
                r = BaseAsyncResult(t['id'])
                r.task_name = t['name']
                tasks_info.append({ 'result': r })

list_active_task()

@app.route('/')
def root():
    tasks = []
    
    for ti in reversed(tasks_info):
        r = ti['result']        
        
        if r.task_name == 'tasks.load_bam':
            task = {
                'task_id': r.id,
                'task_name': r.task_name,
                'bam_load_progress': 0
                }

            if 'file' in ti and ti['file'] != None:
                task['bam_file'] = ti['file']
            
            if r.status == 'PROGRESS':
                task['bam_load_progress'] =  str(r.result['progress']) + '%'
            if r.status == 'SUCCESS':
                task['bam_load_progress'] = '100%'
                if r.result['state'] == 'SUCCESS_WITH_ALERT':
                    task['alert'] = r.result['alert']
            if r.status == 'FAILURE':
                task['bam_load_progress'] = '100%'
                task['alert'] = 'FAILURE'

            tasks.append(task);
                
        if r.task_name == 'tasks.load_bed':
            task = {
                'task_id': r.id,
                'task_name': r.task_name,
                'bed_load_progress': 0
                }

            if 'file' in ti and ti['file'] != None:
                task['bed_file'] = ti['file']

            if r.status == 'PROGRESS':
                task['bed_load_progress'] =  str(r.result['progress']) + '%'
            if r.status == 'SUCCESS':
                task['bed_load_progress'] = '100%'
                if r.result['state'] == 'SUCCESS_WITH_ALERT':
                    task['alert'] = r.result['alert']
            if r.status == 'FAILURE':
                task['bed_load_progress'] = '100%'
                task['alert'] = 'FAILURE'

            tasks.append(task);
        
    return render_template('main.html', tasks=tasks)

@app.route('/viewer')
def viewer():
    return render_template('viewer.html')

@app.route('/help')
def help():
    return render_template('help.html')

@app.route('/api/upload-bam', methods=['POST'])
def upload_bam():
    f = request.files['file']
    if f and allowed_file(f.filename, ['bam']):
        filename = secure_filename(f.filename)
        bam_file = os.path.join(conf.upload_dir, filename)
        f.save(bam_file)

        r = load_bam.delay(bam_file, conf)
        tasks_info.append({'result': r, 'file': filename})

    return redirect('/')

@app.route('/api/upload-bed', methods=['POST'])
def upload_bed():
    f = request.files['file']
    if f and allowed_file(f.filename, ['bed']):
        filename = secure_filename(f.filename)
        bed_file = os.path.join(conf.upload_dir, filename) 
        f.save(bed_file)

        r = load_bed.delay(bed_file, conf)
        tasks_info.append({'result': r, 'file': filename})
        
    return redirect('/')

@app.route('/api/ws/connect')
def ws_connect():
    if request.environ.get('wsgi.websocket') is None:
        return redirect('/viewer')

    ip = request.remote_addr
    ws = request.environ['wsgi.websocket']
    ws_viewer_sockets[ip] = ws
    print 'Register: ', request.remote_addr

    while True:
        src = ws.receive()
        if src is None:
            break

    return redirect('/viewer')

@app.route('/api/ws/send-config')
def ws_send_config():
    if request.environ.get('wsgi.websocket') is None:
        return redirect('/viewer')

    ip = request.remote_addr
    ws = request.environ['wsgi.websocket']

    while True:
        src = ws.receive()
        print src
        if src is None:
            break
        if ip in ws_viewer_sockets:
            try:
                ws_viewer_sockets[ip].send(src)
            except WebSocketError:
                del ws_viewer_sockets[ip]

    return redirect('/viewer')

@app.route('/api/ws/echo')
def ws_echo():
    if request.environ.get('wsgi.websocket') is None:
        return redirect('/viewer')

    ws = request.environ['wsgi.websocket']

    while True:
        src = ws.receive()
        if src is None:
            break
        ws.send(src)

    return redirect('/viewer')

def allowed_file(filename, extensions):
    return '.' in filename and \
           filename.rsplit('.', 1)[1] in extensions

def run():
    if len(sys.argv) == 2 and sys.argv[1] == '--wsgi':
        print 'Debug: Disable\n', \
            'Testing: Disable\n', \
            'Websocket: Enable\n\n', \
            'Run "$ python app.py" if you want to use Debug/Testing mode\n'
        server = pywsgi.WSGIServer(('', 5000), app, handler_class=WebSocketHandler)
        server.serve_forever()        
    else:
        print 'Debug: Enable\n', \
            'Testing: Enable\n', \
            'Websocket: Disable\n\n', \
            'Run "$ python app.py --wsgi" if you want to use websocket\n'
        app.run(host='0.0.0.0')
    
if __name__ == '__main__':
    run()
