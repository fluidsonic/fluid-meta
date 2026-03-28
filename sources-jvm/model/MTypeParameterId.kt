package io.fluidsonic.meta


/** A unique numeric identifier for a type parameter within its declaration scope. */
@JvmInline
public value class MTypeParameterId(public val value: Int) {

	override fun toString(): String = value.toString()


	public companion object
}
