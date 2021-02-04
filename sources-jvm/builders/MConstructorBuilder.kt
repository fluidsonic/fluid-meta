package io.fluidsonic.meta

import kotlinx.metadata.*
import kotlinx.metadata.jvm.*


internal class MConstructorBuilder(
	private val flags: Flags
) : KmConstructorVisitor() {

	private var jvmSignature: MJvmMemberSignature.Method? = null
	private var valueParameters: MutableList<MValueParameterBuilder>? = null
	private var versionRequirements: MutableList<MVersionRequirementBuilder>? = null


	fun build() = MConstructor(
		isSecondary = Flag.Constructor.IS_SECONDARY(flags),
		jvmSignature = jvmSignature,
		valueParameters = valueParameters.mapOrEmpty { it.build() },
		versionRequirements = versionRequirements.mapOrEmpty { it.build() },
		visibility = MVisibility.forFlags(flags)
	)


	override fun visitExtensions(type: KmExtensionType) =
		(type == JvmConstructorExtensionVisitor.TYPE).thenTake {
			object : JvmConstructorExtensionVisitor() {

				override fun visit(signature: JvmMethodSignature?) {
					jvmSignature = signature?.let(::MJvmMemberSignature)
				}
			}
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
