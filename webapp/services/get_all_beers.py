import webapp2
import json
import logging
from google.appengine.ext import ndb
from google.appengine.api import search
from models.beer import Beer

class GetAllBeers(webapp2.RequestHandler):

    def get(self):
		logging.info("******** GetAllBeers ********")

		self.response.headers['Content-Type'] = 'application/json'
		beer_query = Beer.query().order(Beer.name)
		beers = beer_query.fetch()
		beer_list = []

		logging.info(beers)

		for beer in beers:
			date = beer.creation_date
			date_string = date.strftime('%m/%d/%Y')
			beer_list.append({
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
		    })

		logging.info("******** beer_list ********")
		logging.info(beer_list)
		self.response.out.write(json.dumps(beer_list))
        
