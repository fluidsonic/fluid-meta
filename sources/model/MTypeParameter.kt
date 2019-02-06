package com.github.fluidsonic.fluid.meta


@Suppress("EqualsOrHashCode")
data class MTypeParameter(
	override val annotations: List<MAnnotation>,
	val isReified: Boolean,
	val id: MTypeParameterId,
	val name: MTypeParameterName,
	val upperBounds: List<MTypeReference>,
	val variance: MVariance
) : MAnnotatable {

	override fun hashCode() =
		name.hashCode()


	override fun toString() = typeToString(
		"name" to name,
		"annotations" to annotations,
		"id" to id,
		"isReified" to isReified,
		"upperBounds" to upperBounds,
		"variance" to variance
	)


	companion object
}
