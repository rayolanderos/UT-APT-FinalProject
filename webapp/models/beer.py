import logging

from google.appengine.ext import ndb

class Beer(ndb.Model):
    
    name = ndb.StringProperty()
    style = ndb.StringProperty()
    tap_list_image = ndb.StringProperty()
    description = ndb.StringProperty()
    description_image = ndb.StringProperty()
    price = ndb.FloatProperty()
    abv = ndb.FloatProperty()
    ibus = ndb.IntegerProperty()
    srm = ndb.IntegerProperty()
    on_tap = ndb.BooleanProperty()
    review = ndb.FloatProperty()
    review_count = ndb.IntegerProperty(default=0)
    creation_date = ndb.DateTimeProperty(auto_now_add=True)

    def get_name(self):
        name = self.name
        return name



