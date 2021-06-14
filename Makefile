benchmark:
	./gradlew clean installDist installShadowDist
	cd benchmark && ./benchmark.sh
.PHONY: benchmark
