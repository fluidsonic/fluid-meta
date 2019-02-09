package com.github.fluidsonic.fluid.meta


sealed class MEffect {

	data class CallsInPlace(
		val invocationKind: InvocationKind,
		val parameterIndex: MValueParameterIndex
	) : MEffect() {

		override fun toString() =
			MetaCodeWriter.write(this)
	}


	data class Returns(
		val condition: MEffectExpression?,
		val returnValue: ReturnValue?
	) : MEffect() {

		override fun toString() =
			MetaCodeWriter.write(this)
	}


	companion object;


	enum class InvocationKind {

		AT_LEAST_ONCE,
		AT_MOST_ONCE,
		EXACTLY_ONCE,
		UNKNOWN;


		override fun toString() =
			MetaCodeWriter.write(this)


		companion object
	}


	enum class ReturnValue {

		FALSE,
		NOT_NULL,
		NULL,
		TRUE;


		companion object
	}
}
