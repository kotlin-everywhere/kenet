include(":kenet-dsl")
include(":kenet-server", ":kenet-server-engine-http")
include(":kenet-client", ":kenet-client-engine-http")
include(":kenet-gen-typescript")
include(":test-integration")
include(":test-echo:def", ":test-echo:client-jvm", ":test-echo:server-jvm")

include(
    ":benchmark:benchmark-1-server-launch-time",
    ":benchmark:benchmark-2-memory-usage",
    ":benchmark:benchmark-3-execution-image-size",
    ":benchmark:benchmark-4-generate-typescript",
)

include(":benchmark:common", ":benchmark:quit")