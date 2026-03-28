package io.fluidsonic.meta

import kotlin.metadata.jvm.*


/** Represents the JVM signature of a class member (field or method). */
public sealed class MJvmMemberSignature {

	/** The name of the member in the JVM bytecode. */
	public abstract val name: String

	/** The JVM type descriptor of the member. */
	public abstract val typeEncoding: String

	abstract override fun toString(): String


	public companion object;


	/** The JVM signature of a field. */
	public data class Field(
		override val name: String,
		override val typeEncoding: String
	) : MJvmMemberSignature() {

		override fun toString(): String = "$name:$typeEncoding"
	}


	/** The JVM signature of a method. */
	public data class Method(
		override val name: String,
		override val typeEncoding: String
	) : MJvmMemberSignature() {

		override fun toString(): String = "$name$typeEncoding"
	}
}


internal fun MJvmMemberSignature(signature: JvmMemberSignature) = signature.run {
	when (this) {
		is JvmFieldSignature -> MJvmMemberSignature(this)
		is JvmMethodSignature -> MJvmMemberSignature(this)
	}
}


@Suppress("FunctionName")
internal fun MJvmMemberSignature(signature: JvmFieldSignature) =
	MJvmMemberSignature.Field(
		name = signature.name,
		typeEncoding = signature.descriptor
	)


@Suppress("FunctionName")
internal fun MJvmMemberSignature(signature: JvmMethodSignature) =
	MJvmMemberSignature.Method(
		name = signature.name,
		typeEncoding = signature.descriptor
	)
