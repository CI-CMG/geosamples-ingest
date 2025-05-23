#!/bin/bash

set -ex

if [[ -z "$MB_WORK_DIR" ]]; then
   MB_WORK_DIR="$(pwd)/test-dir/work"
fi


docker run -d --name geosamples-ingest-test-1 -p 15215:1521 -v $(pwd)/src/test/resources/db-startup:/opt/oracle/scripts/startup -d container-registry.oracle.com/database/free:23.6.0.0-lite