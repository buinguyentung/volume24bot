import json
import requests
import os 
import time
from google.cloud import firestore
from oauth2client import file, client, tools
from httplib2 import Http
from googleapiclient.discovery import build

os.environ["GOOGLE_APPLICATION_CREDENTIALS"]="/home/hodinhtan/volume24bot/CoinMarketCap/credentials.json"
#/home/tanho/keyfile.json
# "/home/tanho/coinpy/volume24bot/cmarket/keyfilecloud.json
#/home/tanho/github/py/volume24bot/cmarket

#service = build('drive', 'v3', http=creds.authorize(Http()))
SCOPES = 'https://www.googleapis.com/auth/drive.metadata.readonly'

store = file.Storage('token.json')
creds = store.get()
if not creds or creds.invalid:
    flow = client.flow_from_clientsecrets('credentials.json', SCOPES)
    creds = tools.run_flow(flow, store)
    
service = build('drive', 'v3', http=creds.authorize(Http()))

results = service.files().list(pageSize=10, fields="nextPageToken, files(id,name)").execute()

items = results.get('files', [])


print 'conneced '
if not items:
    print "nofile found"
else:
    print 'files:'
    for item in items:
        print(u'{0} ({1})'.format(item['name'], item['id']))
