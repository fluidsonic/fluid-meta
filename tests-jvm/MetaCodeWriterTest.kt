package io.fluidsonic.meta

import kotlin.test.*


class SimpleTestClass
enum class SimpleTestEnum { A, B }


class MetaCodeWriterTest {

	@Test
	fun classToString_containsClassName() {
		val meta = Meta.of(SimpleTestClass::class.java) as MClass
		val output = meta.toString()
		assertTrue(output.contains("SimpleTestClass"), "toString should contain class name, got: $output")
	}

	@Test
	fun enumToString_containsEntries() {
		val meta = Meta.of(SimpleTestEnum::class.java) as MEnumClass
		val output = meta.toString()
		assertTrue(output.contains("A"), "toString should contain enum entry 'A', got: $output")
	}
}
