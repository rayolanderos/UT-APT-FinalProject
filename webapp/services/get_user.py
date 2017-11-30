import webapp2
import json
import logging
from google.appengine.ext import ndb
from google.appengine.api import search
from models.user import User

class GetUser(webapp2.RequestHandler):

    def get(self):

		user_id = int( self.request.get('user_id', 0) )

		user = User.get_by_id(user_id)

		if user != None:
			user_creation_date = user.creation_date
			user_date_of_birth = user.date_of_birth
			user_fb_user_id= user.fb_user_id
			user_creation_date_string = user_creation_date.strftime('%m/%d/%Y')
			user_date_of_birth_string = user_date_of_birth.strftime('%m/%d/%Y')
			user_data = { 
				'id': user.key.id(), 
		    	'name': user.name, 
		    	'email' : user.email,
		    	'fb_user_id' : user.fb_user_id,
		    	'payment_key' : user.payment_key,
		    	'date_of_birth' : user_date_of_birth_string,
		    	'creation_date' : user_creation_date_string
			}
		else:
			user_data = {}

		self.response.out.write(json.dumps(user_data))
		


        
