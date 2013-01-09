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

from __future__ import absolute_import

from celery import current_task
from celery.decorators import task

from task_server.celery import celery

import tools.load_bam
import tools.calc_pileup
import tools.load_bed

from tools.sam.data.sql import SQLDB

from config import Config

# Load a bam file and calculate histograms.
@task(name='tasks.load_bam')
def load_bam(bam_file, conf):
    current_task.update_state(state='STARTED')
    db = SQLDB(conf.db_name, conf.db_host, conf.db_user, conf.db_password)

    tools.load_bam.load(bam_file, db)
    current_task.update_state(state='BAM_FINISH')

    tools.calc_pileup.run(bam_file, db)

# Load a bed file.
@task(name='tasks.load_bed')
def load_bed(bed_file, conf):
    current_task.update_state(state='STARTED')
    db = SQLDB(conf.db_name, conf.db_host, conf.db_user, conf.db_password)
    tools.load_bed.load(bed_file, db)