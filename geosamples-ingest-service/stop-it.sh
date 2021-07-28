#!/bin/bash

set -ex

docker stop geosamples-ingest-test-1
docker container rm geosamples-ingest-test-1
