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

echo ""

RESULT_2_MEMORY_USAGE="$(cat result-2-memory-usage.txt)"
if [ "$RESULT_2_MEMORY_USAGE" -ge 5242880 ]; then
  die "overflow memory usage : memory-usage = $RESULT_2_MEMORY_USAGE"
fi
RESULT_3_EXECUTION_IMAGE_SIZE="$(cat result-3-execution-image-size.txt)"
if [ "$RESULT_3_EXECUTION_IMAGE_SIZE" -ge 10485760 ]; then
  die "overflow execution image size : image-size = $RESULT_3_EXECUTION_IMAGE_SIZE"
fi

echo "* benchmark result validation : PASS"
