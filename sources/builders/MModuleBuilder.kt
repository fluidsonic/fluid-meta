package com.github.fluidsonic.fluid.meta

import kotlinx.metadata.ClassName
import kotlinx.metadata.KmAnnotation
import kotlinx.metadata.jvm.KmModuleVisitor


internal class MModuleBuilder : KmModuleVisitor() {

	private var annotations: MutableList<MAnnotation>? = null
	private var packages: MutableList<MPackage>? = null


	fun build() = MModule(
		annotations = annotations.toListOrEmpty(),
		packages = packages.toListOrEmpty()
	)


	override fun visitAnnotation(annotation: KmAnnotation) {
		MAnnotation(annotation).let {
			annotations?.apply { add(it) } ?: { annotations = mutableListOf(it) }()
		}
	}


	override fun visitPackageParts(fqName: ClassName, fileFacades: List<String>, multiFileClassParts: Map<String, String>) {
		MPackage(
			fileFacadeTypes = fileFacades.map { MQualifiedTypeName.fromKotlinInternal(it) },
			multiFileClassParts = multiFileClassParts.entries
				.associate { MQualifiedTypeName.fromKotlinInternal(it.key) to MQualifiedTypeName.fromKotlinInternal(it.value) },
			name = MPackageName.fromKotlinInternal(fqName)
		).let {
			packages?.apply { add(it) } ?: { packages = mutableListOf(it) }()
		}
	}
}
