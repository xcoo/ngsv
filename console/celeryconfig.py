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

# RabbitMQ settings
BROKER_URL = 'amqp://guest@localhost//'
CELERY_RESULT_BACKEND = 'amqp'
CELERY_TASK_RESULT_EXPIRES = 18000

# MySQL settings
# NOTE: not working
#BROKER_URL = 'sqla+mysql://root:root@localhost/ngsv_celery_broker'
#CELERY_RESULT_BACKEND = 'database'
#CELERY_RESULT_DBURI = 'mysql://root:root@localhost/ngsv_celery_backend'
#CELERY_RESULT_ENGINE_OPTIONS = { 'echo': True }
