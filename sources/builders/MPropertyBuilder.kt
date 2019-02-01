package com.github.fluidsonic.fluid.meta

import com.github.fluidsonic.fluid.stdlib.*
import kotlinx.metadata.Flags
import kotlinx.metadata.KmExtensionType
import kotlinx.metadata.KmPropertyVisitor
import kotlinx.metadata.KmVariance
import kotlinx.metadata.jvm.JvmFieldSignature
import kotlinx.metadata.jvm.JvmMethodSignature
import kotlinx.metadata.jvm.JvmPropertyExtensionVisitor


internal class MPropertyBuilder(
	private val flags: Flags,
	private val getterFlags: Flags,
	private val name: MVariableName,
	private val setterFlags: Flags
) : KmPropertyVisitor() {

	private var jvmFieldSignature: JvmFieldSignature? = null
	private var jvmGetterSignature: JvmMethodSignature? = null
	private var jvmSetterSignature: JvmMethodSignature? = null
	private var jvmSyntheticMethodForAnnotationsSignature: JvmMethodSignature? = null
	private var receiverParameter: MTypeReferenceBuilder? = null
	private var returnType: MTypeReferenceBuilder? = null
	private var setterParameter: MValueParameterBuilder? = null
	private var typeParameters: MutableList<MTypeParameterBuilder>? = null
	private var versionRequirement: MVersionRequirementBuilder? = null


	fun build() = MProperty(
		flags = flags,
		getterFlags = getterFlags,
		jvmFieldSignature = jvmFieldSignature,
		jvmGetterSignature = jvmGetterSignature,
		jvmSetterSignature = jvmSetterSignature,
		jvmSyntheticMethodForAnnotationsSignature = jvmSyntheticMethodForAnnotationsSignature,
		name = name,
		receiverParameter = receiverParameter?.build(),
		returnType = returnType?.build()
			?: throw MetadataException("Property '$name' has no return type"),
		setterFlags = setterFlags,
		setterParameter = setterParameter?.build(),
		typeParameters = typeParameters.mapOrEmpty { it.build() },
		versionRequirement = versionRequirement?.build()
	)


	override fun visitExtensions(type: KmExtensionType) =
		(type == JvmPropertyExtensionVisitor.TYPE).thenTake {
			object : JvmPropertyExtensionVisitor() {

				override fun visit(fieldDesc: JvmFieldSignature?, getterDesc: JvmMethodSignature?, setterDesc: JvmMethodSignature?) {
					jvmFieldSignature = fieldDesc
					jvmGetterSignature = getterDesc
					jvmSetterSignature = setterDesc
				}


				override fun visitSyntheticMethodForAnnotations(desc: JvmMethodSignature?) {
					jvmSyntheticMethodForAnnotationsSignature = desc
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
			.also { versionRequirement = it }
}
