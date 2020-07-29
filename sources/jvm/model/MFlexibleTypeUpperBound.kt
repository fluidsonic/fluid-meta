package io.fluidsonic.meta


public data class MFlexibleTypeUpperBound(
	val type: MTypeReference,
	val typeFlexibilityId: MTypeFlexibilityId?
) {

	override fun toString(): String =
		(typeFlexibilityId?.kotlin ?: "<unnamed>") + ": " + type


	public companion object
}
