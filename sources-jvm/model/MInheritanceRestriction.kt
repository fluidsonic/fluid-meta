package io.fluidsonic.meta

import kotlin.metadata.*


/** Represents the inheritance restriction (modality) of a Kotlin declaration. */
public enum class MInheritanceRestriction {

	ABSTRACT,
	FINAL,
	OPEN;


	override fun toString(): String =
		MetaCodeWriter.write(this)


	public companion object {

		internal fun from(modality: Modality): MInheritanceRestriction =
			when (modality) {
				Modality.ABSTRACT -> ABSTRACT
				Modality.FINAL -> FINAL
				Modality.OPEN -> OPEN
				Modality.SEALED -> ABSTRACT
			}
	}
}
