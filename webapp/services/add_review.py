import webapp2
import json
import logging

from datetime import datetime

from google.appengine.ext import ndb
from google.appengine.api import search
from models.beer import Beer

class AddReview(webapp2.RequestHandler):

    def post(self):

		json_string = self.request.body
		dict_object = json.loads(json_string)

		review = int( dict_object['review'] )
		beer_id = int( dict_object['beer_id'] )
		

		beer = Beer.get_by_id(beer_id)
		
		if beer != None:
			beer.review_sum = beer.review_sum + review
			beer.review_count = beer.review_count + 1
			beer.review = beer.review_sum/beer.review_count
			
			beer.put()
			
			self.response.set_status(200)

		else:
			self.response.set_status(500)

		
        
