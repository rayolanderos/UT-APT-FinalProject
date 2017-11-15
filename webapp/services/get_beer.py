import webapp2
import json
import logging
from google.appengine.ext import ndb
from google.appengine.api import search
from models.beer import Beer

class GetBeer(webapp2.RequestHandler):

    def get(self):

		beer_id = int( self.request.get('beerId', 0) )

		beer = Beer.get_by_id(beer_id)

		if beer != None:
			date = beer.creation_date
			date_string = date.strftime('%m/%d/%Y')
			beer_data = { 
				  'id': beer.key.id(), 
		    	'name': beer.name, 
		    	'style': beer.style, 
		    	'tap_list_image': beer.tap_list_image, 
		    	'description': beer.description,
		    	'description_image' : beer.description_image, 
		    	'price' : beer.price,  
		    	'abv' : beer.abv, 
		    	'ibus' : beer.ibus, 
		    	'srm' : beer.srm, 
		    	'on_tap' : beer.on_tap, 
		    	'review' : beer.review, 
		    	'review_count' : beer.review_count, 
		    	'creation_date' : date_string
			}
		else:
			beer_data = {}

		self.response.out.write(json.dumps(beer_data))
		


        
