#!/bin/bash

function create-fluree-ledger() {

  HTTP_CODE=$(curl -so /dev/null -w "%{http_code}" --location 'http://fluree:8090/fluree/create' \
    --header 'Content-Type: application/json' \
    --data \
'{
  "ledger": "cardano",
  "insert": {
    "@id": "fluree-cardano-genesis",
    "name": "fluree-cardano-genesis"
  }
}')
  echo $HTTP_CODE
}

until [[ $(create-fluree-ledger) == 200 ]] || [[  $(create-fluree-ledger) == 409 ]]
do
  sleep 1
done

mkdir -p /pueue-data/blocks
ITERATION=0
BATCH_SIZE=2000
TOPIC=${1:-transactionTopic}

until kcat -b kafka:9092 -f '%s' -c 1 -e -u -C -t ${TOPIC}
do
  sleep 1
done

while true
do
  BATCH_FILE=/pueue-data/blocks/batch-${ITERATION}.json
  kcat -b kafka:9092 -f '%s' -c ${BATCH_SIZE} -G kcat ${TOPIC} \
   | jq -rs '. | {ledger: "cardano", insert: . }' > ${BATCH_FILE}
  pueue add /bin/sh /src/pueue-curl.sh ${BATCH_FILE} &

  ITERATION=$(expr $ITERATION + 1)
done
