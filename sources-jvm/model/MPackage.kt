package io.fluidsonic.meta


@Suppress("EqualsOrHashCode")
public data class MPackage(
	override val fileTypes: List<MQualifiedTypeName>,
	val multiFileClassParts: Map<MQualifiedTypeName, MQualifiedTypeName>,
	val name: MPackageName
) : MFileTypeContainer, MIdentifyable {

	override val localId: MLocalId.Package by lazy { MLocalId.Package(name = name) }


	override fun hashCode(): Int =
		localId.hashCode()


	override fun toString(): String =
		MetaCodeWriter.write(this)


	public companion object
}
