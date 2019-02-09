package com.github.fluidsonic.fluid.meta

@Suppress("EqualsOrHashCode")
data class MTypeAlias(
	override val annotations: List<MAnnotation>,
	val expandedType: MTypeReference,
	val name: MQualifiedTypeName,
	override val typeParameters: List<MTypeParameter>,
	val underlyingType: MTypeReference,
	override val versionRequirements: List<MVersionRequirement>,
	override val visibility: MVisibility
) : MAnnotatable, MGeneralizable, MVersionRestrictable, MVisibilityRestrictable, MIdentifyable {

	override val localId by lazy { MLocalId.Type(name = name.withoutPackage()) }


	override fun hashCode() =
		localId.hashCode()


	override fun toString() =
		MetaCodeWriter.write(this)


	companion object
}
