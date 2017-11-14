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
			beer_data = { 
				"beerId" : beer.key.id(),
			    "beerName" : beer.name, 
			    "beerStyle" : beer.style,
			    "beerTapListImage" : beer.tap_list_image,
			    "beerDescription" : beer.description,
			    "beerDescriptionImage" : beer.description_image,
			    "beerPrice" : beer.price,
			    "beerAbv" : beer.abv,
			    "beerIbus" : beer.ibus,
			    "beerSrm" : beer.srm,
			    "beerOnTap" : beer.on_tap
			}
		else:
			beer_data = {}

		self.response.out.write(json.dumps(beer_data))
		


        
