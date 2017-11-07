import webapp2
import json
import logging
from google.appengine.ext import ndb
from google.appengine.api import search
from models.beer import Beer

class UpdateBeer(webapp2.RequestHandler):

    def post(self):

        json_string = self.request.body
        
