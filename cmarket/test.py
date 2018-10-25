import json
import requests

r = requests.get('https://api.coinmarketcap.com/v1/ticker/')

for y in r.json():
	print(y)
{
  "market_cap_usd": "63720958.0",
  "price_usd": "0.5827362112",
  "last_updated": "1540476750",
  "name": "TenX",
  "24h_volume_usd": "788879.398331",
  "percent_change_7d": "2.24",
  "symbol": "PAY",
  "max_supply": "None",
  "rank": "100",
  "percent_change_1h": "-0.1",
  "total_supply": "205218256.0",
  "price_btc": "0.00009027",
  "available_supply": "109347861.0",
  "percent_change_24h": "1.48",
  "id": "tenx"
}