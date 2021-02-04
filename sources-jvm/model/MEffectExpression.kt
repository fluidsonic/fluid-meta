package io.fluidsonic.meta


public sealed class MEffectExpression {

	public abstract val andArguments: List<MEffectExpression>
	public abstract val orArguments: List<MEffectExpression>


	public data class Constant(
		override val andArguments: List<MEffectExpression>,
		override val orArguments: List<MEffectExpression>,
		val value: Boolean
	) : MEffectExpression() {

		override fun toString(): String =
			MetaCodeWriter.write(this)
	}


	public data class Empty(
		override val andArguments: List<MEffectExpression>,
		override val orArguments: List<MEffectExpression>
	) : MEffectExpression() {

		override fun toString(): String =
			MetaCodeWriter.write(this)
	}


	public data class IsInstance(
		override val andArguments: List<MEffectExpression>,
		val instanceType: MTypeReference,
		val isNegated: Boolean,
		override val orArguments: List<MEffectExpression>,
		val parameterIndex: MValueParameterIndex
	) : MEffectExpression() {

		override fun toString(): String =
			MetaCodeWriter.write(this)
	}


	public data class IsNull(
		override val andArguments: List<MEffectExpression>,
		val isNegated: Boolean,
		override val orArguments: List<MEffectExpression>,
		val parameterIndex: MValueParameterIndex
	) : MEffectExpression() {

		override fun toString(): String =
			MetaCodeWriter.write(this)
	}


	public data class IsTrue(
		override val andArguments: List<MEffectExpression>,
		val isNegated: Boolean,
		override val orArguments: List<MEffectExpression>,
		val parameterIndex: MValueParameterIndex
	) : MEffectExpression() {

		override fun toString(): String =
			MetaCodeWriter.write(this)
	}


	public companion object
}
