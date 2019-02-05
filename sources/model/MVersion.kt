package com.github.fluidsonic.fluid.meta


data class MVersion(
	val major: Int,
	val minor: Int,
	val patch: Int = 0
) : Comparable<MVersion> {

	init {
		if (major != 256 || minor != 256 || patch != 256) {
			check(major in 0 .. 255) { "'major' must be in range 0 .. 255" }
			check(minor in 0 .. 255) { "'minor' must be in range 0 .. 255" }
			check(patch in 0 .. 255) { "'patch' must be in range 0 .. 255" }
		}
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
		when {
			patch == 0 -> "$major.$minor"
			major == 256 && minor == 256 && patch == 256 -> "infinity"
			else -> "$major.$minor.$patch"
		}


	companion object {

		val infinity = MVersion(256, 256, 256)
	}
}
