import logging

from google.appengine.ext import ndb

class User(ndb.Model):
    
    payment_key = ndb.StringProperty()
    name = ndb.StringProperty()
    email = ndb.StringProperty()
    date_of_birth = ndb.DateTimeProperty(auto_now_add=False)
    creation_date = ndb.DateTimeProperty(auto_now_add=True)




