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
			type = type,
			invocationKind = invocationKind
		)
			.also {
				effects?.apply { add(it) } ?: { effects = mutableListOf(it) }()
			}
}
