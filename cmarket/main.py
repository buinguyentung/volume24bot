import json
import requests
import os 
import time
from google.cloud import firestore

os.environ["GOOGLE_APPLICATION_CREDENTIALS"]="/home/hodinhtan/volume24bot/cmarket/keyfilecloud.json"
#/home/tanho/keyfile.json
# "/home/tanho/coinpy/volume24bot/cmarket/keyfilecloud.json
#/home/tanho/github/py/volume24bot/cmarket
client = firestore.Client()

lt = {'bitcoin','ethereum','ripple','litecoin','dogecoin'}
  
def urlCoin(name):
  return requests.get('https://api.coinmarketcap.com/v1/ticker/'+name)
import datetime
def main():
    while 1:
        for name in lt:
            req = urlCoin(name)
            data = req.json()
            print(data[0])
            add_db = client.collection(name).document(unicode(datetime.datetime.now())).set(data[0])
        sleep(43200) #need to implement usd time 10AM 
if __name__ == "__main__":
  main()

