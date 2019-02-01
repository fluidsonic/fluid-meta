package com.github.fluidsonic.fluid.meta

import java.util.Objects


class MContract internal constructor(
	val effects: List<MEffect>
) {

	override fun equals(other: Any?): Boolean {
		if (other === this) return true
		if (other !is MContract) return false

		return effects == other.effects
	}


	override fun hashCode() =
		Objects.hash(
			effects
		)


	override fun toString() = typeToString(
		"effects" to effects
	)


	companion object
}
