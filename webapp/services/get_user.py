import webapp2
import json
import logging
from google.appengine.ext import ndb
from google.appengine.api import search
from models.user import User

class GetUser(webapp2.RequestHandler):

    def get(self):

		fb_user_id = self.request.get('fbUserId', 0)

		users = User.query(User.fb_user_id == fb_user_id).fetch(1)

		user = users[0]

		if user != None:
			user_creation_date = user.creation_date
			user_date_of_birth = user.date_of_birth
			user_creation_date_string = user_creation_date.strftime('%m/%d/%Y')
			user_date_of_birth_string = user_date_of_birth.strftime('%m/%d/%Y')
			user_data = { 
		    	'userName': user.name, 
		    	'userEmail' : user.email,
		    	'userFbUserID' : user.fb_user_id,
		    	'userPaymentKey' : user.payment_key,
		    	'userDateOfBirth' : user_date_of_birth_string
			}
		else:
			user_data = {}

		self.response.out.write(json.dumps(user_data))
		


        
