import json
import requests

r = requests.get('https://api.coinmarketcap.com/v1/ticker/')
class cstruct:
    def __init__(self, id, symbol, rank):
        self.id = id
        self.symbol = symbol
        self.rank = rank
carray = []
for y in r.json():
	print(y)
for coin in r.json():
    p = cstruct(coin["id"], coin["symbol"], coin["rank"]);
    carray.append(p)

for x in carray:
    print(x.id)
    print(x.symbol)
    print(x.rank)
import os 
os.environ["GOOGLE_APPLICATION_CREDENTIALS"]="/home/tanho/coinpy/volume24bot/cmarket/keyfilecloud.json"

import google.auth
from google.cloud import datastore

credentials, projectId = google.auth.default()
client = datastore.Client()

key = client.key('TenX')
item = datastore.Entity(key)
item.update({
  		'market_cap_usd': 63720958.0,
  		'price_usd': 0.5827362112,
  		'last_updated': 1540476750,
  		'name': "TenX",
  		'24h_volume_usd': 788879.398331,
  		'percent_change_7d': 2.24,
  		'symbol': "PAY",
  		'max_supply': "None",
  		'rank': 100,
  		'percent_change_1h': -0.1,
  		'total_supply': 205218256.0,
  		'price_btc': 0.00009027,
  		'available_supply': 109347861.0,
  		'percent_change_24h': 1.48,
  		'id': 'tenx'
})
key = client.put(item)
# {
#   "market_cap_sd": "63812222.0",
#   "price_sd": "0.583570828",
#   "last_pdated": "1540474772",
#   "name": "TenX",
#   "24h_volme_sd": "780045.695122",
#   "percent_change_7d": "2.3",
#   "symbol": "PAY",
#   "max_spply": "",
#   "rank": "100",
#   "percent_change_1h": "-0.12",
#   "total_spply": "205218256.0",
#   "price_btc": "0.00009015",
#   "available_spply": "109347861.0",
#   "percent_change_24h": "1.6",
#   "id": "tenx"
# }