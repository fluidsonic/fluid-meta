package io.fluidsonic.meta

import kotlinx.metadata.*


internal class MTypeAliasBuilder(
	private val flags: Flags,
	private val name: MQualifiedTypeName
) : KmTypeAliasVisitor() {

	private var annotations: MutableList<MAnnotation>? = null
	private var expandedType: MTypeReferenceBuilder? = null
	private var typeParameters: MutableList<MTypeParameterBuilder>? = null
	private var underlyingType: MTypeReferenceBuilder? = null
	private var versionRequirements: MutableList<MVersionRequirementBuilder>? = null


	fun build() = MTypeAlias(
		annotations = annotations.toListOrEmpty(),
		expandedType = expandedType?.build() ?: throw MetaException("Type alias '$name' has no expanded type"),
		name = name,
		typeParameters = typeParameters.mapOrEmpty { it.build() },
		underlyingType = underlyingType?.build() ?: throw MetaException("Type alias '$name' has no underlying type"),
		versionRequirements = versionRequirements.mapOrEmpty { it.build() },
		visibility = MVisibility.forFlags(flags)
	)


	override fun visitAnnotation(annotation: KmAnnotation) {
		MAnnotation(annotation).let {
			annotations?.apply { add(it) } ?: run {
				annotations = mutableListOf(it)
			}
		}
	}


	override fun visitExpandedType(flags: Flags) =
		MTypeReferenceBuilder(flags = flags)
			.also { expandedType = it }


	override fun visitTypeParameter(flags: Flags, name: String, id: Int, variance: KmVariance) =
		MTypeParameterBuilder(flags = flags, id = MTypeParameterId(id), name = MTypeParameterName(name), variance = MVariance(variance))
			.also {
				typeParameters?.apply { add(it) } ?: run {
					typeParameters = mutableListOf(it)
				}
			}


	override fun visitUnderlyingType(flags: Flags) =
		MTypeReferenceBuilder(flags = flags)
			.also { underlyingType = it }


	override fun visitVersionRequirement() =
		MVersionRequirementBuilder()
			.also {
				versionRequirements?.apply { add(it) } ?: run {
					versionRequirements = mutableListOf(it)
				}
			}
}
