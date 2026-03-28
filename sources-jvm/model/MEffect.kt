package io.fluidsonic.meta


/** Represents an effect declared in a Kotlin contract. */
public sealed class MEffect {

	/** Declares that a function parameter is called in place with a specific invocation kind. */
	public data class CallsInPlace(
		val invocationKind: InvocationKind,
		val parameterIndex: MValueParameterIndex
	) : MEffect() {

		override fun toString(): String =
			MetaCodeWriter.write(this)
	}


	/** Declares that a function returns a specific value, optionally under a condition. */
	public data class Returns(
		val condition: MEffectExpression?,
		val returnValue: ReturnValue?
	) : MEffect() {

		override fun toString(): String =
			MetaCodeWriter.write(this)
	}


	public companion object;


	/** Describes how many times a lambda parameter is invoked. */
	public enum class InvocationKind {

		AT_LEAST_ONCE,
		AT_MOST_ONCE,
		EXACTLY_ONCE,
		UNKNOWN;


		override fun toString(): String =
			MetaCodeWriter.write(this)


		public companion object
	}


	/** Represents the return value in a `returns` contract effect. */
	public enum class ReturnValue {

		FALSE,
		NOT_NULL,
		NULL,
		TRUE;


		public companion object
	}
}
