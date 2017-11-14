import webapp2
import json
import logging
from google.appengine.ext import ndb
from google.appengine.api import search
from models.beer import Beer

class UpdateBeer(webapp2.RequestHandler):

    def post(self):

		json_string = self.request.body
		dict_object = json.loads(json_string)

		logging.info('********* dict object ***********')
		logging.info(dict_object)

		beer_id = int( dict_object['beerId'] ) 
		beer_name = dict_object['beerName']
		beer_style = dict_object['beerStyle']
		beer_price = float( dict_object['beerPrice'] )
		beer_abv = float( dict_object['beerAbv'] )
		beer_ibus = int( dict_object['beerIbus'] )
		beer_srm = int( dict_object['beerSrm'] ) 
		beer_on_tap = bool( dict_object['beerOnTap'] )
		beer_tap_list_image = dict_object['beerTapListImage']
		beer_description = dict_object['beerDescription']
		beer_description_image = dict_object['beerDescriptionImage']

		beer = Beer.get_by_id(beer_id)

		if beer != None:
			beer.name = beer_name
			beer.style = beer_style
			beer.price = beer_price
			beer.abv = beer_abv
			beer.ibus = beer_ibus
			beer.srm = beer_srm
			beer.on_tap = beer_on_tap
			beer.tap_list_image = beer_tap_list_image
			beer.description = beer_description
			beer.description_image = beer_description_image

			beer.put()

			res = { "msg" : "Beer successfully added", "success": True, "beer_id" : beer_id }
			self.response.out.write(json.dumps(res))
		else:
			res = { "msg" : "Oops! Something went wrong. Please try again.", "success": False, "beer_id" : beer_id }
			self.response.out.write(json.dumps(res))