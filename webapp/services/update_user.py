import webapp2
import json
import logging
from google.appengine.ext import ndb
from google.appengine.api import search
from models.user import User

class UpdateUser(webapp2.RequestHandler):

    def post(self):

		json_string = self.request.body
		dict_object = json.loads(json_string)

		user_id = int( dict_object['userId'] ) 
		user_name = dict_object['userName']
		user_fb_user_id = dict_object['userFbUserId']
		user_email = dict_object['userEmail']
		user_date_of_birth = dict_object['userDateOfBirth']

		user = User.get_by_id(user_id)

		if user != None:
			user.name = user_name
			user.email = user_email
			user.fb_user_id = user_fb_user_id
			user.date_of_birth = user_date_of_birth

			user.put()

			res = { "msg" : "User successfully updated", "success": True, "user_id" : user_id }
			self.response.out.write(json.dumps(res))
		else:
			res = { "msg" : "Oops! Something went wrong. Please try again.", "success": False, "user_id" : user_id }
			self.response.out.write(json.dumps(res))