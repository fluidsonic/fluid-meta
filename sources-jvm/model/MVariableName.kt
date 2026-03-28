package io.fluidsonic.meta


/** The name of a Kotlin variable or property. */
@JvmInline
public value class MVariableName(public val kotlin: String) {

	override fun toString(): String = kotlin


	public companion object
}
