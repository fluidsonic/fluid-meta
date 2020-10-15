package io.fluidsonic.meta


@Suppress("EqualsOrHashCode")
public data class MConstructor(
	val isSecondary: Boolean,
	override val jvmSignature: MJvmMemberSignature.Method?,
	override val valueParameters: List<MValueParameter>,
	override val visibility: MVisibility,
	override val versionRequirements: List<MVersionRequirement>
) : MExecutable,
	MIdentifyable,
	MVersionRestrictable,
	MVisibilityRestrictable {

	public val isPrimary: Boolean
		get() = !isSecondary


	override val localId: MLocalId.Constructor by lazy {
		MLocalId.Constructor(valueParameters = valueParameters)
	}


	override fun hashCode(): Int =
		localId.hashCode()


	override fun toString(): String =
		MetaCodeWriter.write(this)


	public companion object
}
