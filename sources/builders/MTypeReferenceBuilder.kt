package com.github.fluidsonic.fluid.meta

import com.github.fluidsonic.fluid.stdlib.*
import kotlinx.metadata.ClassName
import kotlinx.metadata.Flags
import kotlinx.metadata.KmAnnotation
import kotlinx.metadata.KmExtensionType
import kotlinx.metadata.KmTypeVisitor
import kotlinx.metadata.KmVariance
import kotlinx.metadata.jvm.JvmTypeExtensionVisitor


internal class MTypeReferenceBuilder(
	private val flags: Flags
) : KmTypeVisitor() {

	private var annotations: MutableList<MAnnotation>? = null
	private var arguments: MutableList<TypeArgument>? = null
	private var className: MTypeName? = null
	private var flexibilityTypeUpperBound: FlexibilityTypeUpperBound? = null
	private var isRaw = false
	private var outerType: MTypeReferenceBuilder? = null
	private var typeAliasName: MTypeName? = null
	private var typeParameterId: MTypeParameterId? = null


	fun build(): MTypeReference {
		val className = className
		val typeAliasName = typeAliasName
		val typeParameterId = typeParameterId

		return when {
			className != null -> MClassReference(
				annotations = annotations.toListOrEmpty(),
				arguments = arguments.mapOrEmpty { it.build() },
				flags = flags,
				flexibilityTypeUpperBound = flexibilityTypeUpperBound?.build(),
				isRaw = isRaw,
				name = className,
				outerType = outerType?.build()
			)
			typeAliasName != null -> MTypeAliasReference(
				annotations = annotations.toListOrEmpty(),
				arguments = arguments.mapOrEmpty { it.build() },
				flags = flags,
				flexibilityTypeUpperBound = flexibilityTypeUpperBound?.build(),
				isRaw = isRaw,
				name = typeAliasName
			)
			typeParameterId != null -> MTypeParameterReference(
				annotations = annotations.toListOrEmpty(),
				arguments = arguments.mapOrEmpty { it.build() },
				flags = flags,
				flexibilityTypeUpperBound = flexibilityTypeUpperBound?.build(),
				id = typeParameterId,
				isRaw = isRaw
			)
			else -> throw MetadataException("Type not supported")
		}
	}


	override fun visitArgument(flags: Flags, variance: KmVariance) =
		MTypeReferenceBuilder(flags = flags)
			.also {
				arguments?.apply { add(TypeArgument.Type(it, variance = MVariance(variance))) }
					?: { arguments = mutableListOf(TypeArgument.Type(it, variance = MVariance(variance))) }()
			}


	override fun visitClass(name: ClassName) {
		className = MTypeName(name)
	}


	override fun visitExtensions(type: KmExtensionType) =
		(type == JvmTypeExtensionVisitor.TYPE).thenTake {
			object : JvmTypeExtensionVisitor() {

				override fun visit(isRaw: Boolean) {
					this@MTypeReferenceBuilder.isRaw = isRaw
				}


				override fun visitAnnotation(annotation: KmAnnotation) {
					MAnnotation(
						className = MTypeName(annotation.className),
						arguments = annotation.arguments.mapKeys { MTypeParameterName(it.key) }
					).let {
						annotations?.apply { add(it) }
							?: { annotations = mutableListOf(it) }()
					}
				}
			}

		}


	override fun visitFlexibleTypeUpperBound(flags: Flags, typeFlexibilityId: String?) =
		MTypeReferenceBuilder(flags = flags)
			.also {
				flexibilityTypeUpperBound = FlexibilityTypeUpperBound(it, typeFlexibilityId = typeFlexibilityId?.let(::MTypeFlexibilityId))
			}


	override fun visitOuterType(flags: Flags) =
		MTypeReferenceBuilder(flags = flags)
			.also { outerType = it }


	override fun visitStarProjection() {
		arguments?.apply { add(TypeArgument.StarProjection) }
			?: { arguments = mutableListOf(TypeArgument.StarProjection) }()
	}


	override fun visitTypeAlias(name: ClassName) {
		typeAliasName = MTypeName(name)
	}


	override fun visitTypeParameter(id: Int) {
		typeParameterId = MTypeParameterId(id)
	}


	override fun visitAbbreviatedType(flags: Flags) = TODO()


	private data class FlexibilityTypeUpperBound(
		val type: MTypeReferenceBuilder,
		val typeFlexibilityId: MTypeFlexibilityId?
	) {

		fun build() = MFlexibilityTypeUpperBound(
			type = type.build(),
			typeFlexibilityId = typeFlexibilityId
		)
	}


	private sealed class TypeArgument {

		abstract fun build(): MTypeArgument


		data class Type(val type: MTypeReferenceBuilder, val variance: MVariance) : TypeArgument() {

			override fun build() = MTypeArgument.Type(
				type = type.build(),
				variance = variance
			)
		}


		object StarProjection : TypeArgument() {

			override fun build() = MTypeArgument.StarProjection
		}
	}
}
