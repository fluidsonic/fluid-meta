package com.github.fluidsonic.fluid.meta


class MEffectExpression internal constructor() {

	override fun equals(other: Any?): Boolean {
		if (other === this) return true
		if (other !is MEffectExpression) return false

		return true
	}


	override fun hashCode() =
		super.hashCode()


	override fun toString() = typeToString()


	companion object
}
