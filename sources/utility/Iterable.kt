package com.github.fluidsonic.fluid.meta


internal inline fun <T, R> Iterable<T>?.mapOrEmpty(transform: (T) -> R) =
	this?.map(transform) ?: emptyList()


internal fun <T> Iterable<T>?.toListOrEmpty() =
	this?.toList() ?: emptyList()
