package org.kotlin.everywhere.net.test.integration

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext


// kotlinx-coroutines-test 는 JVM 라이브러리다. 현재 기능 개발 논의 중
// https://github.com/Kotlin/kotlinx.coroutines/issues/1996
expect fun runBlockingTest(block: suspend CoroutineScope.() -> Unit)
expect val testCoroutineContext: CoroutineContext

