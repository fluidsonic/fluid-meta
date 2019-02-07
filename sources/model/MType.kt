package com.github.fluidsonic.fluid.meta


sealed class MType


@Suppress("EqualsOrHashCode")
data class MAnnotationClass(
	override val companion: MQualifiedTypeName?,
	val constructor: MConstructor,
	override val isExpect: Boolean,
	val name: MQualifiedTypeName,
	override val properties: List<MProperty>,
	override val types: List<MQualifiedTypeName>,
	override val versionRequirements: List<MVersionRequirement>,
	override val visibility: MVisibility
) : MType(),
	MCompanionable,
	MConstructable,
	MExpectable,
	MIdentifyable,
	MPropertyContainer,
	MTypeContainer,
	MVersionRestrictable,
	MVisibilityRestrictable {

	override val localId by lazy { MLocalId.Type(name = name.withoutPackage()) }


	@Deprecated(level = DeprecationLevel.HIDDEN, message = "Annotation classes only have a single constructor")
	override val constructors: List<MConstructor>
		get() = listOf(constructor)


	override fun hashCode() =
		localId.hashCode()


	override fun toString() = typeToString(
		"companion" to companion,
		"constructor" to constructor,
		"isExpect" to isExpect,
		"name" to name,
		"properties" to properties,
		"types" to types,
		"versionRequirements" to versionRequirements,
		"visibility" to visibility
	)


	companion object
}


@Suppress("EqualsOrHashCode")
data class MClass(
	override val companion: MQualifiedTypeName?,
	override val constructors: List<MConstructor>,
	override val functions: List<MFunction>,
	override val inheritanceRestriction: MInheritanceRestriction,
	override val isExpect: Boolean,
	override val isExternal: Boolean,
	override val isInline: Boolean,
	val isInner: Boolean,
	override val localDelegatedProperties: List<MProperty>,
	val name: MQualifiedTypeName,
	override val properties: List<MProperty>,
	val specialization: Specialization?,
	override val supertypes: List<MTypeReference>,
	override val typeAliases: List<MTypeAlias>,
	override val typeParameters: List<MTypeParameter>,
	override val types: List<MQualifiedTypeName>,
	override val versionRequirements: List<MVersionRequirement>,
	override val visibility: MVisibility
) : MType(),
	MCompanionable,
	MConstructable,
	MExpectable,
	MExternalizable,
	MFunctionContainer,
	MGeneralizable,
	MIdentifyable,
	MInheritanceRestrictable,
	MInlineable,
	MLocalDelegatedPropertyContainer,
	MPropertyContainer,
	MSupertypable,
	MTypeAliasContainer,
	MTypeContainer,
	MVersionRestrictable,
	MVisibilityRestrictable {

	override val localId by lazy { MLocalId.Type(name = name.withoutPackage()) }


	override fun hashCode() =
		localId.hashCode()


	override fun toString() = typeToString(
		"companion" to companion,
		"constructors" to constructors,
		"functions" to functions,
		"inheritanceRestriction" to inheritanceRestriction,
		"isExpect" to isExpect,
		"isExternal" to isExternal,
		"isInline" to isInline,
		"isInner" to isInner,
		"localDelegatedProperties" to localDelegatedProperties,
		"name" to name,
		"properties" to properties,
		"specialization" to specialization,
		"supertypes" to supertypes,
		"typeAliases" to typeAliases,
		"typeParameters" to typeParameters,
		"types" to types,
		"versionRequirements" to versionRequirements,
		"visibility" to visibility
	)


	companion object;


	sealed class Specialization {


		object Data : Specialization() {

			override fun toString() = "data"
		}


		data class Sealed(
			val subclassTypes: List<MQualifiedTypeName>
		) : Specialization() {

			override fun toString() = typeToString(
				"subclassTypes" to subclassTypes
			)
		}


		companion object
	}
}


