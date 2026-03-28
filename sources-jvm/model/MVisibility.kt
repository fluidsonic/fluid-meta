package io.fluidsonic.meta

import kotlin.metadata.*


/** Represents the visibility of a Kotlin declaration. */
public enum class MVisibility {

	INTERNAL,
	LOCAL,
	PRIVATE,
	PRIVATE_TO_THIS,
	PROTECTED,
	PUBLIC;


	override fun toString(): String =
		MetaCodeWriter.write(this)


	public companion object {

		internal fun from(visibility: Visibility): MVisibility =
			when (visibility) {
				Visibility.INTERNAL -> INTERNAL
				Visibility.LOCAL -> LOCAL
				Visibility.PRIVATE -> PRIVATE
				Visibility.PRIVATE_TO_THIS -> PRIVATE_TO_THIS
				Visibility.PROTECTED -> PROTECTED
				Visibility.PUBLIC -> PUBLIC
			}
	}
}
