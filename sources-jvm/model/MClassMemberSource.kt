package io.fluidsonic.meta

import kotlin.metadata.*


/** Represents how a class member was introduced (declared, delegated, synthesized, or fake override). */
public enum class MClassMemberSource {

	DECLARATION,
	DELEGATION,
	FAKE_OVERRIDE,
	SYNTHESIZED;


	override fun toString(): String =
		MetaCodeWriter.write(this)


	public companion object {

		internal fun from(kind: MemberKind): MClassMemberSource =
			when (kind) {
				MemberKind.DECLARATION -> DECLARATION
				MemberKind.DELEGATION -> DELEGATION
				MemberKind.FAKE_OVERRIDE -> FAKE_OVERRIDE
				MemberKind.SYNTHESIZED -> SYNTHESIZED
			}
	}
}
