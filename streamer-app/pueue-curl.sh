#!/bin/bash

PAYLOAD_FILE=$1

while [ ! -e $PAYLOAD_FILE ]
do
  sleep 0.1
done

HTTP_CODE=$(curl -s -o /tmp/$$.log -w "%{http_code}" http://fluree:8090/fluree/transact \
  --header 'Content-Type: application/json' \
  --data @${PAYLOAD_FILE})

if [ $HTTP_CODE -ne 200 ] 
then
  echo "[!] Fluree HTTP response: ${HTTP_CODE} - message: $(cat /tmp/$$.log)"
  rm -f /tmp/$$.log
  exit 1
else
  rm -f ${PAYLOAD_FILE}
fi
