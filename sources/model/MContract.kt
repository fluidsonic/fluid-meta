package com.github.fluidsonic.fluid.meta


data class MContract(
	val effects: List<MEffect>
) {

	override fun toString() = typeToString(
		"effects" to effects
	)


	companion object
}
