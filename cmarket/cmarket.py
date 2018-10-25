import json
import requests

r = requests.get('https://api.coinmarketcap.com/v1/ticker/')
class cstruct:
    def __init__(self, id, symbol, rank):
        self.id = id
        self.symbol = symbol
        self.rank = rank
carray = []
for coin in r.json():
    p = cstruct(coin["id"], coin["symbol"], coin["rank"]);
    carray.append(p)

for x in carray:
    print(x.id)
    print(x.symbol)
    print(x.rank)
