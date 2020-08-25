package io.fluidsonic.meta

import kotlinx.metadata.*
import kotlinx.metadata.jvm.*


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
		val abbreviatedType = abbreviatedType?.build()
		val arguments = arguments.mapOrEmpty { it.build() }
		val annotations = annotations.toListOrEmpty()
		val className = className
		val flexibleTypeUpperBound = flexibleTypeUpperBound?.build()
		val isNullable = Flag.Type.IS_NULLABLE(flags)
		val typeAliasName = typeAliasName
		val typeParameterId = typeParameterId

		return when {
			className != null && kotlinFunctionTypeNameRegex.matches(className.kotlinInternal) -> {
				val parameterArguments = arguments.toMutableList()

				val isExtension = annotations.any { it.className == kotlinExtensionFunctionAnnotationTypeName }
				if (isExtension)
					check(parameterArguments.size >= 2) { "extension function type must have at least two arguments" }
				else
					check(parameterArguments.size >= 1) { "function type must have at least one argument" }

				val receiverArgument = isExtension.thenTake { parameterArguments.removeAt(0) }

				val isSuspend = Flag.Type.IS_SUSPEND(flags)
				val returnArgument = if (isSuspend) {
					check(parameterArguments.size >= 2) { "suspend fun type must have at least two additional arguments for continuation and state" }
					parameterArguments.removeAt(parameterArguments.size - 1)

					val continuation = parameterArguments.removeAt(parameterArguments.size - 1)
					check(continuation is MTypeArgument.Type &&
						continuation.type is MTypeReference.Class &&
						continuation.type.name.withoutPackage() == kotlinContinuationTypeName &&
						continuation.type.arguments.size == 1
					) { "suspend fun type must have a $kotlinContinuationTypeName as penultimate argument" }

					continuation.type.arguments.first()
				}
				else
					parameterArguments.removeAt(parameterArguments.size - 1)

				MTypeReference.Function(
					annotations = annotations.filterNot { it.className == kotlinExtensionFunctionAnnotationTypeName },
					flexibleTypeUpperBound = flexibleTypeUpperBound,
					isNullable = isNullable,
					isSuspend = isSuspend,
					parameterArguments = parameterArguments,
					receiverArgument = receiverArgument,
					returnArgument = returnArgument
				)
			}

			className != null -> MTypeReference.Class(
				abbreviatedType = abbreviatedType,
				annotations = annotations.toListOrEmpty(),
				arguments = arguments,
				flexibleTypeUpperBound = flexibleTypeUpperBound,
				isNullable = isNullable,
				isRaw = isRaw,
				name = className,
				outerType = outerType?.build()
			)
			typeAliasName != null -> MTypeReference.TypeAlias(
				abbreviatedType = abbreviatedType,
				annotations = annotations.toListOrEmpty(),
				arguments = arguments,
				flexibleTypeUpperBound = flexibleTypeUpperBound,
				isNullable = isNullable,
				isRaw = isRaw,
				name = typeAliasName
			)
			typeParameterId != null -> MTypeReference.TypeParameter(
				annotations = annotations.toListOrEmpty(),
				arguments = arguments,
				flexibleTypeUpperBound = flexibleTypeUpperBound,
				isNullable = isNullable,
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


	companion object {

		private val kotlinContinuationTypeName = MTypeName.fromKotlinInternal("Continuation")
		private val kotlinExtensionFunctionAnnotationTypeName = MQualifiedTypeName.fromKotlinInternal("kotlin/ExtensionFunctionType")
		private val kotlinFunctionTypeNameRegex = Regex("^kotlin/Function\\d+$")
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
