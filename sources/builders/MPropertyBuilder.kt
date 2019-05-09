package com.github.fluidsonic.fluid.meta

import com.github.fluidsonic.fluid.stdlib.*
import kotlinx.metadata.*
import kotlinx.metadata.jvm.*


internal class MPropertyBuilder(
	private val flags: Flags,
	private val getterFlags: Flags,
	private val name: MVariableName,
	private val setterFlags: Flags
) : KmPropertyVisitor() {

	private var jvmFieldSignature: MJvmMemberSignature.Field? = null
	private var jvmGetterSignature: MJvmMemberSignature.Method? = null
	private var jvmSetterSignature: MJvmMemberSignature.Method? = null
	private var jvmSyntheticMethodForAnnotationsSignature: MJvmMemberSignature.Method? = null
	private var receiverParameter: MTypeReferenceBuilder? = null
	private var returnType: MTypeReferenceBuilder? = null
	private var setterParameter: MValueParameterBuilder? = null
	private var typeParameters: MutableList<MTypeParameterBuilder>? = null
	private var versionRequirements: MutableList<MVersionRequirementBuilder>? = null


	fun build(): MProperty {
		val returnType = returnType?.build() ?: throw MetaException("property '$name' has a getter without type")
		val getter = Flag.Property.HAS_GETTER(flags).thenTake {
			MPropertyAccessor.Getter(
				isDefault = !Flag.PropertyAccessor.IS_NOT_DEFAULT(getterFlags),
				isExternal = Flag.PropertyAccessor.IS_EXTERNAL(getterFlags),
				isInline = Flag.PropertyAccessor.IS_INLINE(getterFlags),
				jvmSignature = jvmGetterSignature,
				returnType = returnType,
				visibility = MVisibility.forFlags(getterFlags)
			)
		} ?: throw MetaException("property '$name' has no getter")
		val setter = Flag.Property.HAS_SETTER(flags).thenTake {
			MPropertyAccessor.Setter(
				isDefault = !Flag.PropertyAccessor.IS_NOT_DEFAULT(setterFlags),
				isExternal = Flag.PropertyAccessor.IS_EXTERNAL(setterFlags),
				isInline = Flag.PropertyAccessor.IS_INLINE(setterFlags),
				jvmSignature = jvmSetterSignature,
				parameter = setterParameter?.build() ?: MValueParameter(
					declaresDefaultValue = false,
					isCrossinline = false,
					isNoinline = returnType is MTypeReference.Function,
					name = MVariableName("<set-?>"),
					type = returnType,
					varargElementType = null
				),
				visibility = MVisibility.forFlags(setterFlags)
			)
		}

		return MProperty(
			getter = getter,
			inheritanceRestriction = MInheritanceRestriction.forFlags(flags),
			isConst = Flag.Property.IS_CONST(flags),
			isDelegated = Flag.Property.IS_DELEGATED(flags),
			isExpect = Flag.Property.IS_EXPECT(flags),
			isExternal = Flag.Property.IS_EXTERNAL(flags),
			isInline = getter.isInline && (setter == null || setter.isInline),
			isLateinit = Flag.Property.IS_LATEINIT(flags),
			isVar = Flag.Property.IS_VAR(flags),
			jvmFieldSignature = jvmFieldSignature,
			jvmSyntheticMethodForAnnotationsSignature = jvmSyntheticMethodForAnnotationsSignature,
			name = name,
			receiverParameterType = receiverParameter?.build(),
			setter = setter,
			source = MClassMemberSource.forFlags(flags),
			typeParameters = typeParameters.mapOrEmpty { it.build() },
			versionRequirements = versionRequirements.mapOrEmpty { it.build() },
			visibility = MVisibility.forFlags(flags)
		)
	}


	override fun visitExtensions(type: KmExtensionType) =
		(type == JvmPropertyExtensionVisitor.TYPE).thenTake {
			object : JvmPropertyExtensionVisitor() {

				override fun visit(fieldDesc: JvmFieldSignature?, getterDesc: JvmMethodSignature?, setterDesc: JvmMethodSignature?) {
					jvmFieldSignature = fieldDesc?.let(::MJvmMemberSignature)
					jvmGetterSignature = getterDesc?.let(::MJvmMemberSignature)
					jvmSetterSignature = setterDesc?.let(::MJvmMemberSignature)
				}


				override fun visitSyntheticMethodForAnnotations(desc: JvmMethodSignature?) {
					jvmSyntheticMethodForAnnotationsSignature = desc?.let(::MJvmMemberSignature)
				}
			}
		}


	override fun visitReceiverParameterType(flags: Flags) =
		MTypeReferenceBuilder(flags = flags)
			.also { receiverParameter = it }


	override fun visitReturnType(flags: Flags) =
		MTypeReferenceBuilder(flags = flags)
			.also { returnType = it }


	override fun visitSetterParameter(flags: Flags, name: String) =
		MValueParameterBuilder(flags = flags, name = MVariableName(name))
			.also { setterParameter = it }


	override fun visitTypeParameter(flags: Flags, name: String, id: Int, variance: KmVariance) =
		MTypeParameterBuilder(flags = flags, id = MTypeParameterId(id), name = MTypeParameterName(name), variance = MVariance(variance))
			.also {
				typeParameters?.apply { add(it) } ?: { typeParameters = mutableListOf(it) }()
			}


	override fun visitVersionRequirement() =
		MVersionRequirementBuilder()
			.also {
				versionRequirements?.apply { add(it) } ?: { versionRequirements = mutableListOf(it) }()
			}
}
