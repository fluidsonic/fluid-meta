package com.github.fluidsonic.fluid.meta

import kotlinx.metadata.KmAnnotationArgument
import java.util.Objects


class MAnnotation internal constructor(
	val arguments: Map<MTypeParameterName, KmAnnotationArgument<*>>, // FIXME
	val className: MTypeName
) {

	override fun equals(other: Any?): Boolean {
		if (other === this) return true
		if (other !is MAnnotation) return false

		return arguments == other.arguments &&
			className == other.className
	}


	override fun hashCode() =
		Objects.hash(
			arguments,
			className
		)


	override fun toString() = typeToString(
		"className" to className,
		"arguments" to arguments
	)


	companion object
}
