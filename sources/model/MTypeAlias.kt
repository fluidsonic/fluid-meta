package com.github.fluidsonic.fluid.meta

import kotlinx.metadata.Flag
import kotlinx.metadata.Flags
import java.util.Objects


class MTypeAlias internal constructor(
	val annotations: List<MAnnotation>,
	val expandedType: MTypeReference,
	private val flags: Flags,
	val name: String,
	val typeParameters: List<MTypeParameter>,
	val underlyingType: MTypeReference,
	val versionRequirement: MVersionRequirement?
) {

	val hasAnnotations
		get() = Flag.HAS_ANNOTATIONS(flags)

	val visibility = MVisibility.forFlags(flags)
		?: throw MetadataException("Type alias '$name' has an unsupported visibility (flags: $flags)")


	override fun equals(other: Any?): Boolean {
		if (other === this) return true
		if (other !is MTypeAlias) return false

		return annotations == other.annotations &&
			expandedType == other.expandedType &&
			flags == other.flags &&
			name == other.name &&
			typeParameters == other.typeParameters &&
			underlyingType == other.underlyingType &&
			versionRequirement == other.versionRequirement
	}


	override fun hashCode() =
		Objects.hash(
			annotations,
			expandedType,
			flags,
			name,
			typeParameters,
			underlyingType,
			versionRequirement
		)


	override fun toString() = typeToString(
		"name" to name,
		"annotations" to annotations,
		"expandedType" to expandedType,
		"hasAnnotations" to hasAnnotations,
		"typeParameters" to typeParameters,
		"underlyingType" to underlyingType,
		"visibility" to visibility,
		"versionRequirement" to versionRequirement
	)


	companion object
}
