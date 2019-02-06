package com.github.fluidsonic.fluid.meta


sealed class MTypeReference : MAnnotatable {

	abstract val arguments: List<MTypeArgument>
	abstract val flexibleTypeUpperBound: MFlexibleTypeUpperBound?
	abstract val isNullable: Boolean
	abstract val isRaw: Boolean


	companion object
}


val MTypeReference.name
	get() = when (this) {
		is MClassReference -> name
		is MTypeAliasReference -> name
		is MTypeParameterReference -> null
	}


data class MClassReference(
	val abbreviatedType: MTypeReference?,
	override val annotations: List<MAnnotation>,
	override val arguments: List<MTypeArgument>,
	override val flexibleTypeUpperBound: MFlexibleTypeUpperBound?,
	override val isNullable: Boolean,
	val isSuspend: Boolean,
	override val isRaw: Boolean,
	val name: MQualifiedTypeName,
	val outerType: MTypeReference?
) : MTypeReference() {

	override fun toString() = typeToString(
		"name" to name,
		"abbreviatedType" to abbreviatedType,
		"annotations" to annotations,
		"arguments" to arguments,
		"flexibleTypeUpperBound" to flexibleTypeUpperBound,
		"isNullable" to isNullable,
		"isRaw" to isRaw,
		"isSuspend" to isSuspend,
		"outerType" to outerType
	)

	companion object
}


data class MTypeAliasReference(
	val abbreviatedType: MTypeReference?,
	override val annotations: List<MAnnotation>,
	override val arguments: List<MTypeArgument>,
	override val flexibleTypeUpperBound: MFlexibleTypeUpperBound?,
	override val isNullable: Boolean,
	override val isRaw: Boolean,
	val name: MQualifiedTypeName
) : MTypeReference() {

	override fun toString() = typeToString(
		"name" to name,
		"abbreviatedType" to abbreviatedType,
		"annotations" to annotations,
		"arguments" to arguments,
		"flexibleTypeUpperBound" to flexibleTypeUpperBound,
		"isNullable" to isNullable,
		"isRaw" to isRaw
	)


	companion object
}


data class MTypeParameterReference(
	override val annotations: List<MAnnotation>,
	override val arguments: List<MTypeArgument>,
	override val flexibleTypeUpperBound: MFlexibleTypeUpperBound?,
	val id: MTypeParameterId,
	override val isRaw: Boolean,
	override val isNullable: Boolean
) : MTypeReference() {

	override fun toString() = typeToString(
		"id" to id,
		"annotations" to annotations,
		"arguments" to arguments,
		"flexibleTypeUpperBound" to flexibleTypeUpperBound,
		"isNullable" to isNullable,
		"isRaw" to isRaw
	)


	companion object
}
