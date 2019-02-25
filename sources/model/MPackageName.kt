package com.github.fluidsonic.fluid.meta

import javax.lang.model.element.Element
import kotlin.reflect.KClass


@Suppress("NON_PUBLIC_PRIMARY_CONSTRUCTOR_OF_INLINE_CLASS")
inline class MPackageName private constructor(val kotlinInternal: String) {

	override fun toString() = kotlin


	companion object {

		fun empty() =
			MPackageName(kotlinInternal = "")


		fun fromKotlin(kotlin: String) =
			MPackageName(kotlinInternal = kotlin.replace('.', '/'))


		fun fromJvmInternal(jvmInternal: String) =
			MPackageName(kotlinInternal = jvmInternal)


		fun fromKotlinInternal(kotlinInternal: String) =
			MPackageName(kotlinInternal = kotlinInternal)


		fun of(javaClass: Class<*>) =
			fromJvmInternal(javaClass.`package`.name)


		// TODO we have to do a whole lot of conversions here between JVM-land and Kotlin-land
		fun of(kotlinClass: KClass<*>) =
			of(kotlinClass.java)


		fun of(element: Element) =
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


val MPackageName.isDefaultImport
	get() = defaultImports.contains(kotlinInternal)


fun MPackageName.isEmpty() =
	kotlinInternal.isEmpty()


fun MPackageName.isNotEmpty() =
	kotlinInternal.isNotEmpty()


val MPackageName.jvmInternal
	get() = kotlinInternal


val MPackageName.kotlin
	get() = kotlinInternal.replace('/', '.')


val MPackageName.isRoot
	get() = kotlinInternal.isEmpty()
