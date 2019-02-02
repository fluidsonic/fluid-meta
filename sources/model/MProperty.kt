package com.github.fluidsonic.fluid.meta

import kotlinx.metadata.Flag
import kotlinx.metadata.Flags
import java.util.Objects


class MProperty internal constructor(
	private val flags: Flags,
	private val getterFlags: Flags,
	val jvmFieldSignature: MJvmMemberSignature.Field?,
	val jvmGetterSignature: MJvmMemberSignature.Method?,
	val jvmSetterSignature: MJvmMemberSignature.Method?,
	val jvmSyntheticMethodForAnnotationsSignature: MJvmMemberSignature.Method?,
	val name: MVariableName,
	val receiverParameter: MTypeReference?,
	val returnType: MTypeReference,
	private val setterFlags: Flags,
	val setterParameter: MValueParameter?,
	val typeParameters: List<MTypeParameter>,
	val versionRequirement: MVersionRequirement?
) {

	val getterIsExternal
		get() = Flag.PropertyAccessor.IS_EXTERNAL(getterFlags)

	val getterIsInline
		get() = Flag.PropertyAccessor.IS_INLINE(getterFlags)

	val getterIsNotDefault
		get() = Flag.PropertyAccessor.IS_NOT_DEFAULT(getterFlags)

	val hasAnnotations
		get() = Flag.HAS_ANNOTATIONS(flags)

	val hasConstant
		get() = Flag.Property.HAS_CONSTANT(flags)

	val hasGetter
		get() = Flag.Property.HAS_GETTER(flags)

	val hasSetter
		get() = Flag.Property.HAS_SETTER(flags)

	val isConst
		get() = Flag.Property.IS_CONST(flags)

	val isDelegated
		get() = Flag.Property.IS_DELEGATED(flags)

	val isExpect
		get() = Flag.Property.IS_EXPECT(flags)

	val isExternal
		get() = Flag.Property.IS_EXTERNAL(flags)

	val isLateinit
		get() = Flag.Property.IS_LATEINIT(flags)

	val isVar
		get() = Flag.Property.IS_VAR(flags)

	val kind = when {
		Flag.Property.IS_DECLARATION(flags) -> Kind.DECLARATION
		Flag.Property.IS_FAKE_OVERRIDE(flags) -> Kind.FAKE_OVERRIDE
		Flag.Property.IS_DELEGATION(flags) -> Kind.DECLARATION
		Flag.Property.IS_SYNTHESIZED(flags) -> Kind.SYNTHESIZED
		else -> throw MetaException("Property '$name' has an unsupported kind (flags: $flags)")
	}

	val modality = MModality.forFlags(flags)

	val setterIsExternal
		get() = Flag.PropertyAccessor.IS_EXTERNAL(setterFlags)

	val setterIsInline
		get() = Flag.PropertyAccessor.IS_INLINE(setterFlags)

	val setterIsNotDefault
		get() = Flag.PropertyAccessor.IS_NOT_DEFAULT(setterFlags)

	val visibility = MVisibility.forFlags(flags)


	override fun equals(other: Any?): Boolean {
		if (other === this) return true
		if (other !is MProperty) return false

		return flags == other.flags &&
			getterFlags == other.getterFlags &&
			jvmFieldSignature == other.jvmFieldSignature &&
			jvmGetterSignature == other.jvmGetterSignature &&
			jvmSetterSignature == other.jvmSetterSignature &&
			jvmSyntheticMethodForAnnotationsSignature == other.jvmSyntheticMethodForAnnotationsSignature &&
			name == other.name &&
			receiverParameter == other.receiverParameter &&
			returnType == other.returnType &&
			setterFlags == other.setterFlags &&
			setterParameter == other.setterParameter &&
			typeParameters == other.typeParameters &&
			versionRequirement == other.versionRequirement
	}


	override fun hashCode() =
		Objects.hash(
			flags,
			getterFlags,
			jvmFieldSignature,
			jvmGetterSignature,
			jvmSetterSignature,
			jvmSyntheticMethodForAnnotationsSignature,
			name,
			receiverParameter,
			returnType,
			setterFlags,
			setterParameter,
			typeParameters,
			versionRequirement
		)


	override fun toString() = typeToString(
		"name" to name,
		"getterIsExternal" to getterIsExternal,
		"getterIsInline" to getterIsInline,
		"getterIsNotDefault" to getterIsNotDefault,
		"hasAnnotations" to hasAnnotations,
		"hasConstant" to hasConstant,
		"hasGetter" to hasGetter,
		"hasSetter" to hasSetter,
		"isConst" to isConst,
		"isDelegated" to isDelegated,
		"isExpect" to isExpect,
		"isExternal" to isExternal,
		"isLateinit" to isLateinit,
		"isVar" to isVar,
		"jvmFieldSignature" to jvmFieldSignature,
		"jvmGetterSignature" to jvmGetterSignature,
		"jvmSetterSignature" to jvmSetterSignature,
		"jvmSyntheticMethodForAnnotationsSignature" to jvmSyntheticMethodForAnnotationsSignature,
		"kind" to kind,
		"modality" to modality,
		"receiverParameter" to receiverParameter,
		"returnType" to returnType,
		"setterIsExternal" to setterIsExternal,
		"setterIsInline" to setterIsInline,
		"setterIsNotDefault" to setterIsNotDefault,
		"setterParameter" to setterParameter,
		"typeParameters" to typeParameters,
		"visibility" to visibility,
		"versionRequirement" to versionRequirement
	)


	@Suppress("EnumEntryName")
	enum class Kind {

		DECLARATION,
		DELEGATION,
		FAKE_OVERRIDE,
		SYNTHESIZED;


		override fun toString() =
			when (this) {
				DECLARATION -> "declaration"
				DELEGATION -> "delegation"
				FAKE_OVERRIDE -> "fake override"
				SYNTHESIZED -> "synthesized"
			}


		companion object
	}
}
