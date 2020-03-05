package io.fluidsonic.meta

import javax.lang.model.element.*
import kotlin.reflect.*


@Suppress("NON_PUBLIC_PRIMARY_CONSTRUCTOR_OF_INLINE_CLASS")
inline class MTypeName private constructor(val kotlinInternal: String) {

	override fun toString() = kotlin


	companion object {

		fun fromKotlin(kotlin: String, isLocal: Boolean = false) =
			MTypeName(kotlinInternal = if (isLocal) ".$kotlin" else kotlin)


		fun fromJvmInternal(jvmInternal: String) =
			MTypeName(kotlinInternal = jvmInternal.replace('$', '.'))


		fun fromKotlinInternal(kotlinInternal: String) =
			MTypeName(kotlinInternal = kotlinInternal)


		// TODO we have to do a whole lot of conversions here between JVM-land and Kotlin-land
		fun of(javaClass: Class<*>) = when (javaClass) {
			Any::class.java ->
				MTypeName("Any")

			else ->
				fromJvmInternal(javaClass.name.substringAfterLast('.'))
		}


		fun of(kotlinClass: KClass<*>) =
			of(kotlinClass.java)


		fun of(element: Element): MTypeName {
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
