package com.github.fluidsonic.fluid.meta

import kotlinx.metadata.Flag
import kotlinx.metadata.Flags
import kotlinx.metadata.KmValueParameterVisitor


internal class MValueParameterBuilder(
	private val flags: Flags,
	private val name: MVariableName
) : KmValueParameterVisitor() {

	private var isVariadic = false
	private var type: MTypeReferenceBuilder? = null


	fun build() = MValueParameter(
		declaresDefaultValue = Flag.ValueParameter.DECLARES_DEFAULT_VALUE(flags),
		isCrossinline = Flag.ValueParameter.IS_CROSSINLINE(flags),
		isNoinline = Flag.ValueParameter.IS_NOINLINE(flags),
		isVariadic = isVariadic,
		name = name,
		type = type?.build() ?: throw MetaException("Value parameter '$name' has no type")
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
