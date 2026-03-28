package io.fluidsonic.meta


/** Represents a type argument in a generic type reference. */
public sealed class MTypeArgument {

	/** The star projection (`*`) type argument. */
	public object StarProjection : MTypeArgument() {

		override fun toString(): String =
			MetaCodeWriter.write(this)
	}


	/** A concrete type argument with a type reference and variance. */
	public data class Type(
		val type: MTypeReference,
		val variance: MVariance
	) : MTypeArgument() {


		override fun toString(): String =
			MetaCodeWriter.write(this)


		public companion object
	}


	public companion object
}


internal fun MTypeArgument?.equalsExceptForNullability(other: MTypeArgument?): Boolean {
	if (this === other) return true
	if (this !is MTypeArgument.Type || other !is MTypeArgument.Type) return false

	return type.equalsExceptForNullability(other.type)
}
