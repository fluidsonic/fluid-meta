package io.fluidsonic.meta

import kotlin.metadata.*


/** Represents a Kotlin annotation applied to a declaration. */
@Suppress("EqualsOrHashCode")
public data class MAnnotation(
	val arguments: Map<MVariableName, MAnnotationArgument<*>>,
	val className: MQualifiedTypeName
) {

	internal constructor(annotation: KmAnnotation) : this(
		arguments = annotation.arguments.entries
			.associateBy({ MVariableName(it.key) }) { MAnnotationArgument(it.value) },
		className = MQualifiedTypeName.fromKotlinInternal(annotation.className)
	)


	override fun hashCode(): Int =
		className.hashCode()


	override fun toString(): String =
		MetaCodeWriter.write(this)


	public companion object
}
