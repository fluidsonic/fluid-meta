package io.fluidsonic.meta

import javax.lang.model.element.*
import kotlin.reflect.*


public inline class MQualifiedTypeName @PublishedApi internal constructor(public val kotlinInternal: String) {

	public constructor(packageName: MPackageName, typeName: MTypeName) : this(
		when {
			packageName.isRoot -> typeName.kotlinInternal
			typeName.kotlinInternal.startsWith('.') -> ".${packageName.kotlinInternal}/${typeName.kotlinInternal.substring(startIndex = 1)}"
			else -> "${packageName.kotlinInternal}/${typeName.kotlinInternal}"
		}
	)


	override fun toString(): String = kotlin


	public companion object {

		public fun fromKotlin(packageName: String, typeName: String): MQualifiedTypeName =
			MQualifiedTypeName(
				packageName = MPackageName.fromKotlin(packageName),
				typeName = MTypeName.fromKotlin(typeName)
			)


		public fun fromJvmInternal(jvmInternal: String): MQualifiedTypeName =
			MQualifiedTypeName(kotlinInternal = jvmInternal.replace('$', '.'))


		public fun fromKotlinInternal(kotlinInternal: String): MQualifiedTypeName =
			MQualifiedTypeName(kotlinInternal = kotlinInternal)


		// TODO we have to do a whole lot of conversions here between JVM-land and Kotlin-land
		public fun of(javaClass: Class<*>): MQualifiedTypeName = when (javaClass) {
			Any::class.java ->
				MQualifiedTypeName("kotlin/Any")

			else -> {
				val qualifiedJavaName = javaClass.name
				val packageName = qualifiedJavaName.substringBeforeLast('.', missingDelimiterValue = "").replace('.', '/')
				val typeName = qualifiedJavaName.substringAfterLast('.')

				fromJvmInternal("$packageName/$typeName")
			}
		}


		public fun of(kotlinClass: KClass<*>): MQualifiedTypeName =
			of(kotlinClass.java)


		public fun of(element: Element): MQualifiedTypeName {
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
	}
}


public val MQualifiedTypeName.isLocal: Boolean
	get() = kotlinInternal.startsWith('.')


public val MQualifiedTypeName.jvmInternal: String
	get() = kotlinInternal
		.removePrefix(".")
		.replace('.', '$')


public val MQualifiedTypeName.kotlin: String
	get() = kotlinInternal
		.removePrefix(".")
		.replace('/', '.')


public val MQualifiedTypeName.packageName: MPackageName
	get() = MPackageName.fromKotlinInternal(kotlinInternal
		.removePrefix(".")
		.substringBeforeLast('/', missingDelimiterValue = "")
	)


public fun MQualifiedTypeName.withoutPackage(): MTypeName =
	MTypeName.fromKotlinInternal(
		if (kotlinInternal.startsWith('.'))
			"." + kotlinInternal
				.removePrefix(".")
				.substringAfterLast('/')
		else
			kotlinInternal.substringAfterLast('/')
	)


public fun MQualifiedTypeName.withPackage(packageName: MPackageName): MQualifiedTypeName =
	MQualifiedTypeName(packageName = packageName, typeName = withoutPackage())
