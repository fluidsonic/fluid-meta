package io.fluidsonic.meta


/** A zero-based index of a value parameter in a function or constructor. */
@JvmInline
public value class MValueParameterIndex(public val value: Int) {

	override fun toString(): String = value.toString()


	public companion object
}
