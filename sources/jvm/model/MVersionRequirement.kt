package io.fluidsonic.meta


public data class MVersionRequirement(
	val errorCode: Int?,
	val kind: Kind,
	val level: Level,
	val message: String?,
	val version: MVersion
) {

	override fun toString(): String =
		MetaCodeWriter.write(this)


	public companion object;


	public enum class Kind {

		API,
		COMPILER,
		LANGUAGE;


		override fun toString(): String =
			when (this) {
				API -> "api"
				COMPILER -> "compiler"
				LANGUAGE -> "language"
			}


		public companion object
	}


	public enum class Level {
		ERROR,
		HIDDEN,
		WARNING;


		override fun toString(): String =
			when (this) {
				ERROR -> "error"
				HIDDEN -> "hidden"
				WARNING -> "warning"
			}


		public companion object
	}
}
