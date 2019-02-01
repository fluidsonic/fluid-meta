package com.github.fluidsonic.fluid.meta

import com.github.fluidsonic.fluid.stdlib.*
import kotlinx.metadata.Flags
import kotlinx.metadata.KmConstructorVisitor
import kotlinx.metadata.KmExtensionType
import kotlinx.metadata.jvm.JvmConstructorExtensionVisitor
import kotlinx.metadata.jvm.JvmMethodSignature


internal class MConstructorBuilder(
	private val flags: Flags
) : KmConstructorVisitor() {

	private var jvmSignature: JvmMethodSignature? = null
	private var valueParameters: MutableList<MValueParameterBuilder>? = null
	private var versionRequirement: MVersionRequirementBuilder? = null


	fun build() = MConstructor(
		flags = flags,
		jvmSignature = jvmSignature,
		valueParameters = valueParameters.mapOrEmpty { it.build() },
		versionRequirement = versionRequirement?.build()
	)


	override fun visitExtensions(type: KmExtensionType) =
		(type == JvmConstructorExtensionVisitor.TYPE).thenTake {
			object : JvmConstructorExtensionVisitor() {

				override fun visit(desc: JvmMethodSignature?) {
					jvmSignature = desc
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
			.also { versionRequirement = it }
}
