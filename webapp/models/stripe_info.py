import logging

from google.appengine.ext import ndb


class stripe_info(ndb.Model):
    totalPrice = ndb.FloatProperty()
    paymentToken = ndb.TokenProperty()