package io.fluidsonic.meta


/** A declaration that can have annotations. */
public interface MAnnotatable {

	/** The annotations applied to this declaration. */
	public val annotations: List<MAnnotation>


	public companion object
}
