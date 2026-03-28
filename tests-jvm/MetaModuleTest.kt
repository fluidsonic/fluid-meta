package io.fluidsonic.meta

import kotlin.test.*


class MetaModuleTest {

	@Test
	fun allModulesFromSameSourceAs_returnsModules() {
		val modules = Meta.allModulesFromSameSourceAs(Meta::class.java).toList()
		assertTrue(modules.isNotEmpty(), "Should find at least one module")
	}

	@Test
	fun everythingFromSameSourceAs_returnsElements() {
		val elements = Meta.everythingFromSameSourceAs(Meta::class.java).toList()
		assertTrue(elements.isNotEmpty(), "Should find at least one element")
	}
}
