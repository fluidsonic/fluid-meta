package com.github.fluidsonic.fluid.meta

import kotlinx.metadata.Flag
import kotlinx.metadata.Flags


@Suppress("EnumEntryName")
enum class MModality {

	ABSTRACT,
	FINAL,
	OPEN,
	SEALED;


	override fun toString() =
		when (this) {
			ABSTRACT -> "abstract"
			FINAL -> "final"
			OPEN -> "open"
			SEALED -> "sealed"
		}


	companion object {

		internal fun forFlags(flags: Flags) =
			when {
				Flag.IS_ABSTRACT(flags) -> ABSTRACT
				Flag.IS_FINAL(flags) -> FINAL
				Flag.IS_OPEN(flags) -> OPEN
				Flag.IS_SEALED(flags) -> SEALED
				else -> null
			}
	}
}
