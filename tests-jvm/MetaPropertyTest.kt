package io.fluidsonic.meta

import kotlin.test.*


@Suppress("unused")
class PropertyFixture {
	val readOnly: String = "hello"
	var mutable: Int = 0
	lateinit var lateInit: String
}


class MetaPropertyTest {

	private val meta = Meta.of(PropertyFixture::class.java) as MClass
	private val properties = meta.properties

	private fun findProperty(name: String): MProperty =
		properties.first { it.name.kotlin == name }

	@Test
	fun readOnlyProperty_isNotVar() {
		val prop = findProperty("readOnly")
		assertFalse(prop.isVar, "readOnly should not be var")
	}

	@Test
	fun mutableProperty_isVar() {
		val prop = findProperty("mutable")
		assertTrue(prop.isVar, "mutable should be var")
	}

	@Test
	fun lateinitProperty_isLateinit() {
		val prop = findProperty("lateInit")
		assertTrue(prop.isLateinit, "lateInit should be lateinit")
	}

	@Test
	fun property_hasGetter() {
		val prop = findProperty("readOnly")
		assertNotNull(prop.getter, "readOnly should have a getter")
	}

	@Test
	fun mutableProperty_hasSetter() {
		val prop = findProperty("mutable")
		assertNotNull(prop.setter, "mutable should have a setter")
	}

	@Test
	fun readOnlyProperty_hasNoSetter() {
		val prop = findProperty("readOnly")
		assertNull(prop.setter, "readOnly should not have a setter")
	}
}
