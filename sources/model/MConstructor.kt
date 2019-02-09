package com.github.fluidsonic.fluid.meta


@Suppress("EqualsOrHashCode")
data class MConstructor(
	val isPrimary: Boolean,
	override val jvmSignature: MJvmMemberSignature.Method?,
	override val valueParameters: List<MValueParameter>,
	override val visibility: MVisibility,
	override val versionRequirements: List<MVersionRequirement>
) : MExecutable,
	MIdentifyable,
	MVersionRestrictable,
	MVisibilityRestrictable {

	override val localId by lazy {
		MLocalId.Constructor(valueParameters = valueParameters)
	}


	override fun hashCode() =
		localId.hashCode()


	override fun toString() =
		MetaCodeWriter.write(this)


	companion object
}
