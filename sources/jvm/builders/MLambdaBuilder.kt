package io.fluidsonic.meta

import kotlinx.metadata.*


internal class MLambdaBuilder : KmLambdaVisitor() {

	private var function: MFunctionBuilder? = null


	fun build() = MLambda(
		function = function?.build() ?: throw MetaException("Lambda has no function")
	)


	override fun visitFunction(flags: Flags, name: String) =
		MFunctionBuilder(flags = flags, name = MFunctionName(name))
			.also { function = it }
}
