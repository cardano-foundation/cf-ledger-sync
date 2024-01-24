#!/bin/bash

FLUREE_HOST=127.0.0.1

curl -s -X 'POST' \
  "http://${FLUREE_HOST}:58090/fluree/query" \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "from": "cardano",
  "select": {
    "?s": [
      "*",
      { "amounts": ["*"] },
      { "body": ["*"] },
      { "witnesses": ["*"] },
      { "utxos": ["*"] },
      { "update": ["*"] },
      { "outputs": ["*"] },
      { "inputs": ["*"] }
    ]
  },
  "where": {
    "@id": "?s",
    "txHash": "59f68ea73b95940d443dc516702d5e5deccac2429e4d974f464cc9b26292fd9c"
  }
}'
