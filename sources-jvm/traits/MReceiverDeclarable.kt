package io.fluidsonic.meta


/** A declaration that can have a receiver parameter (extension function or property). */
public interface MReceiverDeclarable {

	/** The type of the receiver parameter, or `null` if this is not an extension declaration. */
	public val receiverParameterType: MTypeReference?


	public companion object
}
