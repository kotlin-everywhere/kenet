package org.kotlin.everywhere.net.test.integration

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.promise
import kotlin.coroutines.CoroutineContext

// JS code:
val testScope = MainScope()
actual val testCoroutineContext: CoroutineContext = testScope.coroutineContext
actual fun runBlockingTest(block: suspend CoroutineScope.() -> Unit): dynamic = testScope.promise { block() }