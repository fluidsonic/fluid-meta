package io.fluidsonic.meta

import javax.lang.model.element.*
import kotlin.reflect.*


@Suppress("NON_PUBLIC_PRIMARY_CONSTRUCTOR_OF_INLINE_CLASS")
public inline class MTypeName private constructor(public val kotlinInternal: String) {

	override fun toString(): String = kotlin


	public companion object {

		public fun fromKotlin(kotlin: String, isLocal: Boolean = false): MTypeName =
			MTypeName(kotlinInternal = if (isLocal) ".$kotlin" else kotlin)


		public fun fromJvmInternal(jvmInternal: String): MTypeName =
			MTypeName(kotlinInternal = jvmInternal.replace('$', '.'))


		public fun fromKotlinInternal(kotlinInternal: String): MTypeName =
			MTypeName(kotlinInternal = kotlinInternal)


		// TODO we have to do a whole lot of conversions here between JVM-land and Kotlin-land
		public fun of(javaClass: Class<*>): MTypeName = when (javaClass) {
			Any::class.java ->
				MTypeName("Any")

			else ->
				fromJvmInternal(javaClass.name.substringAfterLast('.'))
		}


		public fun of(kotlinClass: KClass<*>): MTypeName =
			of(kotlinClass.java)


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


public val MTypeName.isLocal: Boolean
	get() = kotlinInternal.startsWith('.')


public val MTypeName.jvmInternal: String
	get() = kotlinInternal
		.removePrefix(".")
		.replace('.', '$')


public val MTypeName.kotlin: String
	get() = kotlinInternal
		.removePrefix(".")


public fun MTypeName.inPackage(name: MPackageName): MQualifiedTypeName =
	MQualifiedTypeName(
		packageName = name,
		typeName = this
	)
