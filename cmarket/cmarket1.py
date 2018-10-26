import json
import requests

name = 'bitcoin/'
r = requests.get('https://api.coinmarketcap.com/v1/ticker/'+name)

coins = r.json()
coin = coins[0]
struct = []
for c in coin:
	print(c + ":" + coin[c])
	struct.append((c, coin[c]))

for (x,y) in struct:
	print x, y

import os 
os.environ["GOOGLE_APPLICATION_CREDENTIALS"]="/home/tanho/coinpy/volume24bot/cmarket/keyfilecloud.json"

from google.cloud import datastore
import time

client = datastore.Client()

while 1:
	key = client.key('bitcoin')
	item = datastore.Entity(key)
	for (k,v) in struct:	
		item.update({k:v})
	key = client.put(item)
	sleep(300) #delay per 5 mins for update

