package io.fluidsonic.meta


public sealed class MEffect {

	public data class CallsInPlace(
		val invocationKind: InvocationKind,
		val parameterIndex: MValueParameterIndex
	) : MEffect() {

		override fun toString(): String =
			MetaCodeWriter.write(this)
	}


	public data class Returns(
		val condition: MEffectExpression?,
		val returnValue: ReturnValue?
	) : MEffect() {

		override fun toString(): String =
			MetaCodeWriter.write(this)
	}


	public companion object;


	public enum class InvocationKind {

		AT_LEAST_ONCE,
		AT_MOST_ONCE,
		EXACTLY_ONCE,
		UNKNOWN;


		override fun toString(): String =
			MetaCodeWriter.write(this)


		public companion object
	}


	public enum class ReturnValue {

		FALSE,
		NOT_NULL,
		NULL,
		TRUE;


		public companion object
	}
}
