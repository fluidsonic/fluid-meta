package io.fluidsonic.meta

import io.fluidsonic.meta.MVersionRequirement.*
import kotlinx.metadata.*


internal class MVersionRequirementBuilder : KmVersionRequirementVisitor() {

	private var errorCode: Int? = null
	private var level: Level? = null
	private var message: String? = null
	private var kind: Kind? = null
	private var version: MVersion? = null


	fun build() = MVersionRequirement(
		errorCode = errorCode,
		level = level ?: throw MetaException("Value requirement has no level"),
		message = message,
		kind = kind ?: throw MetaException("Value requirement has no kind"),
		version = version ?: throw MetaException("Value requirement has no version")
	)


	override fun visit(kind: KmVersionRequirementVersionKind, level: KmVersionRequirementLevel, errorCode: Int?, message: String?) {
		this.errorCode = errorCode
		this.kind = when (kind) {
			KmVersionRequirementVersionKind.API_VERSION -> Kind.API
			KmVersionRequirementVersionKind.COMPILER_VERSION -> Kind.COMPILER
			KmVersionRequirementVersionKind.LANGUAGE_VERSION -> Kind.LANGUAGE
		}
		this.level = when (level) {
			KmVersionRequirementLevel.ERROR -> Level.ERROR
			KmVersionRequirementLevel.HIDDEN -> Level.HIDDEN
			KmVersionRequirementLevel.WARNING -> Level.WARNING
		}
		this.message = message
	}


	override fun visitVersion(major: Int, minor: Int, patch: Int) {
		this.version = MVersion(major, minor, patch)
	}
}
