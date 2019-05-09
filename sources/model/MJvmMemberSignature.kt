package com.github.fluidsonic.fluid.meta

import kotlinx.metadata.jvm.*


sealed class MJvmMemberSignature {

	abstract val name: String
	abstract val typeEncoding: String

	abstract override fun toString(): String


	companion object;


	data class Field(
		override val name: String,
		override val typeEncoding: String
	) : MJvmMemberSignature() {

		override fun toString() = "$name:$typeEncoding"
	}


	data class Method(
		override val name: String,
		override val typeEncoding: String
	) : MJvmMemberSignature() {

		override fun toString() = "$name$typeEncoding"
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
