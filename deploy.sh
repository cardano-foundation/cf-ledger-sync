#!/usr/bin/env bash

set -x

helm upgrade --install cf-ledger-sync deploy/cf-ledger-sync \
  -n cf-explorer \
  -f deploy/cf-ledger-sync/values-preprod.yaml
