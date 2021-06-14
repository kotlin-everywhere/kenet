#!/bin/sh -e

die() {
  message=$1
  [ -z "$message" ] && message="Died"
  echo "$message" >&2
  exit 1
}

cd "$(dirname "$0")"

trap 'sleep 1 && (jobs -p | xargs -r kill -9)' EXIT

export TZ=Asia/Seoul

rm -f result-*.txt

date >>result.txt

echo "* /proc/version" >>result.txt
tee -a result.txt </proc/version || echo "N/A" >>result.txt
echo "" >>result.txt

echo "* /proc/cpuinfo" >>result.txt
tee -a result.txt </proc/cpuinfo || echo "N/A" >>result.txt
echo "" >>result.txt

echo "* /proc/meminfo" >>result.txt
tee -a result.txt </proc/meminfo || echo "N/A" >>result.txt
echo "" >>result.txt

./benchmark-1-server-launch-time/build/install/benchmark-1-server-launch-time/bin/benchmark-1-server-launch-time &
sleep 0.5
./quit/build/install/quit/bin/quit
sleep 1

./benchmark-2-memory-usage/build/install/benchmark-2-memory-usage/bin/benchmark-2-memory-usage &
sleep 0.5
./quit/build/install/quit/bin/quit
sleep 1

java -jar benchmark-3-execution-image-size/build/libs/benchmark-3-execution-image-size-all.jar &
sleep 0.5
./quit/build/install/quit/bin/quit
sleep 1
