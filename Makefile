benchmark:
	./gradlew clean installDist installShadowDist
	./benchmark/benchmark.sh
	./benchmark/benchmark-validation.sh
.PHONY: benchmark
