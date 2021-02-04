package io.fluidsonic.meta


public interface MExecutable {

	public val jvmSignature: MJvmMemberSignature.Method?
	public val valueParameters: List<MValueParameter>


	public companion object
}
