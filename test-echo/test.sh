#!/bin/sh -e

trap 'kill $(jobs -p)' EXIT

echo " * test jvm <-> jvm"
./server-jvm/build/install/server-jvm/bin/server-jvm &
SERVER_JVM_PID=$!
sleep 1
./client-jvm/build/install/client-jvm/bin/client-jvm
kill $SERVER_JVM_PID
sleep 1
echo

echo " * test jvm <-> deno"
./server-jvm/build/install/server-jvm/bin/server-jvm &
sleep 1
deno run --allow-net client-deno/clientDeno.ts
