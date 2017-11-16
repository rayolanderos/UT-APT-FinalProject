import webapp2
import json
import logging
from google.appengine.ext import ndb
from google.appengine.api import search
from models.order import Order

class GetAllOrders(webapp2.RequestHandler):

    def get(self):

		self.response.headers['Content-Type'] = 'application/json'
		order_query = Order.query().order(Order.timestamp)
		orders = order_query.fetch()
		order_list = []

		for order in orders:
			date = order.timestamp
			date_string = date.strftime('%m/%d/%Y %H:%M:%S')
			order_list.append({
		    	'id': order.key.id(), 
		    	'order_total': order.order_total, 
		    	'user_id': order.user_id, 
		    	'status': order.status, 
		    	'reward_id': order.reward_id, 
		    	'discount': order.discount, 
		    	'invoice_number': order.invoice_number, 
		    	'timestamp' : date_string
		    })

		self.response.out.write(json.dumps(order_list))
        
