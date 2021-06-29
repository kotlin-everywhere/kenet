#!/bin/sh -e

trap 'kill $(jobs -p)' EXIT

./server-jvm/build/install/server-jvm/bin/server-jvm &
SERVER_JVM_PID=$!

sleep 1

./client-jvm/build/install/client-jvm/bin/client-jvm
