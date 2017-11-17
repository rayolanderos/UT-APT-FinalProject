import webapp2
import json
import logging
from google.appengine.ext import ndb
from google.appengine.api import search
from models.order import Order

class GetOrder(webapp2.RequestHandler):

    def get(self):

		order_id = int( self.request.get('orderId', 0) )

		order = Order.get_by_id(order_id)

		if order != None:
			date = order.timestamp
			date_string = date.strftime('%m/%d/%Y %H:%M:%S')
			order_data = { 
				'id': order.key.id(), 
		    	'order_total': order.order_total, 
		    	'user_id': order.user_id, 
		    	'status': order.status, 
		    	'reward_id': order.reward_id, 
		    	'discount': order.discount, 
		    	'invoice_number': order.invoice_number, 
		    	'timestamp' : date_string
			}
		else:
			order_data = {}

		self.response.out.write(json.dumps(order_data))
		


        
