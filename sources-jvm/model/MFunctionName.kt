package io.fluidsonic.meta


/** The name of a Kotlin function. */
@JvmInline
public value class MFunctionName(public val kotlin: String) {

	override fun toString(): String = kotlin


	public companion object
}
