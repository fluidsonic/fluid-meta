package com.github.fluidsonic.fluid.meta


@Suppress("NON_PUBLIC_PRIMARY_CONSTRUCTOR_OF_INLINE_CLASS")
inline class MTypeName private constructor(val kotlinInternal: String) {

	override fun toString() = kotlin


	companion object {

		fun fromKotlin(kotlin: String, isLocal: Boolean = false) =
			MTypeName(kotlinInternal = if (isLocal) ".$kotlin" else kotlin)


		fun fromJvmInternal(jvmInternal: String) =
			MTypeName(kotlinInternal = jvmInternal.replace('$', '.')
			)


		fun fromKotlinInternal(kotlinInternal: String) =
			MTypeName(kotlinInternal = kotlinInternal)
	}
}


val MTypeName.isLocal
	get() = kotlinInternal.startsWith('.')


val MTypeName.jvmInternal
	get() = kotlinInternal
		.removePrefix(".")
		.replace('.', '$')


val MTypeName.kotlin
	get() = kotlinInternal
		.removePrefix(".")


fun MTypeName.inPackage(name: MPackageName) =
	MQualifiedTypeName(
		packageName = name,
		typeName = this
	)
