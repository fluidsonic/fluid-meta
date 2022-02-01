package io.fluidsonic.meta

import kotlinx.metadata.*


private val allFlags = mapOf(
	Flag to mapOf(
		Flag.HAS_ANNOTATIONS to "HAS_ANNOTATIONS",
		Flag.IS_ABSTRACT to "IS_ABSTRACT",
		Flag.IS_FINAL to "IS_FINAL",
		Flag.IS_INTERNAL to "IS_INTERNAL",
		Flag.IS_LOCAL to "IS_LOCAL",
		Flag.IS_OPEN to "IS_OPEN",
		Flag.IS_PRIVATE to "IS_PRIVATE",
		Flag.IS_PRIVATE_TO_THIS to "IS_PRIVATE_TO_THIS",
		Flag.IS_PROTECTED to "IS_PROTECTED",
		Flag.IS_PUBLIC to "IS_PUBLIC",
		Flag.IS_SEALED to "IS_SEALED"
	),
	Flag.Class to mapOf(
		Flag.Class.IS_ANNOTATION_CLASS to "IS_ANNOTATION_CLASS",
		Flag.Class.IS_CLASS to "IS_CLASS",
		Flag.Class.IS_COMPANION_OBJECT to "IS_COMPANION_OBJECT",
		Flag.Class.IS_DATA to "IS_DATA",
		Flag.Class.IS_ENUM_CLASS to "IS_ENUM_CLASS",
		Flag.Class.IS_ENUM_ENTRY to "IS_ENUM_ENTRY",
		Flag.Class.IS_EXPECT to "IS_EXPECT",
		Flag.Class.IS_EXTERNAL to "IS_EXTERNAL",
		Flag.Class.IS_FUN to "IS_FUN",
		Flag.Class.IS_INNER to "IS_INNER",
		Flag.Class.IS_INTERFACE to "IS_INTERFACE",
		Flag.Class.IS_OBJECT to "IS_OBJECT",
		Flag.Class.IS_VALUE to "IS_VALUE",
	),
	Flag.Constructor to mapOf(
		Flag.Constructor.IS_SECONDARY to "IS_SECONDARY"
	),
	Flag.EffectExpression to mapOf(
		Flag.EffectExpression.IS_NEGATED to "IS_NEGATED",
		Flag.EffectExpression.IS_NULL_CHECK_PREDICATE to "IS_NULL_CHECK_PREDICATE"
	),
	Flag.Function to mapOf(
		Flag.Function.IS_DECLARATION to "IS_DECLARATION",
		Flag.Function.IS_DELEGATION to "IS_DELEGATION",
		Flag.Function.IS_EXPECT to "IS_EXPECT",
		Flag.Function.IS_EXTERNAL to "IS_EXTERNAL",
		Flag.Function.IS_FAKE_OVERRIDE to "IS_FAKE_OVERRIDE",
		Flag.Function.IS_INFIX to "IS_INFIX",
		Flag.Function.IS_INLINE to "IS_INLINE",
		Flag.Function.IS_OPERATOR to "IS_OPERATOR",
		Flag.Function.IS_SUSPEND to "IS_SUSPEND",
		Flag.Function.IS_SYNTHESIZED to "IS_SYNTHESIZED",
		Flag.Function.IS_TAILREC to "IS_TAILREC"
	),
	Flag.Property to mapOf(
		Flag.Property.HAS_CONSTANT to "HAS_CONSTANT",
		Flag.Property.HAS_GETTER to "HAS_GETTER",
		Flag.Property.HAS_SETTER to "HAS_SETTER",
		Flag.Property.IS_CONST to "IS_CONST",
		Flag.Property.IS_DECLARATION to "IS_DECLARATION",
		Flag.Property.IS_DELEGATED to "IS_DELEGATED",
		Flag.Property.IS_DELEGATION to "IS_DELEGATION",
		Flag.Property.IS_EXPECT to "IS_EXPECT",
		Flag.Property.IS_EXTERNAL to "IS_EXTERNAL",
		Flag.Property.IS_FAKE_OVERRIDE to "IS_FAKE_OVERRIDE",
		Flag.Property.IS_LATEINIT to "IS_LATEINIT",
		Flag.Property.IS_SYNTHESIZED to "IS_SYNTHESIZED",
		Flag.Property.IS_VAR to "IS_VAR"
	),
	Flag.PropertyAccessor to mapOf(
		Flag.PropertyAccessor.IS_EXTERNAL to "IS_EXTERNAL",
		Flag.PropertyAccessor.IS_INLINE to "IS_INLINE",
		Flag.PropertyAccessor.IS_NOT_DEFAULT to "IS_NOT_DEFAULT"
	),
	Flag.Type to mapOf(
		Flag.Type.IS_NULLABLE to "IS_NULLABLE",
		Flag.Type.IS_SUSPEND to "IS_SUSPEND"
	),
	Flag.TypeParameter to mapOf(
		Flag.TypeParameter.IS_REIFIED to "IS_REIFIED"
	),
	Flag.ValueParameter to mapOf(
		Flag.ValueParameter.DECLARES_DEFAULT_VALUE to "DECLARES_DEFAULT_VALUE",
		Flag.ValueParameter.IS_CROSSINLINE to "IS_CROSSINLINE",
		Flag.ValueParameter.IS_NOINLINE to "IS_NOINLINE"
	)
)


internal fun Flag.Common.from(flags: Flags) =
	allFlags[this]
		?.filter { it.key(flags) }
		?.map { it.value }
		?: emptyList()


private fun Flag.Common.from(flags: Flags, group: Any) =
	Flag.from(flags) +
		(allFlags[group]
			?.filter { it.key(flags) }
			?.map { it.value }
			?: emptyList())


internal fun Flag.Class.from(flags: Flags) =
	Flag.from(flags, this)


internal fun Flag.Constructor.from(flags: Flags) =
	Flag.from(flags, this)


internal fun Flag.EffectExpression.from(flags: Flags) =
	Flag.from(flags, this)


internal fun Flag.Function.from(flags: Flags) =
	Flag.from(flags, this)


internal fun Flag.Property.from(flags: Flags) =
	Flag.from(flags, this)


internal fun Flag.PropertyAccessor.from(flags: Flags) =
	Flag.from(flags, this)


internal fun Flag.Type.from(flags: Flags) =
	Flag.from(flags, this)


internal fun Flag.TypeParameter.from(flags: Flags) =
	Flag.from(flags, this)


internal fun Flag.ValueParameter.from(flags: Flags) =
	Flag.from(flags, this)


private fun StringBuilder.appendLines(value: Any?, indentation: String) {
	value.toString()
		.splitToSequence('\n')
		.withIndex()
		.forEach { (index, line) ->
			if (index > 0)
				append(indentation)

			append(line)
		}
}
