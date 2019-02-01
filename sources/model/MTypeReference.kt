package com.github.fluidsonic.fluid.meta

import kotlinx.metadata.Flag
import kotlinx.metadata.Flags
import java.util.Objects


sealed class MTypeReference {

	abstract val annotations: List<MAnnotation>
	abstract val arguments: List<MTypeArgument>
	abstract val flexibilityTypeUpperBound: MFlexibilityTypeUpperBound?
	abstract val hasAnnotations: Boolean
	abstract val isNullable: Boolean
	abstract val isRaw: Boolean


	companion object
}


class MClassReference internal constructor(
	override val annotations: List<MAnnotation>,
	override val arguments: List<MTypeArgument>,
	private val flags: Flags,
	override val flexibilityTypeUpperBound: MFlexibilityTypeUpperBound?,
	override val isRaw: Boolean,
	val name: MTypeName,
	val outerType: MTypeReference?
) : MTypeReference() {

	override val hasAnnotations
		get() = Flag.HAS_ANNOTATIONS(flags)

	override val isNullable
		get() = Flag.Type.IS_NULLABLE(flags)

	val isSuspend
		get() = Flag.Type.IS_SUSPEND(flags)


	override fun equals(other: Any?): Boolean {
		if (other === this) return true
		if (other !is MClassReference) return false

		return annotations == other.annotations &&
			arguments == other.arguments &&
			flags == other.flags &&
			flexibilityTypeUpperBound == other.flexibilityTypeUpperBound &&
			isRaw == other.isRaw &&
			name == other.name &&
			outerType == other.outerType
	}


	override fun hashCode() =
		Objects.hash(
			annotations,
			arguments,
			flags,
			flexibilityTypeUpperBound,
			isRaw,
			name,
			outerType
		)


	override fun toString() = typeToString(
		"name" to name,
		"annotations" to annotations,
		"arguments" to arguments,
		"flexibilityTypeUpperBound" to flexibilityTypeUpperBound,
		"hasAnnotations" to hasAnnotations,
		"isNullable" to isNullable,
		"isRaw" to isRaw,
		"isSuspend" to isSuspend,
		"outerType" to outerType
	)

	companion object
}


class MTypeAliasReference internal constructor(
	override val annotations: List<MAnnotation>,
	override val arguments: List<MTypeArgument>,
	private val flags: Flags,
	override val flexibilityTypeUpperBound: MFlexibilityTypeUpperBound?,
	override val isRaw: Boolean,
	val name: MTypeName
) : MTypeReference() {

	override val hasAnnotations
		get() = Flag.HAS_ANNOTATIONS(flags)

	override val isNullable
		get() = Flag.Type.IS_NULLABLE(flags)


	override fun equals(other: Any?): Boolean {
		if (other === this) return true
		if (other !is MTypeAliasReference) return false

		return annotations == other.annotations &&
			arguments == other.arguments &&
			flags == other.flags &&
			flexibilityTypeUpperBound == other.flexibilityTypeUpperBound &&
			isRaw == other.isRaw &&
			name == other.name
	}


	override fun hashCode() =
		Objects.hash(
			annotations,
			arguments,
			flags,
			flexibilityTypeUpperBound,
			isRaw,
			name
		)


	override fun toString() = typeToString(
		"name" to name,
		"annotations" to annotations,
		"arguments" to arguments,
		"flexibilityTypeUpperBound" to flexibilityTypeUpperBound,
		"hasAnnotations" to hasAnnotations,
		"isNullable" to isNullable,
		"isRaw" to isRaw
	)


	companion object
}


class MTypeParameterReference internal constructor(
	override val annotations: List<MAnnotation>,
	override val arguments: List<MTypeArgument>,
	private val flags: Flags,
	override val flexibilityTypeUpperBound: MFlexibilityTypeUpperBound?,
	val id: MTypeParameterId,
	override val isRaw: Boolean
) : MTypeReference() {

	override val hasAnnotations
		get() = Flag.HAS_ANNOTATIONS(flags)

	override val isNullable
		get() = Flag.Type.IS_NULLABLE(flags)


	override fun equals(other: Any?): Boolean {
		if (other === this) return true
		if (other !is MTypeParameterReference) return false

		return annotations == other.annotations &&
			arguments == other.arguments &&
			flags == other.flags &&
			flexibilityTypeUpperBound == other.flexibilityTypeUpperBound &&
			id == other.id &&
			isRaw == other.isRaw
	}


	override fun hashCode() =
		Objects.hash(
			annotations,
			arguments,
			flags,
			flexibilityTypeUpperBound,
			id,
			isRaw
		)


	override fun toString() = typeToString(
		"id" to id,
		"annotations" to annotations,
		"arguments" to arguments,
		"flexibilityTypeUpperBound" to flexibilityTypeUpperBound,
		"hasAnnotations" to hasAnnotations,
		"isNullable" to isNullable,
		"isRaw" to isRaw
	)


	companion object
}
