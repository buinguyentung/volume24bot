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

lt = {'bitcoin','ethereum','ripple','litecoin','dogecoin',
        "bitcoin-cash","eos","stellar","cardano","monero","tether"}
  
def urlCoin(name):
  return requests.get('https://api.coinmarketcap.com/v1/ticker/'+name)

import datetime, schedule

def sampling_data():
    for name in lt:
        req = urlCoin(name)
        data = req.json()
        updated_date = unicode(datetime.datetime.now().date())
        updated_time = unicode(datetime.datetime.now())
        data[0]["updated_date"] = updated_date
        data[0]["updated_time"] = updated_time
        data[0]["volume_usd"] = data[0]["24h_volume_usd"]
        add_db = client.collection(name).document(updated_date).set(data[0])

schedule.every().day.at("6:00").do(sampling_data)
schedule.every().day.at("14:00").do(sampling_data)
schedule.every().day.at("23:30").do(sampling_data)
#test
#schedule.every().day.at("16:20").do(sampling_data)
def main():
    while True:
        schedule.run_pending()
	#sampling_data()
	time.sleep(1)

if __name__ == "__main__":
  main()

