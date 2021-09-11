#!/bin/sh -e

BASEDIR=$(dirname "$0")
cd "$BASEDIR"

exec kotlinc -script generate.kts
