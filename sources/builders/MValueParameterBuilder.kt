package com.github.fluidsonic.fluid.meta

import kotlinx.metadata.Flags
import kotlinx.metadata.KmValueParameterVisitor


internal class MValueParameterBuilder(
	private val flags: Flags,
	private val name: MVariableName
) : KmValueParameterVisitor() {

	private var isVariadic = false
	private var type: MTypeReferenceBuilder? = null


	fun build() = MValueParameter(
		flags = flags,
		isVariadic = isVariadic,
		name = name,
		type = type?.build() ?: throw MetadataException("Value parameter '$name' has no types")
	)


	override fun visitType(flags: Flags) =
		MTypeReferenceBuilder(flags = flags)
			.also { type = it }


	override fun visitVarargElementType(flags: Flags) =
		MTypeReferenceBuilder(flags = flags)
			.also {
				isVariadic = true
				type = it
			}
}
