package io.fluidsonic.meta


/** Represents the upper bound of a flexible type (e.g., a platform type from Java). */
public data class MFlexibleTypeUpperBound(
	val type: MTypeReference,
	val typeFlexibilityId: MTypeFlexibilityId?
) {

	override fun toString(): String =
		(typeFlexibilityId?.kotlin ?: "<unnamed>") + ": " + type


	public companion object
}
