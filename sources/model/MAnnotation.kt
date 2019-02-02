package com.github.fluidsonic.fluid.meta

import kotlinx.metadata.KmAnnotation
import java.util.Objects


class MAnnotation internal constructor(
	val arguments: Map<MVariableName, MAnnotationArgument<*>>,
	val className: MQualifiedTypeName
) {

	constructor(annotation: KmAnnotation) : this(
		arguments = annotation.arguments
			.map { MVariableName(it.key) to MAnnotationArgument(it.value) }
			.toMap(),
		className = MQualifiedTypeName.fromKotlinInternal(annotation.className)
	)


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
