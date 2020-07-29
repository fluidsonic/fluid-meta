package io.fluidsonic.meta


@Suppress("EqualsOrHashCode")
public data class MTypeParameter(
	override val annotations: List<MAnnotation>,
	val isReified: Boolean,
	val id: MTypeParameterId,
	val name: MTypeParameterName,
	val upperBounds: List<MTypeReference>,
	val variance: MVariance
) : MAnnotatable {

	override fun hashCode(): Int =
		name.hashCode()


	override fun toString(): String =
		MetaCodeWriter.write(this)


	public companion object
}
