package com.github.fluidsonic.fluid.meta


@Suppress("NON_PUBLIC_PRIMARY_CONSTRUCTOR_OF_INLINE_CLASS")
inline class MPackageName private constructor(val kotlinInternal: String) {

	override fun toString() = kotlin


	companion object {

		fun fromKotlin(kotlin: String) =
			MPackageName(kotlinInternal = kotlin.replace('.', '/'))


		fun fromJvmInternal(jvmInternal: String) =
			MPackageName(kotlinInternal = jvmInternal)


		fun fromKotlinInternal(kotlinInternal: String) =
			MPackageName(kotlinInternal = kotlinInternal)
	}
}


val MPackageName.jvmInternal
	get() = kotlinInternal


val MPackageName.kotlin
	get() = kotlinInternal.replace('/', '.')


val MPackageName.isRoot
	get() = kotlinInternal.isEmpty()
