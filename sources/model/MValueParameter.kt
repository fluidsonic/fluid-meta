package com.github.fluidsonic.fluid.meta

import kotlinx.metadata.Flag
import kotlinx.metadata.Flags
import java.util.Objects


class MValueParameter internal constructor(
	private val flags: Flags,
	val isVariadic: Boolean,
	val name: MVariableName,
	val type: MTypeReference
) {

	val declaresDefaultValue
		get() = Flag.ValueParameter.DECLARES_DEFAULT_VALUE(flags)

	val hasAnnotations
		get() = Flag.HAS_ANNOTATIONS(flags)

	val isCrossinline
		get() = Flag.ValueParameter.IS_CROSSINLINE(flags)

	val isNoinline
		get() = Flag.ValueParameter.IS_NOINLINE(flags)

	val modality = MModality.forFlags(flags)

	val visibility = MVisibility.forFlags(flags)
		?: throw MetadataException("Value parameter '$name' has an unsupported visibility (flags: $flags)")


	override fun equals(other: Any?): Boolean {
		if (other === this) return true
		if (other !is MValueParameter) return false

		return flags == other.flags &&
			isVariadic == other.isVariadic &&
			name == other.name &&
			type == other.type
	}


	override fun hashCode() =
		Objects.hash(
			flags,
			isVariadic,
			name,
			type
		)


	override fun toString() = typeToString(
		"name" to name,
		"declaresDefaultValue" to declaresDefaultValue,
		"hasAnnotations" to hasAnnotations,
		"isCrossinline" to isCrossinline,
		"isNoinline" to isNoinline,
		"isVariadic" to isVariadic,
		"modality" to modality,
		"type" to type,
		"visibility" to visibility
	)


	companion object
}
