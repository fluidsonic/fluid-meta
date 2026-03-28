package io.fluidsonic.meta


/** A declaration that can have version requirements. */
public interface MVersionRestrictable {

	/** The version requirements associated with this declaration. */
	public val versionRequirements: List<MVersionRequirement>


	public companion object
}
