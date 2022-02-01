package io.fluidsonic.meta

import kotlinx.metadata.*


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
				effects?.apply { add(it) } ?: run {
					effects = mutableListOf(it)
				}
			}
}
