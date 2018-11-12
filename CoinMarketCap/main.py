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

lt_coin = {'bitcoin','ethereum','ripple','litecoin','dogecoin',
        "bitcoin-cash","eos","stellar","cardano","monero","tether"}
lt_time = {'06:00','14:00','23:30'}

def urlCoin(name):
  return requests.get('https://api.coinmarketcap.com/v1/ticker/'+name)

import datetime, schedule

def sampling_data():
    for name in lt_coin:
        req = urlCoin(name)
        data = req.json()
        updated_date = unicode(datetime.datetime.now().date())
        updated_time = unicode(datetime.datetime.now())
        data[0]["updated_date"] = updated_date
        data[0]["updated_time"] = updated_time
        data[0]["volume_usd"] = data[0]["24h_volume_usd"]
        client.collection(name).document(updated_date).set(data[0])

import timedelta #timedelta(days=1)
def sumary_data():
    limit_date = datetime.datetiem.now().date() - timedelta(days=1)
    for name in lt_coin:
        docs = client.collection(name)
        for i in docs:
            if (docs["updated_date"] > limit_date)
                client.collection(name).document(docs["updated_date"]).
                set({"volume_usd":docs["volume_usd"]})

def run():
    sampling_data()
    sumary_data()

def sampling_time():
    for t in lt_time:
        schedule.every().day.at(t).do(run)

def main():
    sampling_time()
    run()
#    while True:
#        schedule.run_pending()
#	time.sleep(1)

if __name__ == "__main__":
  main()

