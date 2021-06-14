#!/bin/sh -e

trap 'sleep 1 && (jobs -p | xargs -r kill -9)' EXIT

export TZ=Asia/Seoul

date >>result.txt
echo "" >>result.txt

./benchmark-2-memory-usage/build/install/benchmark-2-memory-usage/bin/benchmark-2-memory-usage &
sleep 1
./quit/build/install/quit/bin/quit
sleep 1

java -jar benchmark-3-execution-image-size/build/libs/benchmark-3-execution-image-size-all.jar &
sleep 1
./quit/build/install/quit/bin/quit
sleep 1
