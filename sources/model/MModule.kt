package com.github.fluidsonic.fluid.meta


data class MModule(
	override val annotations: List<MAnnotation>,
	val packages: List<MPackage>
) : MAnnotatable {

	override fun toString() = typeToString(
		"annotations" to annotations,
		"packages" to packages
	)


	companion object
}
