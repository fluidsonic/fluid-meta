package com.github.fluidsonic.fluid.meta

import com.github.fluidsonic.fluid.stdlib.*
import kotlinx.metadata.Flags
import kotlinx.metadata.KmExtensionType
import kotlinx.metadata.KmPackageVisitor
import kotlinx.metadata.jvm.JvmPackageExtensionVisitor


internal class MFileFacadeBuilder : KmPackageVisitor() {

	private var functions: MutableList<MFunctionBuilder>? = null
	private var localDelegatedProperties: MutableList<MPropertyBuilder>? = null
	private var properties: MutableList<MPropertyBuilder>? = null
	private var typeAliases: MutableList<MTypeAliasBuilder>? = null


	fun build() = MFileFacade(
		functions = functions.mapOrEmpty { it.build() },
		localDelegatedProperties = localDelegatedProperties.mapOrEmpty { it.build() },
		properties = properties.mapOrEmpty { it.build() },
		typeAliases = typeAliases.mapOrEmpty { it.build() }
	)


	override fun visitExtensions(type: KmExtensionType) =
		(type == JvmPackageExtensionVisitor.TYPE).thenTake {
			object : JvmPackageExtensionVisitor() {

				override fun visitLocalDelegatedProperty(flags: Flags, name: String, getterFlags: Flags, setterFlags: Flags) =
					MPropertyBuilder(flags = flags, getterFlags = getterFlags, name = MVariableName(name), setterFlags = setterFlags)
						.also {
							localDelegatedProperties?.apply { add(it) }
								?: { localDelegatedProperties = mutableListOf(it) }()
						}
			}
		}


	override fun visitFunction(flags: Flags, name: String) =
		MFunctionBuilder(flags = flags, name = MFunctionName(name))
			.also {
				functions?.apply { add(it) } ?: { functions = mutableListOf(it) }()
			}


	override fun visitProperty(flags: Flags, name: String, getterFlags: Flags, setterFlags: Flags) =
		MPropertyBuilder(flags = flags, getterFlags = getterFlags, name = MVariableName(name), setterFlags = setterFlags)
			.also {
				properties?.apply { add(it) } ?: { properties = mutableListOf(it) }()
			}


	override fun visitTypeAlias(flags: Flags, name: String) =
		MTypeAliasBuilder(flags = flags, name = name)
			.also {
				typeAliases?.apply { add(it) } ?: { typeAliases = mutableListOf(it) }()
			}
}
