package io.fluidsonic.meta

import kotlinx.metadata.*


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
				Flag.Function.IS_DECLARATION(flags) -> DECLARATION
				Flag.Function.IS_FAKE_OVERRIDE(flags) -> FAKE_OVERRIDE
				Flag.Function.IS_DELEGATION(flags) -> DELEGATION
				Flag.Function.IS_SYNTHESIZED(flags) -> SYNTHESIZED
				else -> throw MetaException("unknown member source in flags ${flags.toString(16)}")
			}
	}
}
