package com.github.fluidsonic.fluid.meta

@Suppress("EqualsOrHashCode")
data class MValueParameter(
	val declaresDefaultValue: Boolean,
	val isCrossinline: Boolean,
	val isNoinline: Boolean,
	val name: MVariableName,
	val type: MTypeReference,
	val varargElementType: MTypeReference?
) {

	override fun hashCode() =
		name.hashCode()


	override fun toString() =
		MetaCodeWriter.write(this)


	companion object
}
