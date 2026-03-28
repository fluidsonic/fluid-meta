package io.fluidsonic.meta


/** A declaration that can be invoked with value parameters (function, constructor, or property accessor). */
public interface MExecutable {

	/** The JVM method signature, or `null` if not available. */
	public val jvmSignature: MJvmMemberSignature.Method?

	/** The value parameters of this executable declaration. */
	public val valueParameters: List<MValueParameter>


	public companion object
}
