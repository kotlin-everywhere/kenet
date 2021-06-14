benchmark:
	./gradlew installDist installShadowDist
	./benchmark/benchmark.sh
	./benchmark/benchmark-validation.sh
.PHONY: benchmark

benchmark-loop:
	./gradlew clean
	while true; do make || exit $$?; done
