import json
import requests
import os,errno 
import time
from google.cloud import firestore

os.environ["GOOGLE_APPLICATION_CREDENTIALS"]="/home/tanho/create_service/py/volume24bot/CoinMarketCap/keyfilecloud2.json"
#/home/tanho/create_service/py/volume24bot/CoinMarketCap
#/home/tanho/keyfile.json
# "/home/tanho/coinpy/volume24bot/cmarket/keyfilecloud.json
#/home/tanho/github/py/volume24bot/cmarket
client = firestore.Client()

lt_coin = {'bitcoin','ethereum','ripple','litecoin','dogecoin',
        "bitcoin-cash","eos","stellar","cardano","monero","tether"}
lt_time = {'06:00','14:00','23:30'}

def urlCoin(name):
  return requests.get('https://api.coinmarketcap.com/v1/ticker/'+name)

#backup data
#create back up folder
try:
    os.makedirs("back_up")
except OSError as e:
    if e.errno != errno.EEXIST:
        raise

import json
def backup_data():
  fwrite = open("back_up/CMC_" + unicode(datetime.datetime.now().date()), "w+")
  json.dump(urlCoin("").json(), fwrite)

def read_from_back_up(backup_date):
  fread = open("back_up/CMC_"+ unicode(backup_date), "r+")
  backup_data = json.load(fread)

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
#def sumary_data():
#    limit_date = datetime.datetime.now().date() - timedelta(days=1)
#    for name in lt_coin:
#        docs = client.collection(name)
#        for i in docs:
#            if (docs["updated_date"] > limit_date)
#                client.collection(name).document(docs["updated_date"]).
#                set({"volume_usd":docs["volume_usd"]})

def run():
    backup_data()
    sampling_data()
#    sumary_data()

def sampling_time():
    for t in lt_time:
        schedule.every().day.at(t).do(run)

schedule.every().day.at("6:00").do(run)
schedule.every().day.at("14:00").do(run)
schedule.every().day.at("23:30").do(run)
#test
#schedule.every().day.at("16:20").do(sampling_data)
def main():
    sampling_time()
    while True:
        #schedule.run_pending()
        run()
	time.sleep(2)

if __name__ == "__main__":
  main()

