import logging

from google.appengine.ext import ndb

class OrderBeer(ndb.Model):
    
    order_id = ndb.IntegerProperty()
    beer_id = ndb.IntegerProperty()
    quantity = ndb.IntegerProperty()
    timestamp = ndb.DateTimeProperty(auto_now_add=True)




