package io.fluidsonic.meta

import kotlinx.metadata.*


public enum class MVariance {

	INVARIANT,
	IN,
	OUT;


	override fun toString(): String =
		MetaCodeWriter.write(this)


	public companion object
}


internal fun MVariance(variance: KmVariance) =
	when (variance) {
		KmVariance.INVARIANT -> MVariance.INVARIANT
		KmVariance.IN -> MVariance.IN
		KmVariance.OUT -> MVariance.OUT
	}
