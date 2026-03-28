package io.fluidsonic.meta


/** The name of a Kotlin enum entry. */
@JvmInline
public value class MEnumEntryName(public val kotlin: String) {

	override fun toString(): String = kotlin


	public companion object
}
