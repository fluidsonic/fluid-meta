package com.github.fluidsonic.fluid.meta

import com.github.fluidsonic.fluid.meta.MAnnotationArgument.*
import kotlinx.metadata.KmAnnotationArgument


sealed class MAnnotationArgument<out Value : Any> {

	abstract val value: Value


	data class AnnotationValue(override val value: MAnnotation) : MAnnotationArgument<MAnnotation>() {

		override fun toString() =
			MetaCodeWriter.write(this)
	}

	data class ArrayValue(override val value: List<MAnnotationArgument<*>>) : MAnnotationArgument<List<MAnnotationArgument<*>>>() {

		override fun toString() =
			MetaCodeWriter.write(this)
	}

	data class BooleanValue(override val value: Boolean) : MAnnotationArgument<Boolean>() {

		override fun toString() =
			MetaCodeWriter.write(this)
	}

	data class ByteValue(override val value: Byte) : MAnnotationArgument<Byte>() {

		override fun toString() =
			MetaCodeWriter.write(this)
	}

	data class CharValue(override val value: Char) : MAnnotationArgument<Char>() {

		override fun toString() =
			MetaCodeWriter.write(this)
	}

	data class DoubleValue(override val value: Double) : MAnnotationArgument<Double>() {

		override fun toString() =
			MetaCodeWriter.write(this)
	}

	data class EnumValue(val className: MQualifiedTypeName, val entryName: MEnumEntryName) : MAnnotationArgument<String>() {

		override val value = "$className.$entryName"

		override fun toString() =
			MetaCodeWriter.write(this)
	}

	data class FloatValue(override val value: Float) : MAnnotationArgument<Float>() {

		override fun toString() =
			MetaCodeWriter.write(this)
	}

	data class IntValue(override val value: Int) : MAnnotationArgument<Int>() {

		override fun toString() =
			MetaCodeWriter.write(this)
	}

	data class KClassValue(override val value: MQualifiedTypeName) : MAnnotationArgument<MQualifiedTypeName>() {

		override fun toString() =
			MetaCodeWriter.write(this)
	}

	data class LongValue(override val value: Long) : MAnnotationArgument<Long>() {

		override fun toString() =
			MetaCodeWriter.write(this)
	}

	data class ShortValue(override val value: Short) : MAnnotationArgument<Short>() {

		override fun toString() =
			MetaCodeWriter.write(this)
	}

	data class StringValue(override val value: String) : MAnnotationArgument<String>() {

		override fun toString() =
			MetaCodeWriter.write(this)
	}

	// TODO use property generic types once fixed: https://youtrack.jetbrains.com/issue/KT-29655

	data class UByteValue(override val value: Byte) : MAnnotationArgument<Byte>() {

		override fun toString() =
			MetaCodeWriter.write(this)
	}

	data class UIntValue(override val value: Int) : MAnnotationArgument<Int>() {

		override fun toString() =
			MetaCodeWriter.write(this)
	}

	data class ULongValue(override val value: Long) : MAnnotationArgument<Long>() {

		override fun toString() =
			MetaCodeWriter.write(this)
	}

	data class UShortValue(override val value: Short) : MAnnotationArgument<Short>() {

		override fun toString() =
			MetaCodeWriter.write(this)
	}


	companion object
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
		is KmAnnotationArgument.EnumValue -> EnumValue(MQualifiedTypeName.fromKotlinInternal(enumClassName), MEnumEntryName(enumEntryName))
		is KmAnnotationArgument.FloatValue -> FloatValue(value)
		is KmAnnotationArgument.IntValue -> IntValue(value)
		is KmAnnotationArgument.KClassValue -> KClassValue(MQualifiedTypeName.fromKotlinInternal(value))
		is KmAnnotationArgument.LongValue -> LongValue(value)
		is KmAnnotationArgument.ShortValue -> ShortValue(value)
		is KmAnnotationArgument.StringValue -> StringValue(value)
		is KmAnnotationArgument.UByteValue -> UByteValue(value)
		is KmAnnotationArgument.UIntValue -> UIntValue(value)
		is KmAnnotationArgument.ULongValue -> ULongValue(value)
		is KmAnnotationArgument.UShortValue -> UShortValue(value)
	}
}
