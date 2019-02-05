package com.github.fluidsonic.fluid.meta

import kotlinx.metadata.KmContractVisitor
import kotlinx.metadata.KmEffectInvocationKind
import kotlinx.metadata.KmEffectType


internal class MContractBuilder : KmContractVisitor() {

	private var effects: MutableList<MEffectBuilder>? = null


	fun build() = MContract(
		effects = effects.mapOrEmpty { it.build() }
	)


	override fun visitEffect(type: KmEffectType, invocationKind: KmEffectInvocationKind?) =
		MEffectBuilder(
			type = when (type) {
				KmEffectType.CALLS -> MEffect.Type.CALLS
				KmEffectType.RETURNS_CONSTANT -> MEffect.Type.RETURNS_CONSTANT
				KmEffectType.RETURNS_NOT_NULL -> MEffect.Type.RETURNS_NOT_NULL
			},
			invocationKind = when (invocationKind) {
				KmEffectInvocationKind.AT_LEAST_ONCE -> MEffect.InvocationKind.AT_LEAST_ONCE
				KmEffectInvocationKind.AT_MOST_ONCE -> MEffect.InvocationKind.AT_MOST_ONCE
				KmEffectInvocationKind.EXACTLY_ONCE -> MEffect.InvocationKind.EXACTLY_ONCE
				null -> null
			}
		)
			.also {
				effects?.apply { add(it) } ?: { effects = mutableListOf(it) }()
			}
}
