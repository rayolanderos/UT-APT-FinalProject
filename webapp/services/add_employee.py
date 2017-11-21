import webapp2
import json
import logging
from google.appengine.ext import ndb
from google.appengine.api import search
from models.employee import Employee

class AddEmployee(webapp2.RequestHandler):

    def post(self):

		json_string = self.request.body
		dict_object = json.loads(json_string)

		employee_name = dict_object['employeeName']
		employee_phone = dict_object['employeePhone']
		employee_carrier = dict_object['employeeCarrier']
		employee_bartending = bool( dict_object['employeeBartending'] )
		

		same_name = Employee.query(Employee.name == employee_name).fetch()
		
		if not same_name:

			#NDB storing
			employee = Employee(
				name = employee_name, 
				phone = employee_phone,
			    carrier = employee_carrier,
			    bartending = employee_bartending
			)
			employee_key = employee.put()
			employee_id = str(employee_key.id())

			res = { "msg" : "Employee successfully added", "success": True, "employee_id" : employee_id }
			self.response.out.write(json.dumps(res))

		else:
			res = { "msg" : "That employee already exists in the inventory or something went wrong. Please try again.", "success": False }
			self.response.out.write(json.dumps(res))
        
