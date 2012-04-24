# -*- coding: utf-8 -*-

from sql import *

import json


class Chromosome:

	def __init__(self, db):
		self.db = db

	def get_by_name(self, name):

		sql = u"SELECT chrId, chromosome from chromosome where chromosome = '%s'" % name

		result = self.db.execute(sql)

		if len(result) > 0:
			return {'id': result[0][0], 'name': result[0][1]}
		else:
			return None


	def get_all(self):

		sql = u"SELECT chrId, chromosome FROM chromosome"

		result = self.db.execute(sql)

		if len(result) > 0:
			return result
		else:
			return None


	def append(self, name):

		if self.get_by_name(name) is None:
			sql = u"INSERT INTO chromosome (chromosome) VALUES ('%s')" % name
			self.db.execute(sql)
