import json
import requests
import os 
import time
from google.cloud import datastore

os.environ["GOOGLE_APPLICATION_CREDENTIALS"]="/home/tanho/coinpy/volume24bot/cmarket/keyfilecloud.json"
#/home/tanho/keyfile.json
# "/home/tanho/coinpy/volume24bot/cmarket/keyfilecloud.json"
client = datastore.Client()

lt = {'bitcoin','ethereum','ripple'}
  
def urlCoin(name):
  return requests.get('https://api.coinmarketcap.com/v1/ticker/'+name)

def toStruct(req):
  coins = req.json()
  coin = coins[0]
  struct = [] 
  for c in coin:
    struct.append((c, coin[c]))
  return struct

def toDatastore(name,struct):  
  key = client.key(name)
  item = datastore.Entity(key)
  for (k,v) in struct:  
    item.update({k:v})
  key = client.put(item)
  
def main():
  while 1:
    for name in lt:
      req = urlCoin(name)
      struct = toStruct(req)
      run = toDatastore(name,struct)
    time.sleep(300) #delay per 5 mins for update

  return "done"
if __name__ == "__main__":
  main()

