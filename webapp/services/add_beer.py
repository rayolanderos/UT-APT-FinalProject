import webapp2
import json
import logging
from google.appengine.ext import ndb
from google.appengine.api import search
from models.beer import Beer

class AddBeer(webapp2.RequestHandler):

    def post(self):

		json_string = self.request.body
		dict_object = json.loads(json_string)

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

		same_name = Beer.query(Beer.name == beer_name).fetch()
		
		if not same_name:

			#NDB storing
			beer = Beer(
				name = beer_name, 
				style = beer_style,
			    tap_list_image = beer_tap_list_image,
			    description = beer_description,
			    description_image = beer_description_image,
			    price = beer_price,
			    abv = beer_abv,
			    ibus = beer_ibus,
			    srm = beer_srm,
			    on_tap = beer_on_tap
			)
			beer_key = beer.put()
			beer_id = str(beer_key.id())

			res = { "msg" : "Beer successfully added", "success": True, "beer_id" : beer_id }
			self.response.out.write(json.dumps(res))

		else:
			res = { "msg" : "That beer already exists in the inventory or something went wrong. Please try again.", "success": False }
			self.response.out.write(json.dumps(res))
        
