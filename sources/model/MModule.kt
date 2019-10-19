package io.fluidsonic.meta


data class MModule(
	override val annotations: List<MAnnotation>,
	val name: MModuleName,
	val packages: List<MPackage>
) : MAnnotatable, Meta {

	override fun toString() =
		MetaCodeWriter.write(this)


	companion object
}
