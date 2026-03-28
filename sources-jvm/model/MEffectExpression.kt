package io.fluidsonic.meta


/** Represents a boolean expression used as a condition in a contract effect. */
public sealed class MEffectExpression {

	/** Sub-expressions combined with this expression using logical AND. */
	public abstract val andArguments: List<MEffectExpression>

	/** Sub-expressions combined with this expression using logical OR. */
	public abstract val orArguments: List<MEffectExpression>


	/** A constant boolean value expression. */
	public data class Constant(
		override val andArguments: List<MEffectExpression>,
		override val orArguments: List<MEffectExpression>,
		val value: Boolean
	) : MEffectExpression() {

		override fun toString(): String =
			MetaCodeWriter.write(this)
	}


	/** An empty effect expression with no specific condition. */
	public data class Empty(
		override val andArguments: List<MEffectExpression>,
		override val orArguments: List<MEffectExpression>
	) : MEffectExpression() {

		override fun toString(): String =
			MetaCodeWriter.write(this)
	}


	/** An expression checking whether a parameter is an instance of a specific type. */
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


	/** An expression checking whether a parameter is null. */
	public data class IsNull(
		override val andArguments: List<MEffectExpression>,
		val isNegated: Boolean,
		override val orArguments: List<MEffectExpression>,
		val parameterIndex: MValueParameterIndex
	) : MEffectExpression() {

		override fun toString(): String =
			MetaCodeWriter.write(this)
	}


	/** An expression checking whether a parameter is true. */
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
