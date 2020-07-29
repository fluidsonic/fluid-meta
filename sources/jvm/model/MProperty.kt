package io.fluidsonic.meta

import kotlinx.metadata.*


@Suppress("EqualsOrHashCode")
public data class MProperty(
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
	val jvmFlags: Flags,
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

	override val localId: MLocalId.Property by lazy {
		MLocalId.Property(
			name = name,
			receiverParameterType = receiverParameterType
		)
	}


	override fun hashCode(): Int =
		localId.hashCode()


	override fun toString(): String =
		MetaCodeWriter.write(this)


	public companion object
}
