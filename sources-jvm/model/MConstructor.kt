package io.fluidsonic.meta


/** Represents a Kotlin constructor in the metadata model. */
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

	/** Whether this is a primary constructor. */
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
