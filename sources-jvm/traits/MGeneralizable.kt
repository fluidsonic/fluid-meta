package io.fluidsonic.meta


/** A declaration that can have type parameters (generics). */
public interface MGeneralizable {

	/** The type parameters of this generic declaration. */
	public val typeParameters: List<MTypeParameter>


	public companion object
}
