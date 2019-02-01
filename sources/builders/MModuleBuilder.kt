package com.github.fluidsonic.fluid.meta

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
		MAnnotation(
			className = MTypeName(annotation.className),
			arguments = annotation.arguments.mapKeys { MTypeParameterName(it.key) }
		).let {
			annotations?.apply { add(it) }
				?: { annotations = mutableListOf(it) }()
		}
	}


	override fun visitPackageParts(fqName: String, fileFacades: List<String>, multiFileClassParts: Map<String, String>) {
		MPackage(
			fileFacades = fileFacades.map(::MTypeName),
			multiFileClassParts = multiFileClassParts.entries.associate { MTypeName(it.key) to MTypeName(it.value) },
			name = MPackageName(fqName)
		).let {
			packages?.apply { add(it) }
				?: { packages = mutableListOf(it) }()
		}
	}
}
