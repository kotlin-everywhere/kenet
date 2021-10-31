import org.kotlin.everywhere.net.Call
import org.kotlin.everywhere.net.Kenet
import org.kotlin.everywhere.net.SubKenet
import org.kotlin.everywhere.net.gen.typescript.Deno
import org.kotlin.everywhere.net.gen.typescript.generate
import kotlin.io.path.Path

fun main() {
    val initialTimes = System.currentTimeMillis()

    val kenet = Api()
    generate(kenet, Path("benchmark-4-generate-typescript/dist"), "api", Deno)

    val generateTimes = System.currentTimeMillis() - initialTimes

    log(
        "4. generate-typescript",
        "endpoint-counts: ${sumEndpointSize(kenet)}",
        "generate-times: ${generateTimes}ms",
    )

    logResult("4-generate-typescript", generateTimes)
}

private fun sumEndpointSize(kenet: Kenet): Int {
    return kenet._endpoints.sumOf {
        when (it) {
            is Call<*, *> -> 1
            is SubKenet<*> -> sumEndpointSize(it.sub)
        }
    }
}