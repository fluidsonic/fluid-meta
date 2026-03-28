package io.fluidsonic.meta


/** A declaration that is a member of a class. */
public interface MClassMember {

	/** How this member was introduced (declared, delegated, synthesized, or fake override). */
	public val source: MClassMemberSource


	public companion object
}
