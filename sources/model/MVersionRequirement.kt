package com.github.fluidsonic.fluid.meta


data class MVersionRequirement(
	val errorCode: Int?,
	val kind: Kind,
	val level: Level,
	val message: String?,
	val version: MVersion
) {

	override fun toString() = typeToString(
		"errorCode" to errorCode,
		"kind" to kind,
		"level" to level,
		"message" to message,
		"version" to version
	)


	companion object;


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
