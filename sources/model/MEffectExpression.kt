package com.github.fluidsonic.fluid.meta

sealed class MEffectExpression {

	abstract val andArguments: List<MEffectExpression>
	abstract val orArguments: List<MEffectExpression>


	data class Constant(
		override val andArguments: List<MEffectExpression>,
		override val orArguments: List<MEffectExpression>,
		val value: Boolean
	) : MEffectExpression() {

		override fun toString() =
			MetaCodeWriter.write(this)
	}


	data class Empty(
		override val andArguments: List<MEffectExpression>,
		override val orArguments: List<MEffectExpression>
	) : MEffectExpression() {

		override fun toString() =
			MetaCodeWriter.write(this)
	}


	data class IsInstance(
		override val andArguments: List<MEffectExpression>,
		val instanceType: MTypeReference,
		val isNegated: Boolean,
		override val orArguments: List<MEffectExpression>,
		val parameterIndex: MValueParameterIndex
	) : MEffectExpression() {

		override fun toString() =
			MetaCodeWriter.write(this)
	}


	data class IsNull(
		override val andArguments: List<MEffectExpression>,
		val isNegated: Boolean,
		override val orArguments: List<MEffectExpression>,
		val parameterIndex: MValueParameterIndex
	) : MEffectExpression() {

		override fun toString() =
			MetaCodeWriter.write(this)
	}


	data class IsTrue(
		override val andArguments: List<MEffectExpression>,
		val isNegated: Boolean,
		override val orArguments: List<MEffectExpression>,
		val parameterIndex: MValueParameterIndex
	) : MEffectExpression() {

		override fun toString() =
			MetaCodeWriter.write(this)
	}


	companion object
}
