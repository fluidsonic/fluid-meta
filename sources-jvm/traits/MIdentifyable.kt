package io.fluidsonic.meta


/** A declaration that has a local identity for distinguishing it within its parent scope. */
public interface MIdentifyable {

	/** The local identity of this declaration. */
	public val localId: MLocalId


	public companion object
}
