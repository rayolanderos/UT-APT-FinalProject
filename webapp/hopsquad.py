import os

import jinja2
import webapp2

from google.appengine.api import users

from controllers import tap_list
from controllers import employees
from controllers import orders
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
from services import add_order
from services import get_order
from services import get_all_orders
from services import get_all_orders_by_user
from services import add_user
from services import update_user
from services import get_user
from services import process_payment


templates_dir = os.path.normpath(os.path.dirname(__file__) + '/www/')

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(templates_dir),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

class MainPage(webapp2.RequestHandler):

    def get(self):
        user = users.get_current_user()

        if user:
            logout_url = users.create_logout_url('/')

            self.redirect('/tap_list')
        else:
            login_url = users.create_login_url('/')

            template_values = {
                'login_url': login_url
            }

            template = JINJA_ENVIRONMENT.get_template('login.html')
            self.response.write(template.render(template_values))
        

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
    webapp2.Route('/api/get_all_employees', get_all_employees.GetAllEmployees, name='get-all-employees'),
    webapp2.Route('/orders', orders.Orders, name='orders'),
    webapp2.Route('/api/add_order', add_order.AddOrder, name='add-order'),
    webapp2.Route('/api/get_order', get_order.GetOrder, name='get-order'),
    webapp2.Route('/api/get_all_orders', get_all_orders.GetAllOrders, name='get-all-orders'),
    webapp2.Route('/api/get_all_orders_by_user', get_all_orders_by_user.GetAllOrdersByUser, name='get-all-orders-by-user'),
    webapp2.Route('/api/add_user', add_user.AddUser, name='add-user'),
    webapp2.Route('/api/update_user', update_user.UpdateUser, name='update-user'),
    webapp2.Route('/api/get_user', get_user.GetUser, name='get-user'),
    webapp2.Route('/api/process_payment', process_payment.ProcessPayment, name='process-payment')

], debug=True)