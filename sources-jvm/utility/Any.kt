package io.fluidsonic.meta


internal inline fun <Result> Boolean.thenTake(block: () -> Result): Result? =
	if (this) block() else null
