import json
import requests

r = requests.get('https://api.coinmarketcap.com/v1/ticker/')

coins = r.json()
print(coins)