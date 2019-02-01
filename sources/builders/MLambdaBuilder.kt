package com.github.fluidsonic.fluid.meta

import kotlinx.metadata.Flags
import kotlinx.metadata.KmLambdaVisitor


internal class MLambdaBuilder : KmLambdaVisitor() {

	private var function: MFunctionBuilder? = null


	fun build() = MLambda(
		function = function?.build() ?: throw MetadataException("Lambda has no function")
	)


	override fun visitFunction(flags: Flags, name: String) =
		MFunctionBuilder(flags = flags, name = MFunctionName(name))
			.also { function = it }
}
