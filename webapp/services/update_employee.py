import webapp2
import json
import logging
from google.appengine.ext import ndb
from google.appengine.api import search
from models.employee import Employee

class UpdateEmployee(webapp2.RequestHandler):

    def post(self):

		json_string = self.request.body
		dict_object = json.loads(json_string)

		employee_id = int( dict_object['employeeId'] ) 
		employee_name = dict_object['employeeName']
		employee_phone = dict_object['employeePhone']
		employee_carrier = dict_object['employeeCarrier']
		employee_bartending = bool( dict_object['employeeBartending'] )

		employee = Employee.get_by_id(employee_id)

		if employee != None:
			employee.name = employee_name
			employee.phone = employee_phone
			employee.carrier = employee_carrier
			employee.bartending = employee_bartending

			employee.put()

			res = { "msg" : "Employee successfully updated", "success": True, "employee_id" : employee_id }
			self.response.out.write(json.dumps(res))
		else:
			res = { "msg" : "Oops! Something went wrong. Please try again.", "success": False, "employee_id" : employee_id }
			self.response.out.write(json.dumps(res))