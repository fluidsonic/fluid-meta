package io.fluidsonic.meta


/** A declaration that can have supertypes (classes, interfaces, enum entries). */
public interface MSupertypable {

	/** The supertypes of this declaration. */
	public val supertypes: List<MTypeReference>


	public companion object
}
