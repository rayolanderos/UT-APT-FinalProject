import logging

from google.appengine.ext import ndb

class Employee(ndb.Model):
    
    name = ndb.StringProperty()
    phone = ndb.StringProperty()
    carrier = ndb.StringProperty()
    bartending = ndb.BooleanProperty()
    creation_date = ndb.DateTimeProperty(auto_now_add=True)

    def get_name(self):
        name = self.name
        return name



