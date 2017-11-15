import webapp2
import json
import logging
from google.appengine.ext import ndb
from google.appengine.api import search
from models.employee import Employee

class DeleteEmployee(webapp2.RequestHandler):

    def post(self):
    	
		json_string = self.request.body
		dict_object = json.loads(json_string)

		employee_id = int( dict_object['employeeId'] )

		employee = Employee.get_by_id(employee_id)

		if employee != None:
			employee.key.delete()
			res = { "msg" : "Employee successfully deleted", "success": True, "employee_id" : employee_id }
			self.response.out.write(json.dumps(res))
		else:
			res = { "msg" : "Oops! Something went wrong. Please try again.", "success": False, "employee_id" : employee_id }
			self.response.out.write(json.dumps(res))
