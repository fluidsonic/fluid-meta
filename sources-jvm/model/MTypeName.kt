package io.fluidsonic.meta

import javax.lang.model.element.*
import kotlin.reflect.*


/** A simple (unqualified) Kotlin type name, stored in Kotlin internal format. */
@JvmInline
public value class MTypeName @PublishedApi internal constructor(public val kotlinInternal: String) {

	override fun toString(): String = kotlin


	public companion object {

		/** Creates a type name from a Kotlin-style name string. */
		public fun fromKotlin(kotlin: String, isLocal: Boolean = false): MTypeName =
			MTypeName(kotlinInternal = if (isLocal) ".$kotlin" else kotlin)


		/** Creates a type name from a JVM internal name. */
		public fun fromJvmInternal(jvmInternal: String): MTypeName =
			MTypeName(kotlinInternal = jvmInternal.replace('$', '.'))


		/** Creates a type name from a Kotlin internal name. */
		public fun fromKotlinInternal(kotlinInternal: String): MTypeName =
			MTypeName(kotlinInternal = kotlinInternal)


		/** Creates a type name from a Java class. */
		// TODO we have to do a whole lot of conversions here between JVM-land and Kotlin-land
		public fun of(javaClass: Class<*>): MTypeName = when (javaClass) {
			Any::class.java ->
				MTypeName("Any")

			else ->
				fromJvmInternal(javaClass.name.substringAfterLast('.'))
		}


		/** Creates a type name from a Kotlin class. */
		public fun of(kotlinClass: KClass<*>): MTypeName =
			of(kotlinClass.java)


		/** Creates a type name from an annotation processing element. */
		public fun of(element: Element): MTypeName {
			val components = mutableListOf<String>()
			components += element.simpleName.toString()

			var enclosingElement: Element? = element.enclosingElement
			while (enclosingElement != null && enclosingElement !is PackageElement) {
				components += enclosingElement.simpleName.toString()
				enclosingElement = enclosingElement.enclosingElement
			}
			components.reverse()

			val typeName = components.joinToString(separator = "$")

			return fromJvmInternal(typeName)
		}
	}
}


/** Whether this type name refers to a local type. */
public val MTypeName.isLocal: Boolean
	get() = kotlinInternal.startsWith('.')


/** The JVM internal representation of this type name. */
public val MTypeName.jvmInternal: String
	get() = kotlinInternal
		.removePrefix(".")
		.replace('.', '$')


/** The Kotlin-style representation of this type name. */
public val MTypeName.kotlin: String
	get() = kotlinInternal
		.removePrefix(".")


/** Returns a fully qualified type name by combining this type name with the given package [name]. */
public fun MTypeName.inPackage(name: MPackageName): MQualifiedTypeName =
	MQualifiedTypeName(
		packageName = name,
		typeName = this
	)
