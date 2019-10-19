package io.fluidsonic.meta


sealed class MTypeReference : MAnnotatable {

	abstract val flexibleTypeUpperBound: MFlexibleTypeUpperBound?
	abstract val isNullable: Boolean


	companion object;


	data class Class(
		val abbreviatedType: MTypeReference?,
		override val annotations: List<MAnnotation>,
		val arguments: List<MTypeArgument>,
		override val flexibleTypeUpperBound: MFlexibleTypeUpperBound?,
		override val isNullable: Boolean,
		val isRaw: Boolean,
		val name: MQualifiedTypeName,
		val outerType: MTypeReference?
	) : MTypeReference() {

		override fun toString() =
			MetaCodeWriter.write(this)


		companion object
	}


	data class Function(
		override val annotations: List<MAnnotation>,
		override val flexibleTypeUpperBound: MFlexibleTypeUpperBound?,
		override val isNullable: Boolean,
		val isSuspend: Boolean,
		val parameterArguments: List<MTypeArgument>,
		val receiverArgument: MTypeArgument?,
		val returnArgument: MTypeArgument
	) : MTypeReference() {

		override fun toString() =
			MetaCodeWriter.write(this)


		companion object
	}


	data class TypeAlias(
		val abbreviatedType: MTypeReference?,
		override val annotations: List<MAnnotation>,
		val arguments: List<MTypeArgument>,
		override val flexibleTypeUpperBound: MFlexibleTypeUpperBound?,
		override val isNullable: Boolean,
		val isRaw: Boolean,
		val name: MQualifiedTypeName
	) : MTypeReference() {

		override fun toString() =
			MetaCodeWriter.write(this)


		companion object
	}


	data class TypeParameter(
		override val annotations: List<MAnnotation>,
		val arguments: List<MTypeArgument>,
		override val flexibleTypeUpperBound: MFlexibleTypeUpperBound?,
		val id: MTypeParameterId,
		val isRaw: Boolean,
		override val isNullable: Boolean
	) : MTypeReference() {

		override fun toString() =
			MetaCodeWriter.write(this)


		companion object
	}
}


internal fun MTypeReference?.equalsExceptForNullability(other: MTypeReference?): Boolean {
	if (this === other) return true
	if (this == null || other == null) return false

	return when (this) {
		is MTypeReference.Class -> {
			if (other !is MTypeReference.Class) return false

			name == other.name &&
				outerType.equalsExceptForNullability(other.outerType) &&
				arguments.size == other.arguments.size &&
				arguments.zip(other.arguments).all { (a, b) -> a.equalsExceptForNullability(b) }
		}

		is MTypeReference.Function -> {
			if (other !is MTypeReference.Function) return false

			isSuspend == other.isSuspend &&
				returnArgument.equalsExceptForNullability(other.returnArgument) &&
				receiverArgument.equalsExceptForNullability(other.receiverArgument) &&
				parameterArguments.size == other.parameterArguments.size &&
				parameterArguments.zip(other.parameterArguments).all { (a, b) -> a.equalsExceptForNullability(b) }
		}

		is MTypeReference.TypeAlias -> {
			if (other !is MTypeReference.TypeAlias) return false

			name == other.name &&
				arguments.size == other.arguments.size &&
				arguments.zip(other.arguments).all { (a, b) -> a.equalsExceptForNullability(b) }
		}

		is MTypeReference.TypeParameter -> {
			if (other !is MTypeReference.TypeParameter) return false

			id == other.id &&
				arguments.size == other.arguments.size &&
				arguments.zip(other.arguments).all { (a, b) -> a.equalsExceptForNullability(b) }
		}
	}
}


val MTypeReference.name
	get() = when (this) {
		is MTypeReference.Class -> name
		is MTypeReference.Function -> null
		is MTypeReference.TypeAlias -> name
		is MTypeReference.TypeParameter -> null
	}
