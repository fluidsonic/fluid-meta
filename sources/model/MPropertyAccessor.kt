package com.github.fluidsonic.fluid.meta


sealed class MPropertyAccessor : MExecutable, MExternalizable, MInlineable {

	abstract val isDefault: Boolean


	companion object;


	data class Getter(
		override val isDefault: Boolean,
		override val isExternal: Boolean,
		override val isInline: Boolean,
		override val jvmSignature: MJvmMemberSignature.Method?,
		val returnType: MTypeReference
	) : MPropertyAccessor() {

		override fun toString() = typeToString(
			"isDefault" to isDefault,
			"isExternal" to isExternal,
			"isInline" to isInline,
			"jvmSignature" to jvmSignature,
			"returnType" to returnType
		)


		@Deprecated(level = DeprecationLevel.HIDDEN, message = "Getters don't have value parameters")
		override val valueParameters: List<Nothing>
			get() = emptyList()
	}


	data class Setter(
		override val isDefault: Boolean,
		override val isExternal: Boolean,
		override val isInline: Boolean,
		val parameter: MValueParameter,
		override val jvmSignature: MJvmMemberSignature.Method?
	) : MPropertyAccessor() {

		override fun toString() = typeToString(
			"isDefault" to isDefault,
			"isExternal" to isExternal,
			"isInline" to isInline,
			"jvmSignature" to jvmSignature,
			"parameter" to parameter
		)


		override val valueParameters =
			listOf(parameter)
	}
}
