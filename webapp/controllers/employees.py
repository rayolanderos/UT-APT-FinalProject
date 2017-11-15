import webapp2
import jinja2
import json
import os
import logging

from google.appengine.api import urlfetch

templates_dir = os.path.normpath(os.path.dirname(__file__) + '/../www/')

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(templates_dir),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

class Employees(webapp2.RequestHandler):

    def get_employees(self):
        employees = {}
        result = urlfetch.fetch(self.uri_for('get-all-employees', _full=True))
        if result.status_code == 200:
            employees = json.loads(result.content)
        return employees

    def get(self):

        employees =  self.get_employees()

        page_data = {'employees': employees, 'page_name': 'employees', 'has_msg': False,}
        template = JINJA_ENVIRONMENT.get_template('employees.html')
        self.response.write(template.render(page_data))

    def post(self):

        action = self.request.get('action')

        employee_id = self.request.get('employeeId', '')
        employee_name = self.request.get('employeeName', '')
        employee_phone = self.request.get('employeePhone', '')
        employee_carrier = self.request.get('employeeCarrier', '')
        employee_bartending = self.request.get('employeeBartending', False)
        employee_data = { 
            "employeeId" : employee_id, 
            "employeeName" : employee_name, 
            "employeePhone" : employee_phone,
            "employeeCarrier" : employee_carrier,
            "employeeBartending" : employee_bartending
        }

        if(action == "addEmployee"):
            api_uri = self.uri_for('add-employee', _full=True)
        elif (action == "updateEmployee"):
            api_uri = self.uri_for('update-employee', _full=True)
            logging.info("++++++++++++ updating employee_data +++++++++++++++");
            logging.info(employee_data);
        elif (action == "deleteEmployee"):
            api_uri = self.uri_for('delete-employee', _full=True)
            logging.info("++++++++++++ deleting beer_data +++++++++++++++");
            logging.info(employee_data);

        result = urlfetch.fetch(
            url=api_uri,
            payload=json.dumps(employee_data),
            method=urlfetch.POST,
            headers= {'Content-Type': 'application/json'}
        )

        if result.status_code == 200:
            response = json.loads(result.content)
            
            if response['success'] :
                msg_type = "success"
            else:
                msg_type = "error"
            
            msg = response['msg']

            employees = self.get_employees()

            page_data = {'employees': employees, 'page_name': 'tap_list', 'has_msg': True,'msg_type': msg_type, 'msg': msg}
            template = JINJA_ENVIRONMENT.get_template('employees.html')
            self.response.write(template.render(page_data))


        


