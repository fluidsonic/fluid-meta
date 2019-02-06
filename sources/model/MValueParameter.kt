package com.github.fluidsonic.fluid.meta


@Suppress("EqualsOrHashCode")
data class MValueParameter(
	val declaresDefaultValue: Boolean,
	val isCrossinline: Boolean,
	val isNoinline: Boolean,
	val isVariadic: Boolean,
	val name: MVariableName,
	val type: MTypeReference
) {

	override fun hashCode() =
		name.hashCode()


	override fun toString() = typeToString(
		"name" to name,
		"declaresDefaultValue" to declaresDefaultValue,
		"isCrossinline" to isCrossinline,
		"isNoinline" to isNoinline,
		"isVariadic" to isVariadic,
		"type" to type
	)


	companion object
}
