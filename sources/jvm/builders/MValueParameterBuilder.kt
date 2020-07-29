package io.fluidsonic.meta

import kotlinx.metadata.*


internal class MValueParameterBuilder(
	private val flags: Flags,
	private val name: MVariableName
) : KmValueParameterVisitor() {

	private var type: MTypeReferenceBuilder? = null
	private var varargElementType: MTypeReferenceBuilder? = null


	fun build() = MValueParameter(
		declaresDefaultValue = Flag.ValueParameter.DECLARES_DEFAULT_VALUE(flags),
		isCrossinline = Flag.ValueParameter.IS_CROSSINLINE(flags),
		isNoinline = Flag.ValueParameter.IS_NOINLINE(flags),
		name = name,
		type = type?.build() ?: throw MetaException("Value parameter '$name' has no type"),
		varargElementType = varargElementType?.build()
	)


	override fun visitType(flags: Flags) =
		MTypeReferenceBuilder(flags = flags)
			.also { type = it }


	override fun visitVarargElementType(flags: Flags) =
		MTypeReferenceBuilder(flags = flags)
			.also {
				varargElementType = it
			}
}
