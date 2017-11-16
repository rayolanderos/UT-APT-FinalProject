import logging

from google.appengine.ext import ndb

class Order(ndb.Model):
    
    user_id = ndb.IntegerProperty()
    order_total = ndb.FloatProperty()
    invoice_number = ndb.StringProperty()
    status = ndb.IntegerProperty()
    discount = ndb.FloatProperty()
    reward_id = ndb.IntegerProperty()
    timestamp = ndb.DateTimeProperty(auto_now_add=True)




