package com.github.fluidsonic.fluid.meta


data class MFlexibleTypeUpperBound(
	val type: MTypeReference,
	val typeFlexibilityId: MTypeFlexibilityId?
) {

	override fun toString() = typeToString(
		"type" to type,
		"typeFlexibilityId" to typeFlexibilityId
	)


	companion object
}
