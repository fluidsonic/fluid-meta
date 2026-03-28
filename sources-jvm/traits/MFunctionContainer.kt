package io.fluidsonic.meta


/** A declaration that contains functions. */
public interface MFunctionContainer {

	/** The functions declared in this container. */
	public val functions: List<MFunction>


	public companion object
}
