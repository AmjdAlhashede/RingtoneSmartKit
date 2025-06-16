package io.github.ringtonesmartkit.extensions

internal inline fun <R> runInCatchingBlock(block: () -> R): Result<R> {
    return runCatching {
        block()
    }
}

internal inline fun <T, R> T.runInCatchingBlock(block: T.() -> R): Result<R> {
    return runCatching {
        block()
    }
}
