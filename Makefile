benchmark:
	./gradlew installDist installShadowDist
	cd benchmark && ./benchmark.sh
.PHONY: benchmark
