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

	val isCrossinline
		get() = Flag.ValueParameter.IS_CROSSINLINE(flags)

	val isNoinline
		get() = Flag.ValueParameter.IS_NOINLINE(flags)


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
		"isCrossinline" to isCrossinline,
		"isNoinline" to isNoinline,
		"isVariadic" to isVariadic,
		"type" to type
	)


	companion object
}
