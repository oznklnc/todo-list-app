#!/usr/bin/env bash
set -m

/entrypoint.sh couchbase-server &

sleep 15

# Setup index and memory quota
curl -v -X POST http://127.0.0.1:8091/pools/default \
  -d memoryQuota=1500 \
  -d indexMemoryQuota=512

# Setup services
curl -v http://127.0.0.1:8091/node/controller/setupServices \
  -d services=kv%2Cn1ql%2Cindex

# Setup credentials
curl -v http://127.0.0.1:8091/settings/web \
  -d port=8091 \
  -d username=${COUCHBASE_ADMINISTRATOR_USERNAME} \
  -d password=${COUCHBASE_ADMINISTRATOR_PASSWORD}

# Set indexer storage mode
curl -v -X POST -u ${COUCHBASE_ADMINISTRATOR_USERNAME}:${COUCHBASE_ADMINISTRATOR_PASSWORD} \
  http://127.0.0.1:8091/settings/indexes \
  -d 'storageMode=memory_optimized'

# Create buckets
curl -v -X POST -u ${COUCHBASE_ADMINISTRATOR_USERNAME}:${COUCHBASE_ADMINISTRATOR_PASSWORD} \
  -d name=${COUCHBASE_BUCKET} \
  -d ramQuotaMB=200 \
  -d authType=none \
  -d flushEnabled=1 \
  http://localhost:8091/pools/default/buckets

echo "Type: $TYPE"

if [ "$TYPE" = "WORKER" ]; then
  sleep 15

  IP=$(hostname -i)

  echo "Auto Rebalance: $AUTO_REBALANCE"
  if [ "$AUTO_REBALANCE" = "true" ]; then
    couchbase-cli rebalance \
      --cluster=$COUCHBASE_MASTER:8091 \
      --username=${COUCHBASE_ADMINISTRATOR_USERNAME} \
      --password=${COUCHBASE_ADMINISTRATOR_PASSWORD} \
      --server-add=$IP \
      --server-add-username=${COUCHBASE_ADMINISTRATOR_USERNAME} \
      --server-add-password=${COUCHBASE_ADMINISTRATOR_PASSWORD}
  else
    couchbase-cli server-add \
      --cluster=$COUCHBASE_MASTER:8091 \
      --username=${COUCHBASE_ADMINISTRATOR_USERNAME} \
      --password=${COUCHBASE_ADMINISTRATOR_PASSWORD} \
      --server-add=$IP \
      --server-add-username=${COUCHBASE_ADMINISTRATOR_USERNAME} \
      --server-add-password=${COUCHBASE_ADMINISTRATOR_PASSWORD}
  fi
fi
