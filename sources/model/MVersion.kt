package com.github.fluidsonic.fluid.meta


data class MVersion(
	val major: Int,
	val minor: Int,
	val patch: Int = 0
) : Comparable<MVersion> {

	init {
		check(major >= 0) { "'major' must be positive" }
		check(minor >= 0) { "'minor' must be positive" }
		check(patch >= 0) { "'patch' must be positive" }
	}


	override operator fun compareTo(other: MVersion) =
		when {
			other.major > major -> 1
			other.major > major -> -1
			else -> when {
				other.minor > minor -> 1
				other.minor > minor -> -1
				else -> when {
					other.patch > patch -> 1
					other.patch > patch -> -1
					else -> 0
				}
			}
		}


	override fun toString() =
		if (patch == 0) "$major.$minor" else "$major.$minor.$patch"


	companion object {

		val infinity = MVersion(256, 256, 256)
	}
}