@Suppress("EqualsOrHashCode")
data class MEnumClass(
	override val companion: MQualifiedTypeName?,
	override val constructors: List<MConstructor>,
	val entryNames: List<MEnumEntryName>,
	override val functions: List<MFunction>,
	override val isExpect: Boolean,
	override val isExternal: Boolean,
	override val localDelegatedProperties: List<MProperty>,
	val name: MQualifiedTypeName,
	override val properties: List<MProperty>,
	override val supertypes: List<MTypeReference>,
	override val typeAliases: List<MTypeAlias>,
	override val types: List<MQualifiedTypeName>,
	override val versionRequirements: List<MVersionRequirement>,
	override val visibility: MVisibility
) : MType(),
	MCompanionable,
	MConstructable,
	MExpectable,
	MExternalizable,
	MFunctionContainer,
	MIdentifyable,
	MLocalDelegatedPropertyContainer,
	MPropertyContainer,
	MSupertypable,
	MTypeAliasContainer,
	MTypeContainer,
	MVersionRestrictable,
	MVisibilityRestrictable {

	override val localId by lazy { MLocalId.Type(name = name.withoutPackage()) }


	override fun hashCode() =
		localId.hashCode()


	override fun toString() = typeToString(
		"companion" to companion,
		"constructors" to constructors,
		"enumEntryNames" to entryNames,
		"functions" to functions,
		"isExpect" to isExpect,
		"isExternal" to isExternal,
		"localDelegatedProperties" to localDelegatedProperties,
		"name" to name,
		"nestedClasses" to types,
		"properties" to properties,
		"supertypes" to supertypes,
		"typeAliases" to typeAliases,
		"versionRequirements" to versionRequirements,
		"visibility" to visibility
	)


	companion object
}


@Suppress("EqualsOrHashCode")
data class MEnumEntryClass(
	override val functions: List<MFunction>,
	val name: MQualifiedTypeName,
	override val properties: List<MProperty>,
	val supertype: MTypeReference,
	override val versionRequirements: List<MVersionRequirement>
) : MType(),
	MFunctionContainer,
	MPropertyContainer,
	MSupertypable,
	MVersionRestrictable {

	override fun hashCode() =
		name.hashCode()


	@Deprecated(level = DeprecationLevel.HIDDEN, message = "Enum entries only have a single supertype")
	override val supertypes: List<MTypeReference>
		get() = listOf(supertype)


	override fun toString() = typeToString(
		"functions" to functions,
		"name" to name,
		"properties" to properties,
		"supertype" to supertype,
		"versionRequirements" to versionRequirements
	)


	companion object
}


data class MFileFacade(
	override val functions: List<MFunction>,
	override val localDelegatedProperties: List<MProperty>,
	override val properties: List<MProperty>,
	override val typeAliases: List<MTypeAlias>
) : MType(),
	MFunctionContainer,
	MLocalDelegatedPropertyContainer,
	MPropertyContainer,
	MTypeAliasContainer {

	override fun toString() = typeToString(
		"functions" to functions,
		"localDelegatedProperties" to localDelegatedProperties,
		"properties" to properties,
		"typeAliases" to typeAliases
	)


	companion object
}


