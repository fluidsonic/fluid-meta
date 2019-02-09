package com.github.fluidsonic.fluid.meta

import kotlinx.metadata.Flag
import kotlinx.metadata.Flags


enum class MVisibility {

	INTERNAL,
	LOCAL,
	PRIVATE,
	PRIVATE_TO_THIS,
	PROTECTED,
	PUBLIC;


	override fun toString() =
		MetaCodeWriter.write(this)


	companion object {

		internal fun forFlags(flags: Flags) =
			when {
				Flag.IS_INTERNAL(flags) -> INTERNAL
				Flag.IS_LOCAL(flags) -> LOCAL
				Flag.IS_PRIVATE(flags) -> PRIVATE
				Flag.IS_PRIVATE_TO_THIS(flags) -> PRIVATE_TO_THIS
				Flag.IS_PROTECTED(flags) -> PROTECTED
				Flag.IS_PUBLIC(flags) -> PUBLIC
				else -> throw MetaException("unknown visibility in flags ${flags.toString(16)}")
			}
	}
}


internal val MVisibility.flag
	get() = when (this) {
		MVisibility.INTERNAL -> Flag.IS_INTERNAL
		MVisibility.LOCAL -> Flag.IS_LOCAL
		MVisibility.PRIVATE -> Flag.IS_PRIVATE
		MVisibility.PRIVATE_TO_THIS -> Flag.IS_PRIVATE_TO_THIS
		MVisibility.PROTECTED -> Flag.IS_PROTECTED
		MVisibility.PUBLIC -> Flag.IS_PUBLIC
	}
