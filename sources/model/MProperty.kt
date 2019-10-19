package io.fluidsonic.meta


@Suppress("EqualsOrHashCode")
data class MProperty(
	val getter: MPropertyAccessor.Getter,
	override val inheritanceRestriction: MInheritanceRestriction,
	val isConst: Boolean,
	val isDelegated: Boolean,
	override val isExpect: Boolean,
	override val isExternal: Boolean,
	override val isInline: Boolean,
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
	MInlineable,
	MReceiverDeclarable,
	MVersionRestrictable,
	MVisibilityRestrictable {

	override val localId by lazy {
		MLocalId.Property(
			name = name,
			receiverParameterType = receiverParameterType
		)
	}


	override fun hashCode() =
		localId.hashCode()


	override fun toString() =
		MetaCodeWriter.write(this)


	companion object
}
