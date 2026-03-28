package io.fluidsonic.meta

import kotlin.test.*


class SimpleClass
data class DataClass(val name: String, val age: Int)

sealed class SealedClass {
	class SubA : SealedClass()
	data class SubB(val x: Int) : SealedClass()
}

abstract class AbstractClass
open class OpenClass
annotation class SimpleAnnotation
enum class SimpleEnum { A, B, C }
object SimpleObject
interface SimpleInterface
fun interface FunInterface {
	fun invoke(x: Int): String
}

@JvmInline
value class ValueClass(val value: String)


class MetaClassTest {

	@Test
	fun simpleClass_hasCorrectKind() {
		val meta = Meta.of(SimpleClass::class.java)
		assertIs<MClass>(meta)
	}

	@Test
	fun simpleClass_hasCorrectName() {
		val meta = Meta.of(SimpleClass::class.java) as MClass
		assertTrue(meta.name.kotlin.contains("SimpleClass"), "Name should contain 'SimpleClass', got: ${meta.name.kotlin}")
	}

	@Test
	fun simpleClass_isFinal() {
		val meta = Meta.of(SimpleClass::class.java) as MClass
		assertEquals(meta.inheritanceRestriction, MInheritanceRestriction.FINAL)
	}

	@Test
	fun simpleClass_isPublic() {
		val meta = Meta.of(SimpleClass::class.java) as MClass
		assertEquals(meta.visibility, MVisibility.PUBLIC)
	}

	@Test
	fun dataClass_isDataSpecialization() {
		val meta = Meta.of(DataClass::class.java) as MClass
		assertEquals(meta.specialization, MClass.Specialization.Data)
	}

	@Test
	fun dataClass_hasProperties() {
		val meta = Meta.of(DataClass::class.java) as MClass
		val propertyNames = meta.properties.map { it.name.kotlin }
		assertTrue("name" in propertyNames, "Properties should include 'name', got: $propertyNames")
		assertTrue("age" in propertyNames, "Properties should include 'age', got: $propertyNames")
	}

	@Test
	fun sealedClass_isSealedSpecialization() {
		val meta = Meta.of(SealedClass::class.java) as MClass
		assertIs<MClass.Specialization.Sealed>(meta.specialization)
	}

	@Test
	fun abstractClass_isAbstract() {
		val meta = Meta.of(AbstractClass::class.java) as MClass
		assertEquals(meta.inheritanceRestriction, MInheritanceRestriction.ABSTRACT)
	}

	@Test
	fun openClass_isOpen() {
		val meta = Meta.of(OpenClass::class.java) as MClass
		assertEquals(meta.inheritanceRestriction, MInheritanceRestriction.OPEN)
	}

	@Test
	fun annotationClass_returnsAnnotationClass() {
		val meta = Meta.of(SimpleAnnotation::class.java)
		assertIs<MAnnotationClass>(meta)
	}

	@Test
	fun enumClass_returnsEnumClass() {
		val meta = Meta.of(SimpleEnum::class.java)
		assertIs<MEnumClass>(meta)
		val entryNames = meta.entryNames.map { it.kotlin }
		assertEquals(entryNames, listOf("A", "B", "C"))
	}

	@Test
	fun object_returnsObject() {
		val meta = Meta.of(SimpleObject::class.java)
		assertIs<MObject>(meta)
		assertFalse(meta.isCompanion)
	}

	@Test
	fun interface_returnsInterface() {
		val meta = Meta.of(SimpleInterface::class.java)
		assertIs<MInterface>(meta)
	}

	@Test
	fun funInterface_isFunctional() {
		val meta = Meta.of(FunInterface::class.java)
		assertIs<MInterface>(meta)
		assertTrue(meta.isFunctional, "FunInterface should be functional")
	}

	@Test
	fun valueClass_isValue() {
		val meta = Meta.of(ValueClass::class.java) as MClass
		assertTrue(meta.isValue, "ValueClass should have isValue == true")
	}
}
