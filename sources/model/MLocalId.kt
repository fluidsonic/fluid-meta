package io.fluidsonic.meta

import java.util.*


sealed class MLocalId {

	class Constructor(
		valueParameters: List<MValueParameter>
	) : MLocalId() {

		private val valueParameterIds = valueParameters.map { it.type.localId }
		private val hashCode = Objects.hash(*valueParameterIds.toTypedArray())


		override fun hashCode() =
			hashCode


		override fun equals(other: Any?) =
			valueParameterIds == (other as? Constructor)?.valueParameterIds


		override fun toString() =
			"MLocalId [constructor(${valueParameterIds.joinToString()})]"
	}


	class Function(
		name: MFunctionName,
		receiverParameterType: MTypeReference?,
		valueParameters: List<MValueParameter>
	) : MLocalId() {

		private val name = name.toString()
		private val receiverParameterId = receiverParameterType?.localId
		private val valueParameterIds = valueParameters.map { it.type.localId }

		private val hashCode =
			Objects.hash(*(
				listOf(name, receiverParameterType) +
					valueParameters.fold(0) { hashCode, parameter -> hashCode xor parameter.hashCode() }
				).toTypedArray()
			)


		override fun hashCode() =
			hashCode


		override fun equals(other: Any?): Boolean {
			if (this === other) return true
			if (other !is Function) return false

			return name == other.name &&
				receiverParameterId == other.receiverParameterId &&
				valueParameterIds == other.valueParameterIds
		}


		override fun toString() =
			if (receiverParameterId != null)
				"MLocalId [fun $receiverParameterId.$name(${valueParameterIds.joinToString()})]"
			else
				"MLocalId [fun $name(${valueParameterIds.joinToString()})]"
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
			"MLocalId [package $name]"
	}


	class Property(
		name: MVariableName,
		receiverParameterType: MTypeReference?
	) : MLocalId() {

		private val name = name.toString()
		private val receiverParameterId = receiverParameterType?.localId


		override fun hashCode() =
			Objects.hash(name, receiverParameterId)


		override fun equals(other: Any?): Boolean {
			if (this === other) return true
			if (other !is Property) return false

			return name == other.name &&
				receiverParameterId == other.receiverParameterId
		}


		override fun toString() =
			if (receiverParameterId != null)
				"MLocalId [property $receiverParameterId.$name]"
			else
				"MLocalId [property $name]"
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
			"MLocalId [type $name]"
	}


	companion object
}


private val MTypeReference.localId
	get() = when (this) {
		is MTypeReference.Class -> name.toString()
		is MTypeReference.Function -> toString()
		is MTypeReference.TypeAlias -> name.toString()
		is MTypeReference.TypeParameter -> "<$id>"
	}
