package com.github.fluidsonic.fluid.meta

import kotlinx.metadata.Flag
import kotlinx.metadata.Flags
import java.util.Objects


class MFunction internal constructor(
	val contract: MContract?,
	private val flags: Flags,
	val jvmSignature: MJvmMemberSignature.Method?,
	val lambdaClassOriginName: MQualifiedTypeName?,
	val name: MFunctionName,
	val receiverParameter: MTypeReference?,
	val returnType: MTypeReference,
	val typeParameters: List<MTypeParameter>,
	val valueParameters: List<MValueParameter>,
	val versionRequirement: MVersionRequirement?
) {

	val hasAnnotations
		get() = Flag.HAS_ANNOTATIONS(flags)

	val isExpect
		get() = Flag.Function.IS_EXPECT(flags)

	val isExternal
		get() = Flag.Function.IS_EXTERNAL(flags)

	val isInfix
		get() = Flag.Function.IS_INFIX(flags)

	val isInline
		get() = Flag.Function.IS_INLINE(flags)

	val isOperator
		get() = Flag.Function.IS_OPERATOR(flags)

	val isSuspend
		get() = Flag.Function.IS_SUSPEND(flags)

	val isTailrec
		get() = Flag.Function.IS_TAILREC(flags)

	val kind = when {
		Flag.Function.IS_DECLARATION(flags) -> Kind.DECLARATION
		Flag.Function.IS_FAKE_OVERRIDE(flags) -> Kind.FAKE_OVERRIDE
		Flag.Function.IS_DELEGATION(flags) -> Kind.DECLARATION
		Flag.Function.IS_SYNTHESIZED(flags) -> Kind.SYNTHESIZED
		else -> throw MetaException("Function '$name' has an unsupported kind (flags: $flags)")
	}

	val modality = MModality.forFlags(flags)

	val visibility = MVisibility.forFlags(flags)


	override fun equals(other: Any?): Boolean {
		if (other === this) return true
		if (other !is MFunction) return false

		return contract == other.contract &&
			flags == other.flags &&
			jvmSignature == other.jvmSignature &&
			lambdaClassOriginName == other.lambdaClassOriginName &&
			name == other.name &&
			receiverParameter == other.receiverParameter &&
			returnType == other.returnType &&
			typeParameters == other.typeParameters &&
			valueParameters == other.valueParameters &&
			versionRequirement == other.versionRequirement
	}


	override fun hashCode() =
		Objects.hash(
			contract,
			flags,
			jvmSignature,
			lambdaClassOriginName,
			name,
			receiverParameter,
			returnType,
			typeParameters,
			valueParameters,
			versionRequirement
		)


	override fun toString() = typeToString(
		"name" to name,
		"contract" to contract,
		"hasAnnotations" to hasAnnotations,
		"isExpect" to isExpect,
		"isExternal" to isExternal,
		"isInfix" to isInfix,
		"isInline" to isInline,
		"isOperator" to isOperator,
		"isSuspend" to isSuspend,
		"isTailrec" to isTailrec,
		"jvmSignature" to jvmSignature,
		"kind" to kind,
		"lambdaClassOriginName" to lambdaClassOriginName,
		"modality" to modality,
		"receiverParameter" to receiverParameter,
		"returnType" to returnType,
		"typeParameters" to typeParameters,
		"valueParameters" to valueParameters,
		"visibility" to visibility,
		"versionRequirement" to versionRequirement
	)


	companion object


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
