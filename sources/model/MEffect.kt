package com.github.fluidsonic.fluid.meta

import java.util.Objects


class MEffect internal constructor(
	val conclusionOfConditionalEffect: MEffectExpression?,
	val contructorArguments: List<MEffectExpression>,
	val invocationKind: InvocationKind?,
	val type: Type
) {

	override fun equals(other: Any?): Boolean {
		if (other === this) return true
		if (other !is MEffect) return false

		return conclusionOfConditionalEffect == other.conclusionOfConditionalEffect &&
			contructorArguments == other.contructorArguments &&
			invocationKind == other.invocationKind &&
			type == other.type
	}


	override fun hashCode() =
		Objects.hash(
			conclusionOfConditionalEffect,
			contructorArguments,
			invocationKind,
			type
		)

	override fun toString() = typeToString(
		"conclusionOfConditionalEffect" to conclusionOfConditionalEffect,
		"contructorArguments" to contructorArguments,
		"invocationKind" to invocationKind,
		"type" to type
	)


	companion object;


	enum class InvocationKind {

		AT_LEAST_ONCE,
		AT_MOST_ONCE,
		EXACTLY_ONCE
	}


	enum class Type {

		CALLS,
		RETURNS_CONSTANT,
		RETURNS_NOT_NULL
	}
}
