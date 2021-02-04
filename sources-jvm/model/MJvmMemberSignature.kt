package io.fluidsonic.meta

import kotlinx.metadata.jvm.*


public sealed class MJvmMemberSignature {

	public abstract val name: String
	public abstract val typeEncoding: String

	abstract override fun toString(): String


	public companion object;


	public data class Field(
		override val name: String,
		override val typeEncoding: String
	) : MJvmMemberSignature() {

		override fun toString(): String = "$name:$typeEncoding"
	}


	public data class Method(
		override val name: String,
		override val typeEncoding: String
	) : MJvmMemberSignature() {

		override fun toString(): String = "$name$typeEncoding"
	}
}


@Suppress("FunctionName")
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
		typeEncoding = signature.desc
	)


@Suppress("FunctionName")
internal fun MJvmMemberSignature(signature: JvmMethodSignature) =
	MJvmMemberSignature.Method(
		name = signature.name,
		typeEncoding = signature.desc
	)
