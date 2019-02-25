package com.github.fluidsonic.fluid.meta


sealed class MType : Meta


sealed class MNamedType : MType(),
	MPropertyContainer,
	MVersionRestrictable {

	abstract val name: MQualifiedTypeName
}


@Suppress("EqualsOrHashCode")
data class MAnnotationClass(
	override val companionName: MTypeName?,
	val constructor: MConstructor,
	override val isExpect: Boolean,
	override val name: MQualifiedTypeName,
	override val properties: List<MProperty>,
	override val types: List<MTypeName>,
	override val versionRequirements: List<MVersionRequirement>,
	override val visibility: MVisibility
) : MNamedType(),
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


	override fun toString() =
		MetaCodeWriter.write(this)


	companion object
}


@Suppress("EqualsOrHashCode")
data class MClass(
	val anonymousObjectOriginName: MQualifiedTypeName?,
	override val companionName: MTypeName?,
	override val constructors: List<MConstructor>,
	override val functions: List<MFunction>,
	override val inheritanceRestriction: MInheritanceRestriction,
	override val isExpect: Boolean,
	override val isExternal: Boolean,
	override val isInline: Boolean,
	override val localDelegatedProperties: List<MProperty>,
	override val name: MQualifiedTypeName,
	override val properties: List<MProperty>,
	val specialization: Specialization?,
	override val supertypes: List<MTypeReference>,
	override val typeAliases: List<MTypeAlias>,
	override val typeParameters: List<MTypeParameter>,
	override val types: List<MTypeName>,
	override val versionRequirements: List<MVersionRequirement>,
	override val visibility: MVisibility
) : MNamedType(),
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


	override fun toString() =
		MetaCodeWriter.write(this)


	companion object;


	sealed class Specialization {


		object Data : Specialization() {

			override fun toString() = "data"
		}


		object Inner : Specialization() {

			override fun toString() = "inner"
		}


		data class Sealed(
			val subclassTypes: List<MQualifiedTypeName>
		) : Specialization() {

			override fun toString() =
				subclassTypes.joinToString(prefix = "sealed [\n", separator = ",\n", postfix = "\n]")
		}


		companion object
	}
}


@Suppress("EqualsOrHashCode")
data class MEnumClass(
	override val companionName: MTypeName?,
	override val constructors: List<MConstructor>,
	val entryNames: List<MEnumEntryName>,
	override val functions: List<MFunction>,
	override val isExpect: Boolean,
	override val isExternal: Boolean,
	override val localDelegatedProperties: List<MProperty>,
	override val name: MQualifiedTypeName,
	override val properties: List<MProperty>,
	override val supertypes: List<MTypeReference>,
	override val typeAliases: List<MTypeAlias>,
	override val types: List<MTypeName>,
	override val versionRequirements: List<MVersionRequirement>,
	override val visibility: MVisibility
) : MNamedType(),
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


	override fun toString() =
		MetaCodeWriter.write(this)


	companion object
}


@Suppress("EqualsOrHashCode")
data class MEnumEntryClass(
	override val functions: List<MFunction>,
	override val name: MQualifiedTypeName,
	override val properties: List<MProperty>,
	val supertype: MTypeReference,
	override val versionRequirements: List<MVersionRequirement>
) : MNamedType(),
	MFunctionContainer,
	MPropertyContainer,
	MSupertypable,
	MVersionRestrictable {

	override fun hashCode() =
		name.hashCode()


	@Deprecated(level = DeprecationLevel.HIDDEN, message = "Enum entries only have a single supertype")
	override val supertypes: List<MTypeReference>
		get() = listOf(supertype)


	override fun toString() =
		MetaCodeWriter.write(this)


	companion object
}


data class MFile(
	val facadeClassName: MQualifiedTypeName,
	override val functions: List<MFunction>,
	val jvmPackageName: MPackageName?,
	override val localDelegatedProperties: List<MProperty>,
	override val properties: List<MProperty>,
	override val typeAliases: List<MTypeAlias>
) : MType(),
	MFunctionContainer,
	MLocalDelegatedPropertyContainer,
	MPropertyContainer,
	MTypeAliasContainer {

	override fun toString() =
		MetaCodeWriter.write(this)


	companion object
}


@Suppress("EqualsOrHashCode")
data class MInterface(
	override val companionName: MTypeName?,
	override val functions: List<MFunction>,
	override val isExpect: Boolean,
	override val isExternal: Boolean,
	override val localDelegatedProperties: List<MProperty>,
	override val name: MQualifiedTypeName,
	override val properties: List<MProperty>,
	override val supertypes: List<MTypeReference>,
	override val typeAliases: List<MTypeAlias>,
	override val typeParameters: List<MTypeParameter>,
	override val types: List<MTypeName>,
	override val versionRequirements: List<MVersionRequirement>,
	override val visibility: MVisibility
) : MNamedType(),
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


	override fun toString() =
		MetaCodeWriter.write(this)


	companion object
}


data class MLambda(
	val function: MFunction
) : MType(),
	MFunctionContainer {

	@Deprecated(level = DeprecationLevel.HIDDEN, message = "Lambdas only have a single function")
	override val functions: List<MFunction>
		get() = listOf(function)


	override fun toString() =
		MetaCodeWriter.write(this)


	companion object
}


data class MMultiFileClass(
	val className: MQualifiedTypeName,
	val partClassNames: List<MQualifiedTypeName>
) : MType() {

	override fun toString() =
		MetaCodeWriter.write(this)


	companion object
}


data class MMultiFileClassPart(
	val className: MQualifiedTypeName,
	val file: MFile
) : MType() {

	override fun toString() =
		MetaCodeWriter.write(this)


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
	override val name: MQualifiedTypeName,
	override val properties: List<MProperty>,
	override val supertypes: List<MTypeReference>,
	override val typeAliases: List<MTypeAlias>,
	override val types: List<MTypeName>,
	override val versionRequirements: List<MVersionRequirement>,
	override val visibility: MVisibility
) : MNamedType(),
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


	override fun toString() =
		MetaCodeWriter.write(this)


	@Deprecated(level = DeprecationLevel.HIDDEN, message = "Objects only have a single constructor")
	val secondaryConstructors: List<MConstructor>
		get() = listOf(constructor)


	companion object
}


object MUnknown : MType() {

	override fun toString() =
		MetaCodeWriter.write(this)
}
