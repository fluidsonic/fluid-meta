package com.github.fluidsonic.fluid.meta

import kotlinx.metadata.Flag
import kotlinx.metadata.Flags


enum class MClassMemberSource {

	DECLARATION,
	DELEGATION,
	FAKE_OVERRIDE,
	SYNTHESIZED;


	override fun toString() =
		MetaCodeWriter.write(this)


	companion object {

		internal fun forFlags(flags: Flags) =
			when {
				// same flags are used for functions and properties
				Flag.Function.IS_DECLARATION(flags) -> MClassMemberSource.DECLARATION
				Flag.Function.IS_FAKE_OVERRIDE(flags) -> MClassMemberSource.FAKE_OVERRIDE
				Flag.Function.IS_DELEGATION(flags) -> MClassMemberSource.DELEGATION
				Flag.Function.IS_SYNTHESIZED(flags) -> MClassMemberSource.SYNTHESIZED
				else -> throw MetaException("unknown member source in flags ${flags.toString(16)}")
			}
	}
}
