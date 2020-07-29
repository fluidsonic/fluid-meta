package io.fluidsonic.meta


public sealed class MType : Meta


public sealed class MNamedType : MType(),
	MPropertyContainer,
	MVersionRestrictable {

	public abstract val name: MQualifiedTypeName
}


@Suppress("EqualsOrHashCode")
public data class MAnnotationClass(
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

	override val localId: MLocalId.Type by lazy { MLocalId.Type(name = name.withoutPackage()) }


	@Deprecated(level = DeprecationLevel.HIDDEN, message = "Annotation classes only have a single constructor")
	override val constructors: List<MConstructor>
		get() = listOf(constructor)


	override fun hashCode(): Int =
		localId.hashCode()


	override fun toString(): String =
		MetaCodeWriter.write(this)


	public companion object
}


@Suppress("EqualsOrHashCode")
public data class MClass(
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

	override val localId: MLocalId.Type by lazy { MLocalId.Type(name = name.withoutPackage()) }


	override fun hashCode(): Int =
		localId.hashCode()


	override fun toString(): String =
		MetaCodeWriter.write(this)


	public companion object;


	public sealed class Specialization {


		public object Data : Specialization() {

			override fun toString(): String = "data"
		}


		public object Inner : Specialization() {

			override fun toString(): String = "inner"
		}


		public data class Sealed(
			val subclassTypes: List<MQualifiedTypeName>
		) : Specialization() {

			override fun toString(): String =
				subclassTypes.joinToString(prefix = "sealed [\n", separator = ",\n", postfix = "\n]")
		}


		public companion object
	}
}


@Suppress("EqualsOrHashCode")
public data class MEnumClass(
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

	override val localId: MLocalId.Type by lazy { MLocalId.Type(name = name.withoutPackage()) }


	override fun hashCode(): Int =
		localId.hashCode()


	override fun toString(): String =
		MetaCodeWriter.write(this)


	public companion object
}


@Suppress("EqualsOrHashCode")
public data class MEnumEntryClass(
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

	override fun hashCode(): Int =
		name.hashCode()


	@Deprecated(level = DeprecationLevel.HIDDEN, message = "Enum entries only have a single supertype")
	override val supertypes: List<MTypeReference>
		get() = listOf(supertype)


	override fun toString(): String =
		MetaCodeWriter.write(this)


	public companion object
}


public data class MFile(
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

	override fun toString(): String =
		MetaCodeWriter.write(this)


	public companion object
}


@Suppress("EqualsOrHashCode")
public data class MInterface(
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

	override val localId: MLocalId.Type by lazy { MLocalId.Type(name = name.withoutPackage()) }


	override fun hashCode(): Int =
		localId.hashCode()


	override fun toString(): String =
		MetaCodeWriter.write(this)


	public companion object
}


public data class MLambda(
	val function: MFunction
) : MType(),
	MFunctionContainer {

	@Deprecated(level = DeprecationLevel.HIDDEN, message = "Lambdas only have a single function")
	override val functions: List<MFunction>
		get() = listOf(function)


	override fun toString(): String =
		MetaCodeWriter.write(this)


	public companion object
}


public data class MMultiFileClass(
	val className: MQualifiedTypeName,
	val partClassNames: List<MQualifiedTypeName>
) : MType() {

	override fun toString(): String =
		MetaCodeWriter.write(this)


	public companion object
}


public data class MMultiFileClassPart(
	val className: MQualifiedTypeName,
	val file: MFile
) : MType() {

	override fun toString(): String =
		MetaCodeWriter.write(this)


	public companion object
}


@Suppress("EqualsOrHashCode")
public data class MObject(
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

	override val localId: MLocalId.Type by lazy { MLocalId.Type(name = name.withoutPackage()) }


	@Deprecated(level = DeprecationLevel.HIDDEN, message = "Objects only have a single constructor")
	override val constructors: List<MConstructor>
		get() = listOf(constructor)


	override fun hashCode(): Int =
		localId.hashCode()


	override fun toString(): String =
		MetaCodeWriter.write(this)


	@Deprecated(level = DeprecationLevel.HIDDEN, message = "Objects only have a single constructor")
	val secondaryConstructors: List<MConstructor>
		get() = listOf(constructor)


	public companion object
}


public object MUnknown : MType() {

	override fun toString(): String =
		MetaCodeWriter.write(this)
}
