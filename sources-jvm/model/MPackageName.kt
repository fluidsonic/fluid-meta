package io.fluidsonic.meta

import javax.lang.model.element.*
import kotlin.reflect.*


@JvmInline
public value class MPackageName @PublishedApi internal constructor(public val kotlinInternal: String) {

	override fun toString(): String = kotlin


	public companion object {

		public fun empty(): MPackageName =
			MPackageName(kotlinInternal = "")


		public fun fromKotlin(kotlin: String): MPackageName =
			MPackageName(kotlinInternal = kotlin.replace('.', '/'))


		public fun fromJvmInternal(jvmInternal: String): MPackageName =
			MPackageName(kotlinInternal = jvmInternal)


		public fun fromKotlinInternal(kotlinInternal: String): MPackageName =
			MPackageName(kotlinInternal = kotlinInternal)


		public fun of(javaClass: Class<*>): MPackageName =
			fromJvmInternal(javaClass.`package`.name)


		// TODO we have to do a whole lot of conversions here between JVM-land and Kotlin-land
		public fun of(kotlinClass: KClass<*>): MPackageName =
			of(kotlinClass.java)


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


public val MPackageName.isDefaultImport: Boolean
	get() = defaultImports.contains(kotlinInternal)


public fun MPackageName.isEmpty(): Boolean =
	kotlinInternal.isEmpty()


public fun MPackageName.isNotEmpty(): Boolean =
	kotlinInternal.isNotEmpty()


public val MPackageName.jvmInternal: String
	get() = kotlinInternal


public val MPackageName.kotlin: String
	get() = kotlinInternal.replace('/', '.')


public val MPackageName.isRoot: Boolean
	get() = kotlinInternal.isEmpty()
