package io.fluidsonic.meta


/** An identifier for the flexibility of a type (e.g., platform types). */
@JvmInline
public value class MTypeFlexibilityId(public val kotlin: String) {

	override fun toString(): String = kotlin


	public companion object
}
