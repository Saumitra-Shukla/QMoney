

api_key='3363b903ca0b6da429d1fa9209f385716d4a6359'
date='2020-02-28'
stock_name='aapl'
url_curl="https://api.tiingo.com/tiingo/daily/"$stock_name"/prices?startDate="$date"&token="$api_key

#echo $url_curl

curl -X GET -H 'Accept: */*' \a
-H 'Accept-Encoding: gzip, deflate' \
-H 'Connection: keep-alive' \
-H 'Content-Type: application/json' \
-H 'User-Agent: python-requests/2.18.4' \
$url_curl -o qmoney/bin/main/trades_tiingo.json