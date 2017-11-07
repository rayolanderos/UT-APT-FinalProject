import webapp2
import jinja2
import json
import os

from google.appengine.api import urlfetch

templates_dir = os.path.normpath(os.path.dirname(__file__) + '/../www/')

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(templates_dir),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

class TapList(webapp2.RequestHandler):

    def get(self):

        beers =  ''

        page_data = {'beers': beers, 'page_name': 'tap_list'}
        template = JINJA_ENVIRONMENT.get_template('tap-list.html')
        self.response.write(template.render(page_data))
