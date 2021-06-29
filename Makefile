benchmark:
	./gradlew installDist
	./gradlew installShadowDist
	./benchmark/benchmark.sh --stop-gradle=false
	./benchmark/benchmark-validation.sh

clean:
	./gradlew clean

benchmark-loop: clean
	while true; do make || exit $$?; done

.PHONY: benchmark clean benchmark-loop
