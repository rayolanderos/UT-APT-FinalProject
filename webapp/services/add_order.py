import webapp2
import json
import logging
from google.appengine.ext import ndb
from google.appengine.api import search
from models.order import Order
from models.order_beer import OrderBeer
from models.employee import Employee
from models.user import User
from models.beer import Beer

from twilio.rest import Client

class AddOrder(webapp2.RequestHandler):

	def text_order(self, order_beers, user_id):
		account_sid = ""
		auth_token = ""
		client = Client(account_sid, auth_token)
		
		customers = User.query(User.fb_user_id == user_id).fetch()
		for customer in customers:
			customer_name = customer.name
		beer_list = ""

		for beer in order_beers:
			beer_name = Beer.get_by_id(int(beer['id'])).name
			beer_quantity = beer['quantity']
			beer = '\n{} x{}'.format(beer_name, beer_quantity)
			beer_list += beer;

		msg_body = 'Beer Order for {}! {}'.format(customer_name, beer_list)		

		bartending_employees = Employee.query(Employee.bartending == True).fetch()

		for employee in bartending_employees:
			msg_to = "+1"+employee.phone
			
			rv = client.messages.create(to=msg_to, from_="+15126400776 ",body=msg_body)
			# self.response.write(str(rv))

	def post(self):

		json_string = self.request.body
		dict_object = json.loads(json_string)

		order_fb_user_id = dict_object['userId']
		order_order_total = float ( dict_object['total'] )
		#order_status = dict_object['status']
		order_reward_id = int( dict_object['rewardId'] )
		order_discount = float( dict_object['discount'] )
		order_invoice_number =  dict_object['invoice']
		order_beers = dict_object['beers']

		same_invoice_number = Order.query(Order.invoice_number == order_invoice_number).fetch()
		
		if not same_invoice_number:

			order = Order(
				fb_user_id = order_fb_user_id, 
			    order_total =  order_order_total, 
			    #status = order_status, 
			    reward_id = order_reward_id, 
			    discount = order_discount, 
			    invoice_number = order_invoice_number
			)
			order_key = order.put()
			order_id = order_key.id()

			for order_beer in order_beers:
				order_beer = OrderBeer(
					order_id = int( order_id ),
					beer_id = int (order_beer['id'] ),
					quantity = int (order_beer['quantity'] )
				)
				order_beer_key = order_beer.put()
				order_beer_id = str(order_beer_key.id())

			self.text_order(order_beers, order_fb_user_id)
			self.response.set_status(200)

		else:
			self.response.set_status(500)

		
        
