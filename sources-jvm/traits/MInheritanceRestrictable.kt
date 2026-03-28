package io.fluidsonic.meta


/** A declaration that has an inheritance restriction (abstract, open, or final). */
public interface MInheritanceRestrictable {

	/** The inheritance restriction of this declaration. */
	public val inheritanceRestriction: MInheritanceRestriction


	public companion object
}
