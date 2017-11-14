import webapp2
import json
import logging
from google.appengine.ext import ndb
from google.appengine.api import search
from models.beer import Beer

class DeleteBeer(webapp2.RequestHandler):

    def post(self):
    	
		json_string = self.request.body
		dict_object = json.loads(json_string)

		beer_id = int( dict_object['beerId'] )

		beer = Beer.get_by_id(beer_id)

		if beer != None:
			beer.key.delete()
			res = { "msg" : "Beer successfully deleted", "success": True, "beer_id" : beer_id }
			self.response.out.write(json.dumps(res))
		else:
			res = { "msg" : "Oops! Something went wrong. Please try again.", "success": False, "beer_id" : beer_id }
			self.response.out.write(json.dumps(res))
