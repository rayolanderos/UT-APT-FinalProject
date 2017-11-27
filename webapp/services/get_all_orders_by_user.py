import webapp2
import json
import logging
from google.appengine.ext import ndb
from google.appengine.api import search
from models.order import Order
from models.order_beer import OrderBeer
from models.beer import Beer

class GetAllOrdersByUser(webapp2.RequestHandler):

    def get(self):

		self.response.headers['Content-Type'] = 'application/json'

        user_id = self.request.get('userId', 0)
        limit = int(self.request.get('limit', 3))

		beer = Beer.get_by_id(beer_id)


		order_query = Order.query(Order.fb_user_id == user_id).order(Order.timestamp)
		orders = order_query.fetch(limit)
		order_list = []

        for order in orders:
			date = order.timestamp
			date_string = date.strftime('%m/%d/%Y %H:%M:%S')

			order_beer_query = OrderBeer.query( OrderBeer.order_id == order.key.id() ).order(-OrderBeer.quantity)
			order_beers = order_beer_query.fetch()
			order_beer_list = []
			
			for order_beer in order_beers:

				beer = Beer.get_by_id(order_beer.beer_id)
				
				if beer != None:
					beer_name = beer.name 
				else:
					beer_name = "Unknown beer id " + beer_id
				
				order_beer_list.append({
					'beer_id' : order_beer.beer_id,
					'beer_name' : beer_name,
					'quantity' : order_beer.quantity
				})

			order_list.append({
		    	'id': order.key.id(), 
		    	'order_total': order.order_total, 
		    	'user_id': order.user_id, 
		    	'status': order.status, 
		    	'reward_id': order.reward_id, 
		    	'discount': order.discount, 
		    	'invoice_number': order.invoice_number, 
		    	'timestamp' : date_string, 
		    	'details' : order_beer_list
		    })

		self.response.out.write(json.dumps(order_list))
