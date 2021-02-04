package io.fluidsonic.meta


@Suppress("EqualsOrHashCode")
public data class MValueParameter(
	val declaresDefaultValue: Boolean,
	val isCrossinline: Boolean,
	val isNoinline: Boolean,
	val name: MVariableName,
	val type: MTypeReference,
	val varargElementType: MTypeReference?
) {

	override fun hashCode(): Int =
		name.hashCode()


	override fun toString(): String =
		MetaCodeWriter.write(this)


	public companion object
}


public val MValueParameter.isVariadic: Boolean
	get() = varargElementType != null
