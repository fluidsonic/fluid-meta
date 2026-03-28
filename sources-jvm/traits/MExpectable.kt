package io.fluidsonic.meta


/** A declaration that can be marked as `expect` in a multiplatform project. */
public interface MExpectable {

	/** Whether this declaration is an `expect` declaration. */
	public val isExpect: Boolean


	public companion object
}
