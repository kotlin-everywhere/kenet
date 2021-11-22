benchmark:
	./gradlew installDist installShadowDist
	./benchmark/benchmark.sh --stop-gradle=false
	./benchmark/benchmark-validation.sh

clean:
	./gradlew clean

benchmark-loop: clean
	while true; do make || exit $$?; done

test-echo:
	./gradlew installDist
	cd test-echo && ./test.sh

docs:
	./gradlew clean
	./gradlew dokkaHtmlMultiModule
	rm -rf docs
	cp -r build/dokka/htmlMultiModule docs

.PHONY: benchmark clean benchmark-loop test-echo docs
