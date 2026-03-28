package io.fluidsonic.meta


/** Represents a Kotlin module containing packages. */
public data class MModule(
	override val annotations: List<MAnnotation>,
	val name: MModuleName,
	val packages: List<MPackage>
) : MAnnotatable, Meta {

	override fun toString(): String =
		MetaCodeWriter.write(this)


	public companion object
}
