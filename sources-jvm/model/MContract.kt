package io.fluidsonic.meta


/** Represents a Kotlin contract declaration on a function. */
public data class MContract(
	val effects: List<MEffect>
) {

	override fun toString(): String =
		MetaCodeWriter.write(this)


	public companion object
}
