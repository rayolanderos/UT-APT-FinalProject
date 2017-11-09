import webapp2
import jinja2
import json
import os
import logging

from google.appengine.api import urlfetch

templates_dir = os.path.normpath(os.path.dirname(__file__) + '/../www/')

JINJA_ENVIRONMENT = jinja2.Environment(
    loader=jinja2.FileSystemLoader(templates_dir),
    extensions=['jinja2.ext.autoescape'],
    autoescape=True)

class TapList(webapp2.RequestHandler):

    def get_beers(self):
        beers = {}

    def get(self):

        beers =  ''

        page_data = {'beers': beers, 'page_name': 'tap_list'}
        template = JINJA_ENVIRONMENT.get_template('tap-list.html')
        self.response.write(template.render(page_data))

    def post(self):

        action = self.request.get('action')

        beer_name = self.request.get('beerName', '')
        beer_style = self.request.get('beerStyle', 'Generic')
        beer_price = self.request.get('beerPrice', 0.0)
        beer_abv = self.request.get('beerAbv', 0.0)
        beer_ibus = self.request.get('beerIbus', 0)
        beer_srm = self.request.get('beerSrm', 0)
        beer_on_tap = self.request.get('beerOnTap', False)
        beer_tap_list_image = self.request.get('beerTapListImage', '')
        beer_description = self.request.get('beerDescription', 'Empty description')
        beer_description_image = self.request.get('beerDescriptionImage', '')
        beer_data = { 
            "beerName" : beer_name, 
            "beerStyle" : beer_style,
            "beerTapListImage" : beer_tap_list_image,
            "beerDescription" : beer_description,
            "beerDescriptionImage" : beer_description_image,
            "beerPrice" : beer_price,
            "beerAbv" : beer_abv,
            "beerIbus" : beer_ibus,
            "beerSrm" : beer_srm,
            "beerOnTap" : beer_on_tap 
        }

        logging.info('********* beer data ***********')
        logging.info(beer_data)

        if(action == "addBeer"):
            api_uri = self.uri_for('add-beer', _full=True)
        else:
            api_uri = self.uri_for('add-beer', _full=True)

        result = urlfetch.fetch(
            url=api_uri,
            payload=json.dumps(beer_data),
            method=urlfetch.POST,
            headers= {'Content-Type': 'application/json'}
        )

        if result.status_code == 200:
            response = json.loads(result.content)
            
            if response['success'] :
                msg_type = "success"
            else:
                msg_type = "error"
            
            msg = response['msg']

            beers = self.get_beers()

            page_data = {'beers': beers, 'page_name': 'tap_list', 'msg_type': msg_type, 'msg': msg}
            template = JINJA_ENVIRONMENT.get_template('tap-list.html')
            self.response.write(template.render(page_data))


        


