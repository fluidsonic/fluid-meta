package io.fluidsonic.meta


interface MExecutable {

	val jvmSignature: MJvmMemberSignature.Method?
	val valueParameters: List<MValueParameter>


	companion object
}
