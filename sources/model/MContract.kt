package io.fluidsonic.meta


data class MContract(
	val effects: List<MEffect>
) {

	override fun toString() =
		MetaCodeWriter.write(this)


	companion object
}
