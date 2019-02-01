package com.github.fluidsonic.fluid.meta

import java.util.Objects


class MModule internal constructor(
	val annotations: List<MAnnotation>,
	val packages: List<MPackage>
) {

	override fun equals(other: Any?): Boolean {
		if (other === this) return true
		if (other !is MModule) return false

		return annotations == other.annotations &&
			packages == other.packages
	}


	override fun hashCode() =
		Objects.hash(
			annotations,
			packages
		)


	override fun toString() = typeToString(
		"annotations" to annotations,
		"packages" to packages
	)
}
