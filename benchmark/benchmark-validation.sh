#!/bin/sh -e

die() {
  message=$1
  [ -z "$message" ] && message="Died"
  echo "$message" >&2
  exit 1
}

cd "$(dirname "$0")"

RESULT_1_SERVER_LAUNCH_TIME="$(cat result-1-server-launch-time.txt)"
if [ "$RESULT_1_SERVER_LAUNCH_TIME" -ge 1000 ]; then
  die "overflow launch time : launch-time = $RESULT_1_SERVER_LAUNCH_TIME"
fi
RESULT_2_MEMORY_USAGE="$(cat result-2-memory-usage.txt)"
if [ "$RESULT_2_MEMORY_USAGE" -ge 5242880 ]; then
  die "overflow memory usage : memory-usage = $RESULT_2_MEMORY_USAGE"
fi
RESULT_3_EXECUTION_IMAGE_SIZE="$(cat result-3-execution-image-size.txt)"
if [ "$RESULT_3_EXECUTION_IMAGE_SIZE" -ge 10485760 ]; then
  die "overflow execution image size : image-size = $RESULT_3_EXECUTION_IMAGE_SIZE"
fi

RESULT_4_GENERATE_TYPESCRIPT="$(cat result-4-generate-typescript.txt)"
if [ "$RESULT_4_GENERATE_TYPESCRIPT" -ge 10000 ]; then
  die "overflow generate times : generate-times = $RESULT_4_GENERATE_TYPESCRIPT"
fi

echo "* benchmark result validation : PASS"
