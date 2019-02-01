package com.github.fluidsonic.fluid.meta

import kotlinx.metadata.KmVariance


@Suppress("EnumEntryName")
enum class MVariance {

	INVARIANT,
	IN,
	OUT;


	override fun toString() =
		when (this) {
			INVARIANT -> "invariant"
			IN -> "in"
			OUT -> "out"
		}


	companion object
}


@Suppress("FunctionName")
internal fun MVariance(variance: KmVariance) =
	when (variance) {
		KmVariance.INVARIANT -> MVariance.INVARIANT
		KmVariance.IN -> MVariance.IN
		KmVariance.OUT -> MVariance.OUT
	}
