package io.fluidsonic.meta


@Suppress("EqualsOrHashCode")
public data class MTypeAlias(
	override val annotations: List<MAnnotation>,
	val expandedType: MTypeReference,
	val name: MQualifiedTypeName,
	override val typeParameters: List<MTypeParameter>,
	val underlyingType: MTypeReference,
	override val versionRequirements: List<MVersionRequirement>,
	override val visibility: MVisibility
) : MAnnotatable, MGeneralizable, MVersionRestrictable, MVisibilityRestrictable, MIdentifyable {

	override val localId: MLocalId.Type by lazy { MLocalId.Type(name = name.withoutPackage()) }


	override fun hashCode(): Int =
		localId.hashCode()


	override fun toString(): String =
		MetaCodeWriter.write(this)


	public companion object
}
