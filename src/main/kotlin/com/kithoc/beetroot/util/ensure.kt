package com.kithoc.beetroot.util

const val genericError = "Ensure failed"

inline fun ensure(
    condition: () -> Boolean,
    message: () -> String = { genericError },
    exception: (String) -> Throwable = ::IllegalStateException,
): Unit = run { if (!condition()) throw exception(message()) }

inline fun ensure(
    condition: () -> Boolean,
    message: String,
    exception: (String) -> Throwable = ::IllegalStateException,
) = ensure(condition, { message }, exception)

inline fun ensure(
    condition: Boolean,
    message: () -> String = { genericError },
    exception: (String) -> Throwable = ::IllegalStateException,
) = ensure({ condition }, message, exception)

inline fun ensure(
    condition: Boolean,
    message: String,
    exception: (String) -> Throwable = ::IllegalStateException
) = ensure({ condition }, { message }, exception)

inline fun <T> T.ensure(
    condition: T.() -> Boolean,
    message: () -> String = { genericError },
    exception: (String) -> Throwable = ::IllegalStateException,
): T = also { if (!condition()) throw exception(message()) }

inline fun <T> T.ensure(
    condition: T.() -> Boolean,
    message: String,
    exception: (String) -> Throwable = ::IllegalStateException,
): T = ensure(condition, { message }, exception)

