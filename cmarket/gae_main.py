from google.appengine.api import urlfetch

import json

name = 'bitcoin/'
url = 'https://api.coinmarketcap.com/v1/ticker/'+name

result = urlfetch.fetch(url)

coins = result.content
print(coins)
coin = coins[0]
print(coin)
struct = []
for c in coin:
	print c,coin[c]
	struct.append((c, coin[c]))

for (x,y) in struct:
	print x, y

import os 
os.environ["GOOGLE_APPLICATION_CREDENTIALS"]="/home/tanho/coinpy/volume24bot/cmarket/keyfilecloud.json"
#/home/tanho/keyfile.json
from google.cloud import datastore
import time

client = datastore.Client()

while 1:
	key = client.key('bitcoin')
	item = datastore.Entity(key)
	for (k,v) in struct:	
		item.update({k:v})
	key = client.put(item)
	time.sleep(10) #delay per 5 mins for update

