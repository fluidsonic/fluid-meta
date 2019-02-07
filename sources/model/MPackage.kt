package com.github.fluidsonic.fluid.meta


@Suppress("EqualsOrHashCode")
data class MPackage(
	override val fileFacadeTypes: List<MQualifiedTypeName>,
	val multiFileClassParts: Map<MQualifiedTypeName, MQualifiedTypeName>,
	val name: MPackageName
) : MFileFacadeTypeContainer, MIdentifyable {

	override val localId by lazy { MLocalId.Package(name = name) }


	override fun hashCode() =
		localId.hashCode()


	override fun toString() = typeToString(
		"name" to if (name.isRoot) "<root>" else name,
		"fileFacadeTypes" to fileFacadeTypes,
		"multiFileClassParts" to multiFileClassParts
	)


	companion object
}
