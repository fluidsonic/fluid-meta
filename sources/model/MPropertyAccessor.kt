package com.github.fluidsonic.fluid.meta

sealed class MPropertyAccessor : MExecutable, MExternalizable, MInlineable, MVisibilityRestrictable {

	abstract val isDefault: Boolean


	companion object;


	data class Getter(
		override val isDefault: Boolean,
		override val isExternal: Boolean,
		override val isInline: Boolean,
		override val jvmSignature: MJvmMemberSignature.Method?,
		val returnType: MTypeReference,
		override val visibility: MVisibility
	) : MPropertyAccessor() {

		override fun toString() =
			MetaCodeWriter.write(this)


		@Deprecated(level = DeprecationLevel.HIDDEN, message = "Getters don't have value parameters")
		override val valueParameters: List<Nothing>
			get() = emptyList()
	}


	data class Setter(
		override val isDefault: Boolean,
		override val isExternal: Boolean,
		override val isInline: Boolean,
		val parameter: MValueParameter,
		override val jvmSignature: MJvmMemberSignature.Method?,
		override val visibility: MVisibility
	) : MPropertyAccessor() {

		override fun toString() =
			MetaCodeWriter.write(this)


		override val valueParameters =
			listOf(parameter)
	}
}
