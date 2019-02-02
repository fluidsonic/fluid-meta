package com.github.fluidsonic.fluid.meta

import com.github.fluidsonic.fluid.meta.MAnnotationArgument.*
import kotlinx.metadata.KmAnnotationArgument


sealed class MAnnotationArgument<out Value : Any> {

	abstract val value: Value

	data class AnnotationValue(override val value: MAnnotation) : MAnnotationArgument<MAnnotation>()
	data class ArrayValue(override val value: List<MAnnotationArgument<*>>) : MAnnotationArgument<List<MAnnotationArgument<*>>>()
	data class BooleanValue(override val value: Boolean) : MAnnotationArgument<Boolean>()
	data class ByteValue(override val value: Byte) : MAnnotationArgument<Byte>()
	data class CharValue(override val value: Char) : MAnnotationArgument<Char>()
	data class DoubleValue(override val value: Double) : MAnnotationArgument<Double>()
	data class FloatValue(override val value: Float) : MAnnotationArgument<Float>()
	data class IntValue(override val value: Int) : MAnnotationArgument<Int>()
	data class KClassValue(override val value: MQualifiedTypeName) : MAnnotationArgument<MQualifiedTypeName>()
	data class LongValue(override val value: Long) : MAnnotationArgument<Long>()
	data class ShortValue(override val value: Short) : MAnnotationArgument<Short>()
	data class StringValue(override val value: String) : MAnnotationArgument<String>()
	data class UByteValue(override val value: UByte) : MAnnotationArgument<UByte>()
	data class UIntValue(override val value: UInt) : MAnnotationArgument<UInt>()
	data class ULongValue(override val value: ULong) : MAnnotationArgument<ULong>()
	data class UShortValue(override val value: UShort) : MAnnotationArgument<UShort>()

	data class EnumValue(val className: MQualifiedTypeName, val entryName: String) : MAnnotationArgument<String>() {
		override val value = "$className.$entryName"
	}
}


@Suppress("FunctionName")
internal fun MAnnotationArgument(argument: KmAnnotationArgument<*>): MAnnotationArgument<*> = argument.run {
	when (this) {
		is KmAnnotationArgument.AnnotationValue -> AnnotationValue(MAnnotation(value))
		is KmAnnotationArgument.ArrayValue -> ArrayValue(value.map { MAnnotationArgument(it) })
		is KmAnnotationArgument.BooleanValue -> BooleanValue(value)
		is KmAnnotationArgument.ByteValue -> ByteValue(value)
		is KmAnnotationArgument.CharValue -> CharValue(value)
		is KmAnnotationArgument.DoubleValue -> DoubleValue(value)
		is KmAnnotationArgument.EnumValue -> EnumValue(MQualifiedTypeName.fromKotlinInternal(enumClassName), enumEntryName)
		is KmAnnotationArgument.FloatValue -> FloatValue(value)
		is KmAnnotationArgument.IntValue -> IntValue(value)
		is KmAnnotationArgument.KClassValue -> KClassValue(MQualifiedTypeName.fromKotlinInternal(value))
		is KmAnnotationArgument.LongValue -> LongValue(value)
		is KmAnnotationArgument.ShortValue -> ShortValue(value)
		is KmAnnotationArgument.StringValue -> StringValue(value)
		is KmAnnotationArgument.UByteValue -> UByteValue(value.toUByte())
		is KmAnnotationArgument.UIntValue -> UIntValue(value.toUInt())
		is KmAnnotationArgument.ULongValue -> ULongValue(value.toULong())
		is KmAnnotationArgument.UShortValue -> UShortValue(value.toUShort())
	}
}
