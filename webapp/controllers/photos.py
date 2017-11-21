import webapp2
import logging
import sys
import datetime
import random
import json

from google.appengine.ext import blobstore
from google.appengine.api import urlfetch
from google.appengine.api import images
from google.appengine.ext.webapp import blobstore_handlers

class PhotoUploadHandler(blobstore_handlers.BlobstoreUploadHandler):
    def post(self):
        try:
            uploads = self.get_uploads()

            if len(uploads) > 0:
                upload = uploads[0]
                url = images.get_serving_url(upload.key())
                
            else:
                url = ""
            
            self.response.headers['Content-Type'] = 'text/plain'
            self.response.out.write(url)

        except:
            e = sys.exc_info()[0]
            logging.exception(e)
            self.error(500)

class GenerateUploadUrlHandler(blobstore_handlers.BlobstoreUploadHandler):
    def get(self):
        self.response.headers['Content-Type'] = 'text/plain'
        self.response.out.write(blobstore.create_upload_url('/upload_photo'))