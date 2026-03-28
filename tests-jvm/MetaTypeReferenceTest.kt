package io.fluidsonic.meta

import kotlin.test.*


@Suppress("unused")
class TypeFixture {
	fun nullable(): String? = null
	fun generic(): List<String> = emptyList()
	fun functionType(): (Int) -> String = { it.toString() }
	fun suspendFunctionType(): suspend () -> Unit = {}
	fun nestedGeneric(): Map<String, List<Int>> = emptyMap()
}


class MetaTypeReferenceTest {

	private val meta = Meta.of(TypeFixture::class.java) as MClass
	private val functions = meta.functions

	private fun findFunction(name: String): MFunction =
		functions.first { it.name.kotlin == name }

	@Test
	fun nullableReturnType_isNullable() {
		val fn = findFunction("nullable")
		assertTrue(fn.returnType.isNullable, "nullable() return type should be nullable")
	}

	@Test
	fun genericReturnType_hasArguments() {
		val fn = findFunction("generic")
		val returnType = fn.returnType
		assertIs<MTypeReference.Class>(returnType)
		assertTrue(returnType.arguments.isNotEmpty(), "generic() return type should have type arguments")
	}

	@Test
	fun functionTypeReturnType_isFunctionReference() {
		val fn = findFunction("functionType")
		assertIs<MTypeReference.Function>(fn.returnType)
	}

	@Test
	fun suspendFunctionType_isFunctionType() {
		// Suspend function types in metadata are encoded as regular function types
		// (kotlin/FunctionN with Continuation parameter) rather than SuspendFunctionN.
		// The library decodes them as MTypeReference.Function.
		val fn = findFunction("suspendFunctionType")
		val returnType = fn.returnType
		assertIs<MTypeReference.Function>(returnType)
	}
}
