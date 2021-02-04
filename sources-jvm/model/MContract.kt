package io.fluidsonic.meta


public data class MContract(
	val effects: List<MEffect>
) {

	override fun toString(): String =
		MetaCodeWriter.write(this)


	public companion object
}
