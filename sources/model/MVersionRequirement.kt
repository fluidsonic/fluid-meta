package com.github.fluidsonic.fluid.meta


data class MVersionRequirement(
	val errorCode: Int?,
	val kind: Kind,
	val level: Level,
	val message: String?,
	val version: MVersion
) {

	override fun toString() =
		MetaCodeWriter.write(this)


	companion object;


	enum class Kind {

		API,
		COMPILER,
		LANGUAGE;


		override fun toString() =
			when (this) {
				API -> "api"
				COMPILER -> "compiler"
				LANGUAGE -> "language"
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
