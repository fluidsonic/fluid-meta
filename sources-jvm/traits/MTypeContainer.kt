package io.fluidsonic.meta


/** A declaration that contains nested types. */
public interface MTypeContainer {

	/** The names of nested types declared in this container. */
	public val types: List<MTypeName>


	public companion object
}
