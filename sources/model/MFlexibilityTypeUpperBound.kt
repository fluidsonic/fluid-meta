package com.github.fluidsonic.fluid.meta

import java.util.Objects


class MFlexibilityTypeUpperBound internal constructor(
	val type: MTypeReference,
	val typeFlexibilityId: MTypeFlexibilityId?
) {

	override fun equals(other: Any?): Boolean {
		if (other === this) return true
		if (other !is MFlexibilityTypeUpperBound) return false

		return type == other.type &&
			typeFlexibilityId == other.typeFlexibilityId
	}


	override fun hashCode() =
		Objects.hash(
			type,
			typeFlexibilityId
		)


	override fun toString() = typeToString(
		"type" to type,
		"typeFlexibilityId" to typeFlexibilityId
	)


	companion object
}
