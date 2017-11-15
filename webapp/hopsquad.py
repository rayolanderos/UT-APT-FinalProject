import os

import jinja2
import webapp2

from controllers import tap_list
from controllers import employees
from controllers import photos

from services import add_beer
from services import delete_beer
from services import update_beer
from services import get_beer
from services import get_all_beers
from services import add_employee
from services import delete_employee
from services import update_employee
from services import get_all_employees


templates_dir = os.path.normpath(os.path.dirname(__file__) + '/www/')

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(templates_dir),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

class MainPage(webapp2.RequestHandler):

    def get(self):
        
        self.redirect('/tap_list')


app = webapp2.WSGIApplication([
    webapp2.Route('/', MainPage, name='home'),
    webapp2.Route('/tap_list', tap_list.TapList, name='tap-list'),
    webapp2.Route('/api/add_beer', add_beer.AddBeer, name='add-beer'),
    webapp2.Route('/api/delete_beer', delete_beer.DeleteBeer, name='delete-beer'),
    webapp2.Route('/api/update_beer', update_beer.UpdateBeer, name='update-beer'),
    webapp2.Route('/api/get_all_beers', get_all_beers.GetAllBeers, name='get-all-beers'),
    webapp2.Route('/api/get_beer', get_beer.GetBeer, name='get-beer'),
    webapp2.Route('/upload_photo', photos.PhotoUploadHandler, name='upload-photo'),
    webapp2.Route('/generate_upload_url', photos.GenerateUploadUrlHandler, name='generate-upload-url'), 
    webapp2.Route('/employees', employees.Employees, name='employees'),
    webapp2.Route('/api/add_employee', add_employee.AddEmployee, name='add-employee'),
    webapp2.Route('/api/delete_employee', delete_employee.DeleteEmployee, name='delete-employee'),
    webapp2.Route('/api/update_employee', update_employee.UpdateEmployee, name='update-employee'),
    webapp2.Route('/api/get_all_employees', get_all_employees.GetAllEmployees, name='get-all-employees')

], debug=True)