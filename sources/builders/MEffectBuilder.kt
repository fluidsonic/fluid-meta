package io.fluidsonic.meta

import io.fluidsonic.meta.MEffect.*
import kotlinx.metadata.*


internal class MEffectBuilder(
	private val invocationKind: KmEffectInvocationKind?,
	private val type: KmEffectType
) : KmEffectVisitor() {

	private var constructorArgumentBuilder: MEffectExpressionBuilder? = null
	private var conditionBuilder: MEffectExpressionBuilder? = null


	fun build() = when (type) {
		KmEffectType.CALLS -> CallsInPlace(
			invocationKind = when (invocationKind) {
				KmEffectInvocationKind.AT_LEAST_ONCE -> InvocationKind.AT_LEAST_ONCE
				KmEffectInvocationKind.AT_MOST_ONCE -> InvocationKind.AT_MOST_ONCE
				KmEffectInvocationKind.EXACTLY_ONCE -> InvocationKind.EXACTLY_ONCE
				null -> InvocationKind.UNKNOWN
			},
			parameterIndex = constructorArgumentBuilder?.parameterIndex ?: error("callsInPlace() effect is missing a parameter index")
		)

		KmEffectType.RETURNS_CONSTANT -> Returns(
			condition = conditionBuilder?.build(),
			returnValue = constructorArgumentBuilder?.let { builder ->
				when (val returnValue = builder.constantValue) {
					false -> ReturnValue.FALSE
					null -> ReturnValue.NULL
					true -> ReturnValue.TRUE
					else -> error("returns() effect has unexpected return value $returnValue")
				}
			}
		)

		KmEffectType.RETURNS_NOT_NULL -> Returns(
			condition = conditionBuilder?.build(),
			returnValue = ReturnValue.NOT_NULL
		)
	}


	override fun visitConclusionOfConditionalEffect() =
		MEffectExpressionBuilder()
			.also {
				check(conditionBuilder == null) { "effect unexpectedly has multiple condition expressions" }
				conditionBuilder = it
			}


	override fun visitConstructorArgument() =
		MEffectExpressionBuilder()
			.also {
				check(constructorArgumentBuilder == null) { "effect unexpectedly has multiple constructor arguments" }
				constructorArgumentBuilder = it
			}
}
