import os

import jinja2
import webapp2

from controllers import tap_list

from services import add_beer
from services import delete_beer
from services import update_beer
from services import get_beer
from services import get_all_beers


templates_dir = os.path.normpath(os.path.dirname(__file__) + '/www/')

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(templates_dir),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

class MainPage(webapp2.RequestHandler):

    def get(self):
        
        self.redirect('/tap_list')


app = webapp2.WSGIApplication([
    webapp2.Route('/', MainPage, name='home'),
    webapp2.Route('/tap_list', tap_list.TapList, name='tap-list'),
    webapp2.Route('/api/add_beer', add_beer.AddBeer, name='add-beer'),
    webapp2.Route('/api/delete_beer', delete_beer.DeleteBeer, name='delete-beer'),
    webapp2.Route('/api/update_beer', update_beer.UpdateBeer, name='update-beer'),
    webapp2.Route('/api/get_all_beers', get_all_beers.GetAllBeers, name='get-all-beers'),
    webapp2.Route('/api/get_beer', get_beer.GetBeer, name='get-beer')

], debug=True)