package io.fluidsonic.meta


interface MVersionRestrictable {

	val versionRequirements: List<MVersionRequirement>


	companion object
}
