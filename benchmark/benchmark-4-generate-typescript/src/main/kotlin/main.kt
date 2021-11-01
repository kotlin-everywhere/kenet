import org.kotlin.everywhere.net.Call
import org.kotlin.everywhere.net.Kenet
import org.kotlin.everywhere.net.SubKenet
import org.kotlin.everywhere.net.gen.typescript.Deno
import org.kotlin.everywhere.net.gen.typescript.generate
import kotlin.io.path.Path

fun main() {
    // 프로그램 시작 시각
    val initialTimes = System.currentTimeMillis()

    val kenet = Api()
    // 정의된 10000개 이상의 데이터 dist 아래에 api.ts 로 생성
    generate(kenet, Path("benchmark-4-generate-typescript/dist"), "api", Deno)

    // 생성에 소요된 시간 = 현재시각 - 프로그램 시작 시각
    val generateTimes = System.currentTimeMillis() - initialTimes

    log(
        "4. generate-typescript",
        "endpoint-counts: ${sumEndpointSize(kenet)}" /* 데이터 갯수 */,
        "generate-times: ${generateTimes}ms" /* 생성에 소요된 시간 */,
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
