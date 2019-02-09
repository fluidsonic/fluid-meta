package com.github.fluidsonic.fluid.meta

import javax.lang.model.element.Element
import javax.lang.model.element.PackageElement
import kotlin.reflect.KClass


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

		fun from(javaClass: Class<*>) =
			from(javaClass.kotlin)


		// TODO we have to do a whole lot of conversions here between JVM-land and Kotlin-land
		fun from(kotlinClass: KClass<*>) = when (kotlinClass) {
			Any::class ->
				MQualifiedTypeName("kotlin/Any")

			else -> {
				val qualifiedJavaName = kotlinClass.java.name
				val packageName = qualifiedJavaName.substringBeforeLast('.', missingDelimiterValue = "").replace('.', '/')
				val typeName = qualifiedJavaName.substringAfterLast('.')

				fromJvmInternal("$packageName/$typeName")
			}
		}


		fun from(element: Element): MQualifiedTypeName {
			var packageName = ""
			val components = mutableListOf<String>()
			components += element.simpleName.toString()

			var enclosingElement: Element? = element.enclosingElement
			while (enclosingElement != null) {
				if (enclosingElement is PackageElement)
					packageName = enclosingElement.qualifiedName.toString().replace('.', '/')
				else
					components += enclosingElement.simpleName.toString()

				enclosingElement = enclosingElement.enclosingElement
			}
			components.reverse()

			val typeName = components.joinToString(separator = "$")

			return fromJvmInternal("$packageName/$typeName")
		}


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


fun MQualifiedTypeName.withPackage(packageName: MPackageName) =
	MQualifiedTypeName(packageName = packageName, typeName = withoutPackage())
