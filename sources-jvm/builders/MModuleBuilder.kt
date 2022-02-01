package io.fluidsonic.meta

import kotlinx.metadata.*
import kotlinx.metadata.jvm.*


internal class MModuleBuilder(
	private val name: MModuleName
) : KmModuleVisitor() {

	private var annotations: MutableList<MAnnotation>? = null
	private var packages: MutableList<MPackage>? = null


	fun build() = MModule(
		annotations = annotations.toListOrEmpty(),
		name = name,
		packages = packages.toListOrEmpty()
	)


	override fun visitAnnotation(annotation: KmAnnotation) {
		MAnnotation(annotation).let {
			annotations?.apply { add(it) } ?: run {
				annotations = mutableListOf(it)
			}
		}
	}


	override fun visitPackageParts(fqName: ClassName, fileFacades: List<String>, multiFileClassParts: Map<String, String>) {
		MPackage(
			fileTypes = fileFacades.map { MQualifiedTypeName.fromKotlinInternal(it) },
			multiFileClassParts = multiFileClassParts.entries
				.associate { MQualifiedTypeName.fromKotlinInternal(it.key) to MQualifiedTypeName.fromKotlinInternal(it.value) },
			name = MPackageName.fromKotlinInternal(fqName)
		).let {
			packages?.apply { add(it) } ?: run {
				packages = mutableListOf(it)
			}
		}
	}
}
