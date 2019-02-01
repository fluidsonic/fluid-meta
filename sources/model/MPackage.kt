package com.github.fluidsonic.fluid.meta

import java.util.Objects


class MPackage internal constructor(
	val fileFacades: List<MTypeName>,
	val multiFileClassParts: Map<MTypeName, MTypeName>,
	val name: MPackageName
) {

	override fun equals(other: Any?): Boolean {
		if (other === this) return true
		if (other !is MPackage) return false

		return fileFacades == other.fileFacades &&
			multiFileClassParts == other.multiFileClassParts &&
			name == other.name
	}


	override fun hashCode() =
		Objects.hash(
			fileFacades,
			multiFileClassParts,
			name
		)


	override fun toString() = typeToString(
		"name" to if (name.raw.isEmpty()) "root package" else name,
		"fileFacades" to fileFacades,
		"multiFileClassParts" to multiFileClassParts
	)


	companion object
}
