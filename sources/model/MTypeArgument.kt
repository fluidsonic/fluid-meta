package io.fluidsonic.meta


sealed class MTypeArgument {

	object StarProjection : MTypeArgument() {

		override fun toString() =
			MetaCodeWriter.write(this)
	}


	data class Type(
		val type: MTypeReference,
		val variance: MVariance
	) : MTypeArgument() {


		override fun toString() =
			MetaCodeWriter.write(this)


		companion object
	}


	companion object
}


internal fun MTypeArgument?.equalsExceptForNullability(other: MTypeArgument?): Boolean {
	if (this === other) return true
	if (this !is MTypeArgument.Type || other !is MTypeArgument.Type) return false

	return type.equalsExceptForNullability(other.type)
}
