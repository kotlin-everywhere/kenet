include(":kenet-dsl")
include(":kenet-server", ":kenet-server-engine-http")
include(":kenet-client")
include(":test-integration")

include(
    ":benchmark:benchmark-2-memory-usage",
    ":benchmark:benchmark-3-execution-image-size",
)

include(":benchmark:common", ":benchmark:quit")