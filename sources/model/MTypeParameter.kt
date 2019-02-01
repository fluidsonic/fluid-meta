package com.github.fluidsonic.fluid.meta

import kotlinx.metadata.Flag
import kotlinx.metadata.Flags
import java.util.Objects


class MTypeParameter internal constructor(
	val annotations: List<MAnnotation>,
	private val flags: Flags,
	val id: MTypeParameterId,
	val name: MTypeParameterName,
	val upperBounds: List<MTypeReference>,
	val variance: MVariance
) {

	val hasAnnotations
		get() = Flag.HAS_ANNOTATIONS(flags)

	val isReified
		get() = Flag.TypeParameter.IS_REIFIED(flags)


	override fun equals(other: Any?): Boolean {
		if (other === this) return true
		if (other !is MTypeParameter) return false

		return annotations == other.annotations &&
			flags == other.flags &&
			id == other.id &&
			name == other.name &&
			upperBounds == other.upperBounds &&
			variance == other.variance
	}


	override fun hashCode() =
		Objects.hash(
			annotations,
			flags,
			id,
			name,
			upperBounds,
			variance
		)


	override fun toString() = typeToString(
		"name" to name,
		"annotations" to annotations,
		"hasAnnotations" to hasAnnotations,
		"id" to id,
		"isReified" to isReified,
		"upperBounds" to upperBounds,
		"variance" to variance
	)


	companion object
}
