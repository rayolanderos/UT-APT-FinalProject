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

class Orders(webapp2.RequestHandler):

    def get_orders(self):
        orders = {}
        result = urlfetch.fetch(self.uri_for('get-all-orders', _full=True))
        if result.status_code == 200:
            orders = json.loads(result.content)
        return orders

    def get(self):

        orders =  self.get_orders()

        page_data = {'orders': orders, 'page_name': 'orders', 'has_msg': False,}
        template = JINJA_ENVIRONMENT.get_template('orders.html')
        self.response.write(template.render(page_data))