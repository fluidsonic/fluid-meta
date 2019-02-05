package com.github.fluidsonic.fluid.meta

import kotlinx.metadata.Flag
import kotlinx.metadata.Flags
import java.util.Objects


class MEffectExpression internal constructor(
	val andArguments: List<MEffectExpression>,
	val constantValue: Any?,
	private val flags: Flags,
	val instanceType: MTypeReference?,
	val orArguments: List<MEffectExpression>,
	val parameterIndex: MValueParameterIndex?
) {

	override fun equals(other: Any?): Boolean {
		if (other === this) return true
		if (other !is MEffectExpression) return false

		return andArguments == other.andArguments &&
			constantValue == other.constantValue &&
			flags == other.flags &&
			instanceType == other.instanceType &&
			orArguments == other.orArguments &&
			parameterIndex == other.parameterIndex
	}


	val isNegated
		get() = Flag.EffectExpression.IS_NEGATED(flags)


	val isNullCheckPredicate
		get() = Flag.EffectExpression.IS_NULL_CHECK_PREDICATE(flags)


	override fun hashCode() =
		Objects.hash(
			andArguments,
			constantValue,
			flags,
			instanceType,
			orArguments,
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
