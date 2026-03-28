package io.fluidsonic.meta


/** A declaration that can have a companion object. */
public interface MCompanionable : MTypeContainer {

	/** The name of the companion object, or `null` if there is none. */
	public val companionName: MTypeName?


	public companion object
}
