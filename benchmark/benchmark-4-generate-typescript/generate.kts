fun echoEndpoint(prefix: String): String {
    return (1..1_000).map { x -> "val endpoint$prefix$x by c<String, String>()" }.joinToString("\n")
}

var endpoints = (1..10).map { x ->
    """val sub$x by c(object: Kenet() {
        ${echoEndpoint("${x}_")}
        })"""
        .trimIndent()
}

var apiKt = """
    import org.kotlin.everywhere.net.Kenet

    class Api : Kenet() {
    ${endpoints.joinToString("\n")}
    }
""".trimIndent()

java.io.File("src/main/kotlin/api.kt").writeText(apiKt)