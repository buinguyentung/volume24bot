import json
import requests
import os 
import time
from google.cloud import firestore

os.environ["GOOGLE_APPLICATION_CREDENTIALS"]="/home/hodinhtan/volume24bot/CoinMarketCap/keyfilecloud.json"
#/home/tanho/keyfile.json
# "/home/tanho/coinpy/volume24bot/cmarket/keyfilecloud.json
#/home/tanho/github/py/volume24bot/cmarket
client = firestore.Client()

lt = {'bitcoin','ethereum','ripple','litecoin','dogecoin'}
  
def urlCoin(name):
  return requests.get('https://api.coinmarketcap.com/v1/ticker/'+name)

import datetime, schedule

def sampling_data():
    for name in lt:
        req = urlCoin(name)
        data = req.json()
        date_time = unicode(datetime.datetime.now().date())
        updatedtime = unicode(datetime.datetime.now())
        data[0]["datetime"] = date_time
        data[0]["updatedtime"] = updatedtime
        data[0]["volume_usd"] = data[0]["24h_volume_usd"]
        add_db = client.collection(name).document(date_time).set(data[0])

# schedule.every().day.at("10:00").do(sampling_data,'It is 10:00')
def main():
    while True:
        # schedule.run_pending()
	sampling_data()
	time.sleep(10000)

if __name__ == "__main__":
  main()

