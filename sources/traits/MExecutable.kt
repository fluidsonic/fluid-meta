package com.github.fluidsonic.fluid.meta


interface MExecutable {

	val jvmSignature: MJvmMemberSignature.Method?
	val valueParameters: List<MValueParameter>


	companion object
}
