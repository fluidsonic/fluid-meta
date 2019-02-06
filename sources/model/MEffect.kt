package com.github.fluidsonic.fluid.meta

import java.util.Objects


@Suppress("EqualsOrHashCode")
data class MEffect(
	val conclusionOfConditionalEffect: MEffectExpression?,
	val constructorArguments: List<MEffectExpression>,
	val invocationKind: InvocationKind?,
	val type: Type
) {

	override fun hashCode() =
		Objects.hash(
			invocationKind,
			type
		)


	override fun toString() = typeToString(
		"conclusionOfConditionalEffect" to conclusionOfConditionalEffect,
		"constructorArguments" to constructorArguments,
		"invocationKind" to invocationKind,
		"type" to type
	)


	companion object;


	enum class InvocationKind {

		AT_LEAST_ONCE,
		AT_MOST_ONCE,
		EXACTLY_ONCE;


		companion object;
	}


	enum class Type {

		CALLS,
		RETURNS_CONSTANT,
		RETURNS_NOT_NULL;


		companion object;
	}
}
