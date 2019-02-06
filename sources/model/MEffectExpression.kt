package com.github.fluidsonic.fluid.meta

import java.util.Objects


@Suppress("EqualsOrHashCode")
data class MEffectExpression(
	val andArguments: List<MEffectExpression>,
	val constantValue: Any?,
	val instanceType: MTypeReference?,
	val isNegated: Boolean,
	val isNullCheckPredicate: Boolean,
	val orArguments: List<MEffectExpression>,
	val parameterIndex: MValueParameterIndex?
) {

	override fun hashCode() =
		Objects.hash(
			constantValue,
			instanceType,
			parameterIndex
		)


	override fun toString() = typeToString(
		"andArguments" to andArguments,
		"constantValue" to constantValue,
		"instanceType" to instanceType,
		"isNegated" to isNegated,
		"isNullCheckPredicate" to isNullCheckPredicate,
		"orArguments" to orArguments,
		"parameterIndex" to parameterIndex
	)


	companion object
}
