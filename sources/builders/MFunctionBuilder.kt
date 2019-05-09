package com.github.fluidsonic.fluid.meta

import com.github.fluidsonic.fluid.stdlib.*
import kotlinx.metadata.*
import kotlinx.metadata.jvm.*


internal class MFunctionBuilder(
	private val flags: Flags,
	private val name: MFunctionName
) : KmFunctionVisitor() {

	private var contract: MContractBuilder? = null
	private var jvmSignature: MJvmMemberSignature.Method? = null
	private var lambdaClassOriginName: MQualifiedTypeName? = null
	private var receiverParameter: MTypeReferenceBuilder? = null
	private var returnType: MTypeReferenceBuilder? = null
	private var typeParameters: MutableList<MTypeParameterBuilder>? = null
	private var valueParameters: MutableList<MValueParameterBuilder>? = null
	private var versionRequirements: MutableList<MVersionRequirementBuilder>? = null


	fun build() = MFunction(
		contract = contract?.build(),
		inheritanceRestriction = MInheritanceRestriction.forFlags(flags),
		isExpect = Flag.Function.IS_EXPECT(flags),
		isExternal = Flag.Function.IS_EXTERNAL(flags),
		isInfix = Flag.Function.IS_INFIX(flags),
		isInline = Flag.Function.IS_INLINE(flags),
		isOperator = Flag.Function.IS_OPERATOR(flags),
		isSuspend = Flag.Function.IS_SUSPEND(flags),
		isTailrec = Flag.Function.IS_TAILREC(flags),
		jvmSignature = jvmSignature,
		lambdaClassOriginName = lambdaClassOriginName,
		name = name,
		receiverParameterType = receiverParameter?.build(),
		returnType = returnType?.build() ?: throw MetaException("function '$name' has no return type"),
		source = MClassMemberSource.forFlags(flags),
		typeParameters = typeParameters.mapOrEmpty { it.build() },
		valueParameters = valueParameters.mapOrEmpty { it.build() },
		versionRequirements = versionRequirements.mapOrEmpty { it.build() },
		visibility = MVisibility.forFlags(flags)
	)


	override fun visitContract() =
		MContractBuilder()
			.also { contract = it }


	override fun visitExtensions(type: KmExtensionType) =
		(type == JvmFunctionExtensionVisitor.TYPE).thenTake {
			object : JvmFunctionExtensionVisitor() {

				override fun visit(desc: JvmMethodSignature?) {
					jvmSignature = desc?.let(::MJvmMemberSignature)
				}


				override fun visitLambdaClassOriginName(internalName: ClassName) {
					lambdaClassOriginName = MQualifiedTypeName.fromKotlinInternal(internalName)
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
			.also {
				versionRequirements?.apply { add(it) } ?: { versionRequirements = mutableListOf(it) }()
			}
}
