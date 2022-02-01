package io.fluidsonic.meta

import kotlinx.metadata.*


internal class MEffectExpressionBuilder : KmEffectExpressionVisitor() {

	var andArgumentBuilders: MutableList<MEffectExpressionBuilder>? = null
		private set

	var constantValue: Any? = null
		private set

	var flags: Flags = 0
		private set

	var instanceTypeBuilder: MTypeReferenceBuilder? = null
		private set

	var orArgumentBuilders: MutableList<MEffectExpressionBuilder>? = null
		private set

	var parameterIndex: MValueParameterIndex? = null
		private set


	fun build(): MEffectExpression {
		val andArguments = andArgumentBuilders.mapOrEmpty { it.build() }
		val constantValue = constantValue
		val instanceTypeBuilder = instanceTypeBuilder
		val isNegated = Flag.EffectExpression.IS_NEGATED(flags)
		val orArguments = orArgumentBuilders.mapOrEmpty { it.build() }
		val parameterIndex = parameterIndex

		return when {
			Flag.EffectExpression.IS_NULL_CHECK_PREDICATE(flags) -> MEffectExpression.IsNull(
				andArguments = andArguments,
				isNegated = isNegated,
				orArguments = orArguments,
				parameterIndex = parameterIndex ?: error("is-null effect expression is missing a parameter index")
			)

			instanceTypeBuilder != null -> MEffectExpression.IsInstance(
				andArguments = andArguments,
				instanceType = instanceTypeBuilder.build(),
				isNegated = isNegated,
				orArguments = orArguments,
				parameterIndex = parameterIndex ?: error("is-instance effect expression is missing a parameter index")
			)

			parameterIndex != null -> MEffectExpression.IsTrue(
				andArguments = andArguments,
				isNegated = isNegated,
				orArguments = orArguments,
				parameterIndex = parameterIndex
			)

			constantValue is Boolean -> MEffectExpression.Constant(
				andArguments = andArguments,
				orArguments = orArguments,
				value = constantValue
			)

			constantValue == null -> MEffectExpression.Empty(
				andArguments = andArguments,
				orArguments = orArguments
			)

			else ->
				error("contract effect expression has unexpected constant value $constantValue")
		}
	}


	override fun visit(flags: Flags, parameterIndex: Int?) {
		this.flags = flags
		this.parameterIndex = parameterIndex?.let(::MValueParameterIndex)
	}


	override fun visitAndArgument() =
		MEffectExpressionBuilder().also {
			andArgumentBuilders?.apply { add(it) } ?: run {
				andArgumentBuilders = mutableListOf(it)
			}
		}


	override fun visitConstantValue(value: Any?) {
		this.constantValue = value
	}


	override fun visitIsInstanceType(flags: Flags) =
		MTypeReferenceBuilder(flags = flags)
			.also { instanceTypeBuilder = it }


	override fun visitOrArgument() =
		MEffectExpressionBuilder().also {
			orArgumentBuilders?.apply { add(it) } ?: run {
				orArgumentBuilders = mutableListOf(it)
			}
		}
}
