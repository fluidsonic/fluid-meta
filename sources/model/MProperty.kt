package com.github.fluidsonic.fluid.meta


@Suppress("EqualsOrHashCode")
data class MProperty(
	val getter: MPropertyAccessor.Getter,
	override val inheritanceRestriction: MInheritanceRestriction,
	val isConst: Boolean,
	val isDelegated: Boolean,
	override val isExpect: Boolean,
	override val isExternal: Boolean,
	val isLateinit: Boolean,
	val isVar: Boolean,
	val jvmFieldSignature: MJvmMemberSignature.Field?,
	val jvmSyntheticMethodForAnnotationsSignature: MJvmMemberSignature.Method?,
	val name: MVariableName,
	override val receiverParameterType: MTypeReference?,
	val setter: MPropertyAccessor.Setter?,
	override val source: MClassMemberSource,
	override val typeParameters: List<MTypeParameter>,
	override val versionRequirements: List<MVersionRequirement>,
	override val visibility: MVisibility
) : MClassMember,
	MExpectable,
	MExternalizable,
	MGeneralizable,
	MIdentifyable,
	MInheritanceRestrictable,
	MReceiverDeclarable,
	MVersionRestrictable,
	MVisibilityRestrictable {

	override val localId = MLocalId.Property(
		name = name,
		receiverParameterType = receiverParameterType
	)


	override fun hashCode() =
		localId.hashCode()


	override fun toString() = typeToString(
		"name" to name,
		"getter" to getter,
		"inheritanceRestriction" to inheritanceRestriction,
		"isConst" to isConst,
		"isDelegated" to isDelegated,
		"isExpect" to isExpect,
		"isExternal" to isExternal,
		"isLateinit" to isLateinit,
		"isVar" to isVar,
		"jvmFieldSignature" to jvmFieldSignature,
		"jvmSyntheticMethodForAnnotationsSignature" to jvmSyntheticMethodForAnnotationsSignature,
		"name" to name,
		"receiverParameterType" to receiverParameterType,
		"setter" to setter,
		"source" to source,
		"typeParameters" to typeParameters,
		"versionRequirements" to versionRequirements,
		"visibility" to visibility
	)


	companion object
}
