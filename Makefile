benchmark:
	./gradlew installDist installShadowDist
	./benchmark/benchmark.sh
	./benchmark/benchmark-validation.sh
.PHONY: benchmark

benchmark-loop:
	while true; do make || exit $$?; done
