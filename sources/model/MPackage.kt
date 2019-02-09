package com.github.fluidsonic.fluid.meta

@Suppress("EqualsOrHashCode")
data class MPackage(
	override val fileTypes: List<MQualifiedTypeName>,
	val multiFileClassParts: Map<MQualifiedTypeName, MQualifiedTypeName>,
	val name: MPackageName
) : MFileTypeContainer, MIdentifyable {

	override val localId by lazy { MLocalId.Package(name = name) }


	override fun hashCode() =
		localId.hashCode()


	override fun toString() =
		MetaCodeWriter.write(this)


	companion object
}
