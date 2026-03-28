package io.fluidsonic.meta

import kotlin.metadata.*


/** Represents the variance of a type parameter or type argument (`in`, `out`, or invariant). */
public enum class MVariance {

	INVARIANT,
	IN,
	OUT;


	override fun toString(): String =
		MetaCodeWriter.write(this)


	public companion object
}


internal fun MVariance(variance: KmVariance): MVariance =
	when (variance) {
		KmVariance.INVARIANT -> MVariance.INVARIANT
		KmVariance.IN -> MVariance.IN
		KmVariance.OUT -> MVariance.OUT
	}
