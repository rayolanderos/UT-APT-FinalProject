import webapp2
import json
import logging
from google.appengine.ext import ndb
from google.appengine.api import search
from models.order import Order

class AddOrder(webapp2.RequestHandler):

    def post(self):

		json_string = self.request.body
		dict_object = json.loads(json_string)

		order_user_id = dict_object['orderUserId']
		order_order_total = dict_object['orderOrderTotal']
		order_status = dict_object['orderStatus']
		order_reward_id = dict_object['orderRewardId']
		order_discount = dict_object['orderDiscount']
		order_invoice_number = dict_object['orderInvoiceNumber']

		same_invoice_number = Order.query(Order.invoice_number == order_invoice_number).fetch()
		
		if not same_invoice_number:

			order = Order(
				user_id = order_user_id, 
			    order_total =  order_order_total, 
			    status = order_status, 
			    reward_id = order_reward_id, 
			    discount = order_discount, 
			    invoice_number = order_invoice_number
			)
			order_key = beer.put()
			order_id = str(order_key.id())

			res = { "msg" : "Order successfully placed", "success": True, "order_id" : order_id }
			self.response.out.write(json.dumps(res))

		else:
			res = { "msg" : "That order already exists in the system or something went wrong. Please try again.", "success": False }
			self.response.out.write(json.dumps(res))

		
        
