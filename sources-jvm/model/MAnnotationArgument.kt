package io.fluidsonic.meta

import io.fluidsonic.meta.MAnnotationArgument.*
import kotlin.metadata.*


/** Represents a typed argument value of a Kotlin annotation. */
public sealed class MAnnotationArgument<out Value : Any> {

	/** The argument value. */
	public abstract val value: Value


	/** An annotation used as an annotation argument value. */
	public data class AnnotationValue(override val value: MAnnotation) : MAnnotationArgument<MAnnotation>() {

		override fun toString(): String =
			MetaCodeWriter.write(this)
	}

	/** An array used as an annotation argument value. */
	public data class ArrayValue(override val value: List<MAnnotationArgument<*>>) : MAnnotationArgument<List<MAnnotationArgument<*>>>() {

		override fun toString(): String =
			MetaCodeWriter.write(this)
	}

	/** A [Boolean] annotation argument value. */
	public data class BooleanValue(override val value: Boolean) : MAnnotationArgument<Boolean>() {

		override fun toString(): String =
			MetaCodeWriter.write(this)
	}

	/** A [Byte] annotation argument value. */
	public data class ByteValue(override val value: Byte) : MAnnotationArgument<Byte>() {

		override fun toString(): String =
			MetaCodeWriter.write(this)
	}

	/** A [Char] annotation argument value. */
	public data class CharValue(override val value: Char) : MAnnotationArgument<Char>() {

		override fun toString(): String =
			MetaCodeWriter.write(this)
	}

	/** A [Double] annotation argument value. */
	public data class DoubleValue(override val value: Double) : MAnnotationArgument<Double>() {

		override fun toString(): String =
			MetaCodeWriter.write(this)
	}

	/** An enum entry annotation argument value. */
	public data class EnumValue(val className: MQualifiedTypeName, val entryName: MEnumEntryName) : MAnnotationArgument<String>() {

		override val value: String = "$className.$entryName"

		override fun toString(): String =
			MetaCodeWriter.write(this)
	}

	/** A [Float] annotation argument value. */
	public data class FloatValue(override val value: Float) : MAnnotationArgument<Float>() {

		override fun toString(): String =
			MetaCodeWriter.write(this)
	}

	/** An [Int] annotation argument value. */
	public data class IntValue(override val value: Int) : MAnnotationArgument<Int>() {

		override fun toString(): String =
			MetaCodeWriter.write(this)
	}

	/** A `KClass` annotation argument value, represented by the class name. */
	public data class KClassValue(override val value: MQualifiedTypeName) : MAnnotationArgument<MQualifiedTypeName>() {

		override fun toString(): String =
			MetaCodeWriter.write(this)
	}

	/** A [Long] annotation argument value. */
	public data class LongValue(override val value: Long) : MAnnotationArgument<Long>() {

		override fun toString(): String =
			MetaCodeWriter.write(this)
	}

	/** A [Short] annotation argument value. */
	public data class ShortValue(override val value: Short) : MAnnotationArgument<Short>() {

		override fun toString(): String =
			MetaCodeWriter.write(this)
	}

	/** A [String] annotation argument value. */
	public data class StringValue(override val value: String) : MAnnotationArgument<String>() {

		override fun toString(): String =
			MetaCodeWriter.write(this)
	}

	/** A [UByte] annotation argument value. */
	public data class UByteValue(override val value: UByte) : MAnnotationArgument<UByte>() {

		override fun toString(): String =
			MetaCodeWriter.write(this)
	}

	/** A [UInt] annotation argument value. */
	public data class UIntValue(override val value: UInt) : MAnnotationArgument<UInt>() {

		override fun toString(): String =
			MetaCodeWriter.write(this)
	}

	/** A [ULong] annotation argument value. */
	public data class ULongValue(override val value: ULong) : MAnnotationArgument<ULong>() {

		override fun toString(): String =
			MetaCodeWriter.write(this)
	}

	/** A [UShort] annotation argument value. */
	public data class UShortValue(override val value: UShort) : MAnnotationArgument<UShort>() {

		override fun toString(): String =
			MetaCodeWriter.write(this)
	}


	public companion object
}


@Suppress("FunctionName")
internal fun MAnnotationArgument(argument: KmAnnotationArgument): MAnnotationArgument<*> = argument.run {
	when (this) {
		is KmAnnotationArgument.AnnotationValue -> AnnotationValue(MAnnotation(annotation))
		is KmAnnotationArgument.ArrayValue -> ArrayValue(elements.map { MAnnotationArgument(it) })
		is KmAnnotationArgument.BooleanValue -> BooleanValue(value)
		is KmAnnotationArgument.ByteValue -> ByteValue(value)
		is KmAnnotationArgument.CharValue -> CharValue(value)
		is KmAnnotationArgument.DoubleValue -> DoubleValue(value)
		is KmAnnotationArgument.EnumValue -> EnumValue(MQualifiedTypeName.fromKotlinInternal(enumClassName), MEnumEntryName(enumEntryName))
		is KmAnnotationArgument.FloatValue -> FloatValue(value)
		is KmAnnotationArgument.IntValue -> IntValue(value)
		is KmAnnotationArgument.KClassValue -> KClassValue(MQualifiedTypeName.fromKotlinInternal(className)) // FIXME Support `arrayDimensionCount`.
		is KmAnnotationArgument.ArrayKClassValue -> KClassValue(MQualifiedTypeName.fromKotlinInternal(className)) // FIXME Support `arrayDimensionCount`.
		is KmAnnotationArgument.LongValue -> LongValue(value)
		is KmAnnotationArgument.ShortValue -> ShortValue(value)
		is KmAnnotationArgument.StringValue -> StringValue(value)
		is KmAnnotationArgument.UByteValue -> UByteValue(value)
		is KmAnnotationArgument.UIntValue -> UIntValue(value)
		is KmAnnotationArgument.ULongValue -> ULongValue(value)
		is KmAnnotationArgument.UShortValue -> UShortValue(value)
	}
}
