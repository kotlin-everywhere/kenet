import org.kotlin.everywhere.net.gen.typescript.generate
import kotlin.io.path.Path

fun main() {
    val initialTimes = System.currentTimeMillis()

    val kenet = Api()
    generate(kenet, Path("benchmark-4-generate-typescript/dist"), "api")

    val generateTimes = System.currentTimeMillis() - initialTimes

    log(
        "4. generate-typescript",
        "endpoint-counts: ${kenet._endpoints.size}",
        "generate-times: ${generateTimes}ms",
    )

    logResult("4-generate-typescript", generateTimes)
}