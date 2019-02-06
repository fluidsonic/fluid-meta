package com.github.fluidsonic.fluid.meta

import com.github.fluidsonic.fluid.stdlib.*
import kotlinx.metadata.ClassName
import kotlinx.metadata.Flag
import kotlinx.metadata.Flags
import kotlinx.metadata.KmAnnotation
import kotlinx.metadata.KmExtensionType
import kotlinx.metadata.KmTypeVisitor
import kotlinx.metadata.KmVariance
import kotlinx.metadata.jvm.JvmTypeExtensionVisitor


internal class MTypeReferenceBuilder(
	private val flags: Flags
) : KmTypeVisitor() {

	private var abbreviatedType: MTypeReferenceBuilder? = null
	private var annotations: MutableList<MAnnotation>? = null
	private var arguments: MutableList<TypeArgument>? = null
	private var className: MQualifiedTypeName? = null
	private var flexibleTypeUpperBound: FlexibleTypeUpperBound? = null
	private var isRaw = false
	private var outerType: MTypeReferenceBuilder? = null
	private var typeAliasName: MQualifiedTypeName? = null
	private var typeParameterId: MTypeParameterId? = null


	fun build(): MTypeReference {
		val className = className
		val typeAliasName = typeAliasName
		val typeParameterId = typeParameterId

		return when {
			className != null -> MClassReference(
				abbreviatedType = abbreviatedType?.build(),
				annotations = annotations.toListOrEmpty(),
				arguments = arguments.mapOrEmpty { it.build() },
				flexibleTypeUpperBound = flexibleTypeUpperBound?.build(),
				isNullable = Flag.Type.IS_NULLABLE(flags),
				isSuspend = Flag.Type.IS_SUSPEND(flags),
				isRaw = isRaw,
				name = className,
				outerType = outerType?.build()
			)
			typeAliasName != null -> MTypeAliasReference(
				abbreviatedType = abbreviatedType?.build(),
				annotations = annotations.toListOrEmpty(),
				arguments = arguments.mapOrEmpty { it.build() },
				flexibleTypeUpperBound = flexibleTypeUpperBound?.build(),
				isNullable = Flag.Type.IS_NULLABLE(flags),
				isRaw = isRaw,
				name = typeAliasName
			)
			typeParameterId != null -> MTypeParameterReference(
				annotations = annotations.toListOrEmpty(),
				arguments = arguments.mapOrEmpty { it.build() },
				flexibleTypeUpperBound = flexibleTypeUpperBound?.build(),
				isNullable = Flag.Type.IS_NULLABLE(flags),
				id = typeParameterId,
				isRaw = isRaw
			)
			else -> throw MetaException("Type not supported")
		}
	}


	override fun visitAbbreviatedType(flags: Flags) =
		MTypeReferenceBuilder(flags = flags)
			.also { abbreviatedType = it }


	override fun visitArgument(flags: Flags, variance: KmVariance) =
		MTypeReferenceBuilder(flags = flags)
			.also {
				arguments?.apply { add(TypeArgument.Type(it, variance = MVariance(variance))) }
					?: { arguments = mutableListOf(TypeArgument.Type(it, variance = MVariance(variance))) }()
			}


	override fun visitClass(name: ClassName) {
		className = MQualifiedTypeName.fromKotlinInternal(name)
	}


	override fun visitExtensions(type: KmExtensionType) =
		(type == JvmTypeExtensionVisitor.TYPE).thenTake {
			object : JvmTypeExtensionVisitor() {

				override fun visit(isRaw: Boolean) {
					this@MTypeReferenceBuilder.isRaw = isRaw
				}


				override fun visitAnnotation(annotation: KmAnnotation) {
					MAnnotation(annotation).let {
						annotations?.apply { add(it) }
							?: { annotations = mutableListOf(it) }()
					}
				}
			}

		}


	override fun visitFlexibleTypeUpperBound(flags: Flags, typeFlexibilityId: String?) =
		MTypeReferenceBuilder(flags = flags)
			.also {
				flexibleTypeUpperBound = FlexibleTypeUpperBound(it, typeFlexibilityId = typeFlexibilityId?.let(::MTypeFlexibilityId))
			}


	override fun visitOuterType(flags: Flags) =
		MTypeReferenceBuilder(flags = flags)
			.also { outerType = it }


	override fun visitStarProjection() {
		arguments?.apply { add(TypeArgument.StarProjection) }
			?: { arguments = mutableListOf(TypeArgument.StarProjection) }()
	}


	override fun visitTypeAlias(name: ClassName) {
		typeAliasName = MQualifiedTypeName.fromKotlinInternal(name)
	}


	override fun visitTypeParameter(id: Int) {
		typeParameterId = MTypeParameterId(id)
	}


	private data class FlexibleTypeUpperBound(
		val type: MTypeReferenceBuilder,
		val typeFlexibilityId: MTypeFlexibilityId?
	) {

		fun build() = MFlexibleTypeUpperBound(
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
