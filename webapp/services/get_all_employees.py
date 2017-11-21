import webapp2
import json
import logging
from google.appengine.ext import ndb
from google.appengine.api import search
from models.employee import Employee

class GetAllEmployees(webapp2.RequestHandler):

    def get(self):

		self.response.headers['Content-Type'] = 'application/json'
		employee_query = Employee.query().order(Employee.name)
		employees = employee_query.fetch()
		employee_list = []

		for employee in employees:
			date = employee.creation_date
			date_string = date.strftime('%m/%d/%Y')
			employee_list.append({
		    	'id': employee.key.id(), 
		    	'name': employee.name, 
		    	'phone': employee.phone, 
		    	'carrier': employee.carrier, 
		    	'bartending': employee.bartending,
		    	'creation_date' : date_string
		    })

		self.response.out.write(json.dumps(employee_list))
        
