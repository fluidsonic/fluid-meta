package com.github.fluidsonic.fluid.meta

import java.util.Objects


sealed class MTypeArgument {

	class Type internal constructor(
		val type: MTypeReference,
		val variance: MVariance
	) : MTypeArgument() {

		override fun equals(other: Any?): Boolean {
			if (other === this) return true
			if (other !is Type) return false

			return type == other.type &&
				variance == other.variance
		}


		override fun hashCode() =
			Objects.hash(
				type,
				variance
			)


		override fun toString() = typeToString(
			"type" to type,
			"variance" to variance
		)


		companion object
	}

	object StarProjection : MTypeArgument() {

		override fun toString() = "*"
	}


	companion object
}
