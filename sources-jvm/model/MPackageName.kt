package io.fluidsonic.meta

import javax.lang.model.element.*
import kotlin.reflect.*


/** A Kotlin package name, stored in Kotlin internal format (e.g., `kotlin/collections`). */
@JvmInline
public value class MPackageName @PublishedApi internal constructor(public val kotlinInternal: String) {

	override fun toString(): String = kotlin


	public companion object {

		/** Returns the root (empty) package name. */
		public fun empty(): MPackageName =
			MPackageName(kotlinInternal = "")


		/** Creates a package name from a Kotlin-style dot-separated string. */
		public fun fromKotlin(kotlin: String): MPackageName =
			MPackageName(kotlinInternal = kotlin.replace('.', '/'))


		/** Creates a package name from a JVM internal name. */
		public fun fromJvmInternal(jvmInternal: String): MPackageName =
			MPackageName(kotlinInternal = jvmInternal)


		/** Creates a package name from a Kotlin internal name. */
		public fun fromKotlinInternal(kotlinInternal: String): MPackageName =
			MPackageName(kotlinInternal = kotlinInternal)


		/** Creates a package name from a Java class. */
		public fun of(javaClass: Class<*>): MPackageName =
			fromJvmInternal(javaClass.`package`.name)


		/** Creates a package name from a Kotlin class. */
		// TODO we have to do a whole lot of conversions here between JVM-land and Kotlin-land
		public fun of(kotlinClass: KClass<*>): MPackageName =
			of(kotlinClass.java)


		/** Creates a package name from an annotation processing element. */
		public fun of(element: Element): MPackageName =
			fromJvmInternal(element.`package`.qualifiedName.toString())
	}
}


// TODO how to handle java/lang?
private val defaultImports = setOf(
	"kotlin",
	"kotlin/annotations",
	"kotlin/collections",
	"kotlin/comparisons",
	"kotlin/io",
	"kotlin/jvm",
	"kotlin/ranges",
	"kotlin/sequences",
	"kotlin/text"
)


/** Whether this package is automatically imported in Kotlin source files. */
public val MPackageName.isDefaultImport: Boolean
	get() = defaultImports.contains(kotlinInternal)


/** Whether this package name is empty (root package). */
public fun MPackageName.isEmpty(): Boolean =
	kotlinInternal.isEmpty()


/** Whether this package name is not empty (not the root package). */
public fun MPackageName.isNotEmpty(): Boolean =
	kotlinInternal.isNotEmpty()


/** The JVM internal representation of this package name. */
public val MPackageName.jvmInternal: String
	get() = kotlinInternal


/** The Kotlin-style dot-separated representation of this package name. */
public val MPackageName.kotlin: String
	get() = kotlinInternal.replace('/', '.')


/** Whether this is the root (empty) package. */
public val MPackageName.isRoot: Boolean
	get() = kotlinInternal.isEmpty()
