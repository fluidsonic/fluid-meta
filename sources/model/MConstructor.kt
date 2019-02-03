package com.github.fluidsonic.fluid.meta

import kotlinx.metadata.Flag
import kotlinx.metadata.Flags
import java.util.Objects


class MConstructor internal constructor(
	private val flags: Flags,
	val jvmSignature: MJvmMemberSignature.Method?,
	val valueParameters: List<MValueParameter>,
	val versionRequirement: MVersionRequirement?
) {

	val isPrimary
		get() = Flag.Constructor.IS_PRIMARY(flags)

	val modality = MModality.forFlags(flags)

	val visibility = MVisibility.forFlags(flags)


	override fun equals(other: Any?): Boolean {
		if (other === this) return true
		if (other !is MConstructor) return false

		return flags == other.flags &&
			jvmSignature == other.jvmSignature &&
			valueParameters == other.valueParameters &&
			versionRequirement == other.versionRequirement
	}


	override fun hashCode() =
		Objects.hash(
			flags,
			jvmSignature,
			valueParameters,
			versionRequirement
		)


	override fun toString() = typeToString(
		"isPrimary" to isPrimary,
		"jvmSignature" to jvmSignature,
		"modality" to modality,
		"valueParameters" to valueParameters,
		"visibility" to visibility,
		"versionRequirement" to versionRequirement
	)


	companion object
}
