var endpoints = (1..1_000).map { x -> "val endpoint$x by c<String, String>()" }

var apiKt = """
    import org.kotlin.everywhere.net.Kenet

    class Api : Kenet() {
    ${endpoints.joinToString("\n")}
    }
""".trimIndent()

java.io.File("src/main/kotlin/api.kt").writeText(apiKt)