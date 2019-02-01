package com.github.fluidsonic.fluid.meta

import java.util.Objects


class MVersionRequirement internal constructor(
	val errorCode: Int?,
	val level: Level,
	val message: String?,
	val kind: Kind,
	val version: MVersion
) {

	override fun equals(other: Any?): Boolean {
		if (other === this) return true
		if (other !is MVersionRequirement) return false

		return errorCode == other.errorCode &&
			level == other.level &&
			message == other.message &&
			kind == other.kind &&
			version == other.version
	}


	override fun hashCode() =
		Objects.hash(
			errorCode,
			level,
			message,
			kind,
			version
		)


	override fun toString() = typeToString(
		"errorCode" to errorCode,
		"level" to level,
		"message" to message,
		"kind" to kind,
		"version" to version
	)


	companion object


	@Suppress("EnumEntryName")
	enum class Kind {

		API_VERSION,
		COMPILER_VERSION,
		LANGUAGE_VERSION;


		override fun toString() =
			when (this) {
				API_VERSION -> "api version"
				COMPILER_VERSION -> "compiler version"
				LANGUAGE_VERSION -> "language version"
			}


		companion object
	}


	@Suppress("EnumEntryName")
	enum class Level {
		ERROR,
		HIDDEN,
		WARNING;


		override fun toString() =
			when (this) {
				ERROR -> "error"
				HIDDEN -> "hidden"
				WARNING -> "warning"
			}


		companion object
	}
}
