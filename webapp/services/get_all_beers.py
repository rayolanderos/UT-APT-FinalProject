import webapp2
import json
import logging
from google.appengine.ext import ndb
from google.appengine.api import search
from models.beer import Beer

class GetAllBeers(webapp2.RequestHandler):

    def get(self):

		self.response.headers['Content-Type'] = 'application/json'
		beer_query = Beer.query().order(Beer.name)
		beers = beer_query.fetch()
		beer_list = []

		for beer in beers:
			date = beer.creation_date
			date_string = date.strftime('%m/%d/%Y')
			beer_list.append({
		    	'id': beer.key.id(), 
		    	'key' : str(beer.key),
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
		    })
		    
		self.response.out.write(json.dumps(beer_list))
        
