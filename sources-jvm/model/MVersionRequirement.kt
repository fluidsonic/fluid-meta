package io.fluidsonic.meta


/** Represents a version requirement on a Kotlin declaration, indicating the minimum version needed. */
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


	/** The kind of version being required (API, compiler, or language). */
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


	/** The severity level of the version requirement diagnostic. */
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
