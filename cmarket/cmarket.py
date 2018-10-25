import json
import requests

r = requests.get('https://api.coinmarketcap.com/v1/ticker/')
for coin in r.json():
    print(coin["id"])
    print("  "+coin["rank"])
    print("  "+coin["symbol"])
    
