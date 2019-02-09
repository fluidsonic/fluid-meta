package com.github.fluidsonic.fluid.meta


data class MFlexibleTypeUpperBound(
	val type: MTypeReference,
	val typeFlexibilityId: MTypeFlexibilityId?
) {

	override fun toString() =
		(typeFlexibilityId?.kotlin ?: "<unnamed>") + ": " + type


	companion object
}
