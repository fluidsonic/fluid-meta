package com.github.fluidsonic.fluid.meta


interface MVersionRestrictable {

	val versionRequirements: List<MVersionRequirement>


	companion object
}
