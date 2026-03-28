package io.fluidsonic.meta


/** The name of a Kotlin module. */
@JvmInline
public value class MModuleName(public val kotlin: String) {

	override fun toString(): String = kotlin


	public companion object
}
