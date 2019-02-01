package com.github.fluidsonic.fluid.meta

import kotlinx.metadata.Flags
import kotlinx.metadata.KmAnnotation
import kotlinx.metadata.KmTypeAliasVisitor
import kotlinx.metadata.KmVariance


internal class MTypeAliasBuilder(
	private val flags: Flags,
	private val name: String
) : KmTypeAliasVisitor() {

	private var annotations: MutableList<MAnnotation>? = null
	private var expandedType: MTypeReferenceBuilder? = null
	private var typeParameters: MutableList<MTypeParameterBuilder>? = null
	private var underlyingType: MTypeReferenceBuilder? = null
	private var versionRequirement: MVersionRequirementBuilder? = null


	fun build() = MTypeAlias(
		annotations = annotations.toListOrEmpty(),
		expandedType = expandedType?.build() ?: throw MetadataException("Type alias '$name' has no expanded type"),
		flags = flags,
		name = name,
		typeParameters = typeParameters.mapOrEmpty { it.build() },
		underlyingType = underlyingType?.build() ?: throw MetadataException("Type alias '$name' has no underlying type"),
		versionRequirement = versionRequirement?.build()
	)


	override fun visitAnnotation(annotation: KmAnnotation) {
		MAnnotation(
			className = MTypeName(annotation.className),
			arguments = annotation.arguments.mapKeys { MTypeParameterName(it.key) }
		).let {
			annotations?.apply { add(it) }
				?: { annotations = mutableListOf(it) }()
		}
	}


	override fun visitExpandedType(flags: Flags) =
		MTypeReferenceBuilder(flags = flags)
			.also { expandedType = it }


	override fun visitTypeParameter(flags: Flags, name: String, id: Int, variance: KmVariance) =
		MTypeParameterBuilder(flags = flags, id = MTypeParameterId(id), name = MTypeParameterName(name), variance = MVariance(variance))
			.also {
				typeParameters?.apply { add(it) } ?: { typeParameters = mutableListOf(it) }()
			}


	override fun visitUnderlyingType(flags: Flags) =
		MTypeReferenceBuilder(flags = flags)
			.also { underlyingType = it }


	override fun visitVersionRequirement() =
		MVersionRequirementBuilder()
			.also { versionRequirement = it }
}
