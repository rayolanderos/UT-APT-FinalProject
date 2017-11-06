import os
import json
import urllib

from google.appengine.api import users
from google.appengine.api import urlfetch

import jinja2
import webapp2

from controllers import tap_list

from services import viewTapList


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
    webapp2.Route('/api/get_tap_list', get_tap_list.getTapList, name='get_tap_list')

], debug=True)