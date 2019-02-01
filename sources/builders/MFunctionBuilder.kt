package com.github.fluidsonic.fluid.meta

import com.github.fluidsonic.fluid.stdlib.*
import kotlinx.metadata.Flags
import kotlinx.metadata.KmExtensionType
import kotlinx.metadata.KmFunctionVisitor
import kotlinx.metadata.KmVariance
import kotlinx.metadata.jvm.JvmFunctionExtensionVisitor
import kotlinx.metadata.jvm.JvmMethodSignature


internal class MFunctionBuilder(
	private val flags: Flags,
	private val name: MFunctionName
) : KmFunctionVisitor() {

	private var contract: MContractBuilder? = null
	private var jvmSignature: JvmMethodSignature? = null
	private var lambdaClassOriginName: MTypeName? = null
	private var receiverParameter: MTypeReferenceBuilder? = null
	private var returnType: MTypeReferenceBuilder? = null
	private var typeParameters: MutableList<MTypeParameterBuilder>? = null
	private var valueParameters: MutableList<MValueParameterBuilder>? = null
	private var versionRequirement: MVersionRequirementBuilder? = null


	fun build() = MFunction(
		contract = contract?.build(),
		flags = flags,
		jvmSignature = jvmSignature,
		lambdaClassOriginName = lambdaClassOriginName,
		name = name,
		receiverParameter = receiverParameter?.build(),
		returnType = returnType?.build()
			?: throw MetadataException("Function '$name' has no return type"),
		typeParameters = typeParameters.mapOrEmpty { it.build() },
		valueParameters = valueParameters.mapOrEmpty { it.build() },
		versionRequirement = versionRequirement?.build()
	)


	override fun visitContract() =
		MContractBuilder()
			.also { contract = it }


	override fun visitExtensions(type: KmExtensionType) =
		(type == JvmFunctionExtensionVisitor.TYPE).thenTake {
			object : JvmFunctionExtensionVisitor() {

				override fun visit(desc: JvmMethodSignature?) {
					jvmSignature = desc
				}


				override fun visitLambdaClassOriginName(internalName: String) {
					lambdaClassOriginName = MTypeName(internalName)
				}
			}
		}


	override fun visitReceiverParameterType(flags: Flags) =
		MTypeReferenceBuilder(flags = flags)
			.also { receiverParameter = it }


	override fun visitReturnType(flags: Flags) =
		MTypeReferenceBuilder(flags = flags)
			.also { returnType = it }


	override fun visitTypeParameter(flags: Flags, name: String, id: Int, variance: KmVariance) =
		MTypeParameterBuilder(flags = flags, id = MTypeParameterId(id), name = MTypeParameterName(name), variance = MVariance(variance))
			.also {
				typeParameters?.apply { add(it) } ?: { typeParameters = mutableListOf(it) }()
			}


	override fun visitValueParameter(flags: Flags, name: String) =
		MValueParameterBuilder(flags = flags, name = MVariableName(name))
			.also {
				valueParameters?.apply { add(it) } ?: { valueParameters = mutableListOf(it) }()
			}


	override fun visitVersionRequirement() =
		MVersionRequirementBuilder()
			.also { versionRequirement = it }
}
