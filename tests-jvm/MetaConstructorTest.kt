package io.fluidsonic.meta

import kotlin.test.*


class PrimaryOnly(val x: Int)

class WithSecondary(val x: Int) {
	constructor() : this(0)
}


class MetaConstructorTest {

	@Test
	fun primaryConstructor_isNotSecondary() {
		val meta = Meta.of(PrimaryOnly::class.java) as MClass
		val primary = meta.constructors.first { !it.isSecondary }
		assertFalse(primary.isSecondary, "Primary constructor should not be secondary")
		assertTrue(primary.isPrimary, "Primary constructor isPrimary should be true")
	}

	@Test
	fun secondaryConstructor_isSecondary() {
		val meta = Meta.of(WithSecondary::class.java) as MClass
		val secondary = meta.constructors.first { it.isSecondary }
		assertTrue(secondary.isSecondary, "Secondary constructor should be secondary")
	}

	@Test
	fun constructor_hasValueParameters() {
		val meta = Meta.of(PrimaryOnly::class.java) as MClass
		val primary = meta.constructors.first { !it.isSecondary }
		val paramNames = primary.valueParameters.map { it.name.kotlin }
		assertTrue("x" in paramNames, "Constructor should have parameter 'x', got: $paramNames")
	}
}
