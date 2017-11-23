import webapp2
import json
import logging

from datetime import datetime

from google.appengine.ext import ndb
from google.appengine.api import search
from models.user import User

class AddUser(webapp2.RequestHandler):

    def post(self):

		json_string = self.request.body
		dict_object = json.loads(json_string)

		user_name = dict_object['userName']
		user_payment_key = dict_object['userPaymentKey']
		user_email = dict_object['userEmail']
		user_date_of_birth = dict_object['userDateOfBirth']


		same_email = User.query(User.email == user_email).fetch()
		
		if not same_email:

			user = User(
				name = user_name, 
				payment_key = user_payment_key,
				email = user_email,
				date_of_birth = datetime.strptime(user_date_of_birth, '%Y-%m-%d %H:%M:%S')
			)
			user_key = user.put()
			user_id = str(user_key.id())

			res = { "msg" : "User successfully added", "success": True, "user_id" : user_id }
			self.response.out.write(json.dumps(res))

		else:
			res = { "msg" : "That user already exists in the system or something went wrong. Please try again.", "success": False }
			self.response.out.write(json.dumps(res))

		
        
