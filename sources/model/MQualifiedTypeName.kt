package com.github.fluidsonic.fluid.meta


@Suppress("NON_PUBLIC_PRIMARY_CONSTRUCTOR_OF_INLINE_CLASS")
inline class MQualifiedTypeName private constructor(val kotlinInternal: String) {

	constructor(packageName: MPackageName, typeName: MTypeName) : this(
		if (typeName.kotlinInternal.startsWith('.'))
			".${packageName.kotlinInternal}/${typeName.kotlinInternal.substring(startIndex = 1)}"
		else
			"${packageName.kotlinInternal}/${typeName.kotlinInternal}"
	)


	override fun toString() = kotlin


	companion object {

		fun fromKotlin(packageName: String, typeName: String) =
			MQualifiedTypeName(
				packageName = MPackageName.fromKotlin(packageName),
				typeName = MTypeName.fromKotlin(typeName)
			)


		fun fromJvmInternal(jvmInternal: String) =
			MQualifiedTypeName(kotlinInternal = jvmInternal.replace('$', '.'))


		fun fromKotlinInternal(kotlinInternal: String) =
			MQualifiedTypeName(kotlinInternal = kotlinInternal)
	}
}


val MQualifiedTypeName.isLocal
	get() = kotlinInternal.startsWith('.')


val MQualifiedTypeName.jvmInternal
	get() = kotlinInternal
		.removePrefix(".")
		.replace('.', '$')


val MQualifiedTypeName.kotlin
	get() = kotlinInternal
		.removePrefix(".")
		.replace('/', '.')


val MQualifiedTypeName.packageName
	get() = MPackageName.fromKotlinInternal(kotlinInternal
		.removePrefix(".")
		.substringBeforeLast('/', missingDelimiterValue = "")
	)


fun MQualifiedTypeName.withoutPackage() =
	MTypeName.fromKotlinInternal(
		if (kotlinInternal.startsWith('.'))
			"." + kotlinInternal
				.removePrefix(".")
				.substringAfterLast('/')
		else
			kotlinInternal.substringAfterLast('/')
	)
