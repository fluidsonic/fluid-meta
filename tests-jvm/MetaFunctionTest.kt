package io.fluidsonic.meta

import kotlin.test.*


@Suppress("RedundantSuspendModifier", "unused")
class FunctionFixture {
	fun simple(): Unit = Unit
	suspend fun suspendFun(): String = ""
	inline fun inlineFun(block: () -> Unit) { block() }
	operator fun plus(other: FunctionFixture): FunctionFixture = this
	infix fun combine(other: FunctionFixture): FunctionFixture = this
	tailrec fun factorial(n: Int, acc: Int = 1): Int = if (n <= 1) acc else factorial(n - 1, n * acc)
	fun String.extensionFun(): Int = length
	fun withDefault(x: Int = 42): Int = x
}


class MetaFunctionTest {

	private val meta = Meta.of(FunctionFixture::class.java) as MClass
	private val functions = meta.functions

	private fun findFunction(name: String): MFunction =
		functions.first { it.name.kotlin == name }

	@Test
	fun simpleFunction_hasCorrectName() {
		val fn = findFunction("simple")
		assertEquals(fn.name.kotlin, "simple")
	}

	@Test
	fun suspendFunction_isSuspend() {
		val fn = findFunction("suspendFun")
		assertTrue(fn.isSuspend, "suspendFun should be suspend")
	}

	@Test
	fun inlineFunction_isInline() {
		val fn = findFunction("inlineFun")
		assertTrue(fn.isInline, "inlineFun should be inline")
	}

	@Test
	fun operatorFunction_isOperator() {
		val fn = findFunction("plus")
		assertTrue(fn.isOperator, "plus should be operator")
	}

	@Test
	fun infixFunction_isInfix() {
		val fn = findFunction("combine")
		assertTrue(fn.isInfix, "combine should be infix")
	}

	@Test
	fun tailrecFunction_isTailrec() {
		val fn = findFunction("factorial")
		assertTrue(fn.isTailrec, "factorial should be tailrec")
	}

	@Test
	fun extensionFunction_hasReceiverParameter() {
		val fn = findFunction("extensionFun")
		assertNotNull(fn.receiverParameterType, "extensionFun should have a receiver parameter type")
	}

	@Test
	fun functionWithDefault_parameterDeclaresDefault() {
		val fn = findFunction("withDefault")
		val param = fn.valueParameters.first { it.name.kotlin == "x" }
		assertTrue(param.declaresDefaultValue, "Parameter 'x' should declare a default value")
	}
}
