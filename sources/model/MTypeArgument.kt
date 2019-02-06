package com.github.fluidsonic.fluid.meta


sealed class MTypeArgument {

	object StarProjection : MTypeArgument() {

		override fun toString() = "*"
	}


	data class Type(
		val type: MTypeReference,
		val variance: MVariance
	) : MTypeArgument() {

		override fun toString() = typeToString(
			"type" to type,
			"variance" to variance
		)


		companion object
	}


	companion object
}
