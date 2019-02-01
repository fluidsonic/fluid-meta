package com.github.fluidsonic.fluid.meta

import kotlinx.metadata.Flag
import kotlinx.metadata.Flags


@Suppress("EnumEntryName")
enum class MVisibility {

	INTERNAL,
	LOCAL,
	PRIVATE,
	PRIVATE_TO_THIS,
	PROTECTED,
	PUBLIC;


	override fun toString() =
		when (this) {
			INTERNAL -> "internal"
			LOCAL -> "local"
			PRIVATE -> "private"
			PRIVATE_TO_THIS -> "private(this)"
			PROTECTED -> "protected"
			PUBLIC -> "public"
		}


	companion object {

		internal fun forFlags(flags: Flags) =
			when {
				Flag.IS_INTERNAL(flags) -> INTERNAL
				Flag.IS_LOCAL(flags) -> LOCAL
				Flag.IS_PRIVATE(flags) -> PRIVATE
				Flag.IS_PRIVATE_TO_THIS(flags) -> PRIVATE_TO_THIS
				Flag.IS_PROTECTED(flags) -> PROTECTED
				Flag.IS_PUBLIC(flags) -> PUBLIC
				else -> null
			}
	}
}
