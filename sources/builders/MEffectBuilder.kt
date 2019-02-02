package com.github.fluidsonic.fluid.meta

import kotlinx.metadata.KmEffectVisitor


internal class MEffectBuilder(
	private val invocationKind: MEffect.InvocationKind?,
	private val type: MEffect.Type
) : KmEffectVisitor() {

	private var conclusionOfConditionalEffect: MEffectExpressionBuilder? = null
	private var contructorArguments: MutableList<MEffectExpressionBuilder>? = null


	fun build() = MEffect(
		conclusionOfConditionalEffect = conclusionOfConditionalEffect?.build(),
		contructorArguments = contructorArguments.mapOrEmpty { it.build() },
		invocationKind = invocationKind,
		type = type
	)


	override fun visitConclusionOfConditionalEffect() =
		MEffectExpressionBuilder()
			.also { conclusionOfConditionalEffect = it }


	override fun visitConstructorArgument() =
		MEffectExpressionBuilder()
			.also {
				contructorArguments?.apply { add(it) } ?: { contructorArguments = mutableListOf(it) }()
			}
}
