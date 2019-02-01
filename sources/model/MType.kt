package com.github.fluidsonic.fluid.meta

import kotlinx.metadata.Flag
import kotlinx.metadata.Flags
import java.util.Objects


sealed class MType


class MClass internal constructor(
	val anonymousObjectOriginName: MTypeName?,
	val companion: MTypeName?,
	val constructors: List<MConstructor>,
	val enumEntries: List<MEnumEntry>,
	private val flags: Flags,
	val functions: List<MFunction>,
	val localDelegatedProperties: List<MProperty>,
	val name: MTypeName?,
	val nestedClasses: List<MTypeName>,
	val properties: List<MProperty>,
	val sealedSubclasses: List<MTypeName>,
	val supertype: MTypeReference?,
	val typeAliases: List<MTypeAlias>,
	val typeParameters: List<MTypeParameter>,
	val versionRequirement: MVersionRequirement?
) : MType() {

	val hasAnnotations
		get() = Flag.HAS_ANNOTATIONS(flags)

	val isExpect
		get() = Flag.Class.IS_EXPECT(flags)

	val isExternal
		get() = Flag.Class.IS_EXTERNAL(flags)

	val isInline
		get() = Flag.Class.IS_INLINE(flags)

	val isInner
		get() = Flag.Class.IS_INNER(flags)

	val kind = when {
		Flag.Class.IS_DATA(flags) -> Kind.DATA_CLASS // special case of IS_CLASS, check first
		Flag.Class.IS_ANNOTATION_CLASS(flags) -> Kind.ANNOTATION_CLASS
		Flag.Class.IS_CLASS(flags) -> Kind.CLASS
		Flag.Class.IS_COMPANION_OBJECT(flags) -> Kind.COMPANION_OBJECT
		Flag.Class.IS_ENUM_CLASS(flags) -> Kind.ENUM_CLASS
		Flag.Class.IS_ENUM_ENTRY(flags) -> Kind.ENUM_ENTRY
		Flag.Class.IS_INTERFACE(flags) -> Kind.INTERFACE
		Flag.Class.IS_OBJECT(flags) -> Kind.OBJECT
		else -> throw MetadataException("Class '$name' has an unsupported class kind (flags: $flags)")
	}

	val modality = MModality.forFlags(flags)

	val visibility = MVisibility.forFlags(flags)
		?: throw MetadataException("Class '$name' has an unsupported visibility (flags: $flags)")


	override fun equals(other: Any?): Boolean {
		if (other === this) return true
		if (other !is MClass) return false

		return anonymousObjectOriginName == other.anonymousObjectOriginName &&
			companion == other.companion &&
			constructors == other.constructors &&
			enumEntries == other.enumEntries &&
			flags == other.flags &&
			functions == other.functions &&
			localDelegatedProperties == other.localDelegatedProperties &&
			name == other.name &&
			nestedClasses == other.nestedClasses &&
			properties == other.properties &&
			sealedSubclasses == other.sealedSubclasses &&
			supertype == other.supertype &&
			typeAliases == other.typeAliases &&
			typeParameters == other.typeParameters &&
			versionRequirement == other.versionRequirement
	}


	override fun hashCode() =
		Objects.hash(
			anonymousObjectOriginName,
			companion,
			constructors,
			enumEntries,
			flags,
			functions,
			localDelegatedProperties,
			name,
			nestedClasses,
			properties,
			sealedSubclasses,
			supertype,
			typeAliases,
			typeParameters,
			versionRequirement
		)


	override fun toString() = typeToString(
		"anonymousObjectOriginName" to anonymousObjectOriginName,
		"name" to name,
		"companion" to companion,
		"constructors" to constructors,
		"enumEntries" to enumEntries,
		"hasAnnotations" to hasAnnotations,
		"functions" to functions,
		"isExpect" to isExpect,
		"isExternal" to isExternal,
		"isInline" to isInline,
		"isInner" to isInner,
		"kind" to kind,
		"localDelegatedProperties" to localDelegatedProperties,
		"modality" to modality,
		"nestedClasses" to nestedClasses,
		"properties" to properties,
		"sealedSubclasses" to sealedSubclasses,
		"supertype" to supertype,
		"typeAliases" to typeAliases,
		"typeParameters" to typeParameters,
		"visibility" to visibility,
		"versionRequirement" to versionRequirement
	)


	companion object;


	@Suppress("EnumEntryName")
	enum class Kind {

		ANNOTATION_CLASS,
		COMPANION_OBJECT,
		DATA_CLASS,
		CLASS,
		ENUM_CLASS,
		ENUM_ENTRY,
		INTERFACE,
		OBJECT;


		override fun toString() =
			when (this) {
				ANNOTATION_CLASS -> "annotation class"
				COMPANION_OBJECT -> "companion object"
				DATA_CLASS -> "data class"
				CLASS -> "class"
				ENUM_CLASS -> "enum class"
				ENUM_ENTRY -> "enum entry"
				INTERFACE -> "interface"
				OBJECT -> "object"
			}


		companion object
	}
}


class MFileFacade internal constructor(
	val functions: List<MFunction>,
	val localDelegatedProperties: List<MProperty>,
	val properties: List<MProperty>,
	val typeAliases: List<MTypeAlias>
) : MType() {

	override fun equals(other: Any?): Boolean {
		if (other === this) return true
		if (other !is MFileFacade) return false

		return functions == other.functions &&
			localDelegatedProperties == other.localDelegatedProperties &&
			properties == other.properties &&
			typeAliases == other.typeAliases
	}


	override fun hashCode() =
		Objects.hash(
			functions,
			localDelegatedProperties,
			properties,
			typeAliases
		)


	override fun toString() = typeToString(
		"functions" to functions,
		"localDelegatedProperties" to localDelegatedProperties,
		"properties" to properties,
		"typeAliases" to typeAliases
	)


	companion object
}


class MLambda internal constructor(
	val function: MFunction
) : MType() {

	override fun equals(other: Any?): Boolean {
		if (other === this) return true
		if (other !is MLambda) return false

		return function == other.function
	}


	override fun hashCode() =
		Objects.hash(
			function
		)


	override fun toString() = typeToString(
		"function" to function
	)


	companion object
}


class MMultiFileClassFacade internal constructor(
	val partClassNames: List<MTypeName>
) : MType() {

	override fun equals(other: Any?): Boolean {
		if (other === this) return true
		if (other !is MMultiFileClassFacade) return false

		return partClassNames == other.partClassNames
	}


	override fun hashCode() =
		Objects.hash(
			partClassNames
		)


	override fun toString() = typeToString(
		"partClassNames" to partClassNames
	)


	companion object
}


class MMultiFileClassPart internal constructor(
	val facadeClassName: MTypeName,
	val fileFacade: MFileFacade
) : MType() {

	override fun equals(other: Any?): Boolean {
		if (other === this) return true
		if (other !is MMultiFileClassPart) return false

		return facadeClassName == other.facadeClassName &&
			fileFacade == other.fileFacade
	}


	override fun hashCode() =
		Objects.hash(
			facadeClassName,
			fileFacade
		)


	override fun toString() = typeToString(
		"facadeClassName" to facadeClassName,
		"fileFacade" to fileFacade
	)


	companion object
}


object MUnknown : MType() {

	override fun toString() = typeToString()
}