@Suppress("EqualsOrHashCode")
data class MInterface(
	override val companion: MQualifiedTypeName?,
	override val functions: List<MFunction>,
	override val isExpect: Boolean,
	override val isExternal: Boolean,
	override val localDelegatedProperties: List<MProperty>,
	val name: MQualifiedTypeName,
	override val properties: List<MProperty>,
	override val supertypes: List<MTypeReference>,
	override val typeAliases: List<MTypeAlias>,
	override val typeParameters: List<MTypeParameter>,
	override val types: List<MQualifiedTypeName>,
	override val versionRequirements: List<MVersionRequirement>,
	override val visibility: MVisibility
) : MType(),
	MCompanionable,
	MExpectable,
	MExternalizable,
	MFunctionContainer,
	MGeneralizable,
	MIdentifyable,
	MLocalDelegatedPropertyContainer,
	MPropertyContainer,
	MSupertypable,
	MTypeAliasContainer,
	MTypeContainer,
	MVersionRestrictable,
	MVisibilityRestrictable {

	override val localId by lazy { MLocalId.Type(name = name.withoutPackage()) }


	override fun hashCode() =
		localId.hashCode()


	override fun toString() = typeToString(
		"companion" to companion,
		"functions" to functions,
		"isExpect" to isExpect,
		"isExternal" to isExternal,
		"localDelegatedProperties" to localDelegatedProperties,
		"name" to name,
		"properties" to properties,
		"supertypes" to supertypes,
		"typeAliases" to typeAliases,
		"typeParameters" to typeParameters,
		"types" to types,
		"versionRequirements" to versionRequirements,
		"visibility" to visibility
	)


	companion object
}


data class MLambda(
	val function: MFunction
) : MType(),
	MFunctionContainer {

	@Deprecated(level = DeprecationLevel.HIDDEN, message = "Lambdas only have a single function")
	override val functions: List<MFunction>
		get() = listOf(function)


	override fun toString() = typeToString(
		"function" to function
	)


	companion object
}


data class MMultiFileClassFacade(
	val partClassNames: List<MQualifiedTypeName>
) : MType() {

	override fun toString() = typeToString(
		"partClassNames" to partClassNames
	)


	companion object
}


data class MMultiFileClassPart(
	val facadeClassName: MQualifiedTypeName,
	val fileFacade: MFileFacade
) : MType() {

	override fun toString() = typeToString(
		"facadeClassName" to facadeClassName,
		"fileFacade" to fileFacade
	)


	companion object
}


@Suppress("EqualsOrHashCode")
data class MObject(
	val constructor: MConstructor,
	override val functions: List<MFunction>,
	val isCompanion: Boolean,
	override val isExpect: Boolean,
	override val isExternal: Boolean,
	override val localDelegatedProperties: List<MProperty>,
	val name: MQualifiedTypeName,
	val originName: MQualifiedTypeName?,
	override val properties: List<MProperty>,
	override val supertypes: List<MTypeReference>,
	override val typeAliases: List<MTypeAlias>,
	override val types: List<MQualifiedTypeName>,
	override val versionRequirements: List<MVersionRequirement>,
	override val visibility: MVisibility
) : MType(),
	MConstructable,
	MExpectable,
	MExternalizable,
	MFunctionContainer,
	MIdentifyable,
	MLocalDelegatedPropertyContainer,
	MPropertyContainer,
	MSupertypable,
	MTypeAliasContainer,
	MTypeContainer,
	MVersionRestrictable,
	MVisibilityRestrictable {

	override val localId by lazy { MLocalId.Type(name = name.withoutPackage()) }


	@Deprecated(level = DeprecationLevel.HIDDEN, message = "Objects only have a single constructor")
	override val constructors: List<MConstructor>
		get() = listOf(constructor)


	override fun hashCode() =
		localId.hashCode()


	override fun toString() = typeToString(
		"constructor" to constructor,
		"functions" to functions,
		"isCompanion" to isCompanion,
		"isExpect" to isExpect,
		"isExternal" to isExternal,
		"localDelegatedProperties" to localDelegatedProperties,
		"name" to name,
		"originName" to originName,
		"properties" to properties,
		"supertypes" to supertypes,
		"typeAliases" to typeAliases,
		"types" to types,
		"versionRequirements" to versionRequirements,
		"visibility" to visibility
	)


	@Deprecated(level = DeprecationLevel.HIDDEN, message = "Objects only have a single constructor")
	val secondaryConstructors: List<MConstructor>
		get() = listOf(constructor)


	companion object
}


object MUnknown : MType() {

	override fun toString() = typeToString()
}
