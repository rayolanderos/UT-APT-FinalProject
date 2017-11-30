import webapp2
import json
import logging
from google.appengine.ext import ndb
from google.appengine.api import search


# installing stripe library - "pip install --upgrade stripe" in terminal

class ProcessPayment(webapp2.RequestHandler):

    def post(self):

        json_string = self.request.body
        dict_object = json.loads(json_string)

        total_price = float( dict_object['totalPrice'] )
        token = token( dict_object['paymentToken'] )

        # Set your secret key: remember to change this to your live secret key in production
        # See your keys here: https://dashboard.stripe.com/account/apikeys
        stripe.api_key = "sk_test_BQokikJOvBiI2HlWgH4olfQ2" #ToDO change key to actual secret key

        # Charge the user's card:
        charge = stripe.Charge.create(
            amount=total_price,
            currency="usd",
            description="Hop Squad Purchase",
            source=token,
        ) # if creation succeeds, charge made.  throws an error if charge fails
