package io.fluidsonic.meta

import javax.lang.model.element.*
import kotlin.reflect.*


/** A fully qualified Kotlin type name, stored in Kotlin internal format (e.g., `kotlin/collections/List`). */
@JvmInline
public value class MQualifiedTypeName @PublishedApi internal constructor(public val kotlinInternal: String) {

	public constructor(packageName: MPackageName, typeName: MTypeName) : this(
		when {
			packageName.isRoot -> typeName.kotlinInternal
			typeName.kotlinInternal.startsWith('.') -> ".${packageName.kotlinInternal}/${typeName.kotlinInternal.substring(startIndex = 1)}"
			else -> "${packageName.kotlinInternal}/${typeName.kotlinInternal}"
		}
	)


	override fun toString(): String = kotlin


	public companion object {

		/** Creates a qualified type name from Kotlin-style package and type name strings. */
		public fun fromKotlin(packageName: String, typeName: String): MQualifiedTypeName =
			MQualifiedTypeName(
				packageName = MPackageName.fromKotlin(packageName),
				typeName = MTypeName.fromKotlin(typeName)
			)


		/** Creates a qualified type name from a JVM internal name (e.g., `java/lang/String`). */
		public fun fromJvmInternal(jvmInternal: String): MQualifiedTypeName =
			MQualifiedTypeName(kotlinInternal = jvmInternal.replace('$', '.'))


		/** Creates a qualified type name from a Kotlin internal name (e.g., `kotlin/collections/List`). */
		public fun fromKotlinInternal(kotlinInternal: String): MQualifiedTypeName =
			MQualifiedTypeName(kotlinInternal = kotlinInternal)


		/** Creates a qualified type name from a Java class. */
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


		/** Creates a qualified type name from a Kotlin class. */
		public fun of(kotlinClass: KClass<*>): MQualifiedTypeName =
			of(kotlinClass.java)


		/** Creates a qualified type name from an annotation processing element. */
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


/** Whether this type name refers to a local type. */
public val MQualifiedTypeName.isLocal: Boolean
	get() = kotlinInternal.startsWith('.')


/** The JVM internal representation of this qualified type name. */
public val MQualifiedTypeName.jvmInternal: String
	get() = kotlinInternal
		.removePrefix(".")
		.replace('.', '$')


/** The Kotlin-style dot-separated representation of this qualified type name. */
public val MQualifiedTypeName.kotlin: String
	get() = kotlinInternal
		.removePrefix(".")
		.replace('/', '.')


/** The package name portion of this qualified type name. */
public val MQualifiedTypeName.packageName: MPackageName
	get() = MPackageName.fromKotlinInternal(kotlinInternal
		.removePrefix(".")
		.substringBeforeLast('/', missingDelimiterValue = "")
	)


/** Returns the simple type name without the package prefix. */
public fun MQualifiedTypeName.withoutPackage(): MTypeName =
	MTypeName.fromKotlinInternal(
		if (kotlinInternal.startsWith('.'))
			"." + kotlinInternal
				.removePrefix(".")
				.substringAfterLast('/')
		else
			kotlinInternal.substringAfterLast('/')
	)


/** Returns a new qualified type name with the given [packageName]. */
public fun MQualifiedTypeName.withPackage(packageName: MPackageName): MQualifiedTypeName =
	MQualifiedTypeName(packageName = packageName, typeName = withoutPackage())
