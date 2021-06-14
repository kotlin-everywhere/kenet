benchmark:
	./gradlew installDist installShadowDist
	./benchmark/benchmark.sh
	./benchmark/benchmark-validation.sh
.PHONY: benchmark
