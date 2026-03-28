package io.fluidsonic.meta


/** The name of a type parameter (e.g., `T`, `E`). */
@JvmInline
public value class MTypeParameterName(public val kotlin: String) {

	override fun toString(): String = kotlin


	public companion object
}
