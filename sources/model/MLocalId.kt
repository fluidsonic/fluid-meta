package com.github.fluidsonic.fluid.meta

import java.util.Objects


// FIXME rework output
sealed class MLocalId {

	// FIXME what about class generics & vararg
	class Constructor(
		private val valueParameterTypes: List<MTypeReference>
	) : MLocalId() {

		private val hashCode =
			Objects.hash(*valueParameterTypes.toTypedArray())


		override fun hashCode() =
			hashCode


		override fun equals(other: Any?) =
			valueParameterTypes == (other as? Constructor)?.valueParameterTypes


		override fun toString() =
			"[constructor(${valueParameterTypes.joinToString { it.name.toString() }})]"
	}


	// FIXME what about generics and return type & vararg
	class Function(
		name: MFunctionName,
		private val receiverParameterType: MTypeReference?,
		private val valueParameterTypes: List<MTypeReference>
	) : MLocalId() {

		private val name = name.toString()

		private val hashCode =
			Objects.hash(*(
				listOf(name, receiverParameterType) +
					valueParameterTypes.fold(0) { hashCode, parameter -> hashCode xor parameter.hashCode() }
				).toTypedArray()
			)


		override fun hashCode() =
			hashCode


		override fun equals(other: Any?): Boolean {
			if (this === other) return true
			if (other !is Function) return false

			return name == other.name &&
				receiverParameterType == other.receiverParameterType &&
				valueParameterTypes == other.valueParameterTypes
		}


		override fun toString() =
			if (receiverParameterType != null)
				"[fun ${receiverParameterType.name}.$name(${valueParameterTypes.joinToString { it.name.toString() }})]"
			else
				"[fun $name(${valueParameterTypes.joinToString { it.name.toString() }})]" // FIXME tostring nullable
	}


	class Package(
		name: MPackageName
	) : MLocalId() {

		private val name = name.kotlin


		override fun hashCode() =
			name.hashCode()


		override fun equals(other: Any?) =
			name == (other as? Package)?.name


		override fun toString() =
			"[package $name]"
	}


	class Property(
		name: MVariableName,
		private val receiverParameterType: MTypeReference?
	) : MLocalId() {

		private val name = name.toString()


		override fun hashCode() =
			Objects.hash(name, receiverParameterType)


		override fun equals(other: Any?): Boolean {
			if (this === other) return true
			if (other !is Property) return false

			return name == other.name &&
				receiverParameterType == other.receiverParameterType
		}


		override fun toString() =
			if (receiverParameterType != null)
				"[property ${receiverParameterType.name}.$name]"
			else
				"[property $name]"
	}


	class Type(
		name: MTypeName
	) : MLocalId() {

		private val name = name.kotlin


		override fun hashCode() =
			name.hashCode()


		override fun equals(other: Any?) =
			name == (other as? Type)?.name


		override fun toString() =
			"[type $name]"
	}


	companion object
}
