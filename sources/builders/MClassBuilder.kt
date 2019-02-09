package com.github.fluidsonic.fluid.meta

import com.github.fluidsonic.fluid.stdlib.*
import kotlinx.metadata.ClassName
import kotlinx.metadata.Flag
import kotlinx.metadata.Flags
import kotlinx.metadata.KmClassVisitor
import kotlinx.metadata.KmExtensionType
import kotlinx.metadata.KmVariance
import kotlinx.metadata.jvm.JvmClassExtensionVisitor


internal class MClassBuilder : KmClassVisitor() {

	private var anonymousObjectOriginName: MQualifiedTypeName? = null
	private var companionName: MTypeName? = null
	private var constructors: MutableList<MConstructorBuilder>? = null
	private var enumEntryNames: MutableList<MEnumEntryName>? = null
	private var flags: Flags = 0
	private var functions: MutableList<MFunctionBuilder>? = null
	private var localDelegatedProperties: MutableList<MPropertyBuilder>? = null
	private var name: MQualifiedTypeName? = null
	private var properties: MutableList<MPropertyBuilder>? = null
	private var sealedSubclasses: MutableList<MQualifiedTypeName>? = null
	private var supertypes: MutableList<MTypeReferenceBuilder>? = null
	private var typeAliases: MutableList<MTypeAliasBuilder>? = null
	private var typeParameters: MutableList<MTypeParameterBuilder>? = null
	private var types: MutableList<MTypeName>? = null
	private var versionRequirements: MutableList<MVersionRequirementBuilder>? = null


	fun build(): MType =
		when {
			// order of flag checks is important
			Flag.Class.IS_ANNOTATION_CLASS(flags) -> buildAnnotationClass()
			Flag.Class.IS_CLASS(flags) -> buildClass()
			Flag.Class.IS_COMPANION_OBJECT(flags) -> buildObject(isCompanion = true)
			Flag.Class.IS_ENUM_CLASS(flags) -> buildEnumClass()
			Flag.Class.IS_ENUM_ENTRY(flags) -> buildEnumEntryClass()
			Flag.Class.IS_INTERFACE(flags) -> buildInterface()
			Flag.Class.IS_OBJECT(flags) -> buildObject(isCompanion = false)
			else -> throw MetaException("unknown clas kind in flags ${flags.toString(16)}")
		}


	private fun buildAnnotationClass() =
		MAnnotationClass(
			companionName = companionName,
			constructor = constructors?.singleOrNull()?.build()
				?: throw MetaException("an annotation class must have exactly one constructor"),
			isExpect = Flag.Class.IS_EXPECT(flags),
			name = name ?: throw MetaException("annotation class has no name"),
			properties = properties.mapOrEmpty { it.build() },
			types = types.toListOrEmpty(),
			versionRequirements = versionRequirements.mapOrEmpty { it.build() },
			visibility = MVisibility.forFlags(flags)
		)


	private fun buildClass() =
		MClass(
			anonymousObjectOriginName = anonymousObjectOriginName,
			companionName = companionName,
			constructors = constructors.mapOrEmpty { it.build() },
			functions = functions.mapOrEmpty { it.build() },
			inheritanceRestriction = MInheritanceRestriction.forFlags(flags),
			isExpect = Flag.Class.IS_EXPECT(flags),
			isExternal = Flag.Class.IS_EXTERNAL(flags),
			isInline = Flag.Class.IS_INLINE(flags),
			name = name ?: throw MetaException("class has no name"),
			localDelegatedProperties = localDelegatedProperties.mapOrEmpty { it.build() },
			properties = properties.mapOrEmpty { it.build() },
			specialization = when {
				Flag.Class.IS_DATA(flags) -> MClass.Specialization.Data
				Flag.Class.IS_INNER(flags) -> MClass.Specialization.Inner
				Flag.IS_SEALED(flags) -> MClass.Specialization.Sealed(
					subclassTypes = sealedSubclasses.toListOrEmpty()
				)
				else -> null
			},
			supertypes = supertypes.mapOrEmpty { it.build() },
			typeAliases = typeAliases.mapOrEmpty { it.build() },
			typeParameters = typeParameters.mapOrEmpty { it.build() },
			types = types.toListOrEmpty(),
			versionRequirements = versionRequirements.mapOrEmpty { it.build() },
			visibility = MVisibility.forFlags(flags)
		)


	private fun buildEnumClass() =
		MEnumClass(
			companionName = companionName,
			constructors = constructors.mapOrEmpty { it.build() },
			entryNames = enumEntryNames.toListOrEmpty(),
			functions = functions.mapOrEmpty { it.build() },
			isExpect = Flag.Class.IS_EXPECT(flags),
			isExternal = Flag.Class.IS_EXTERNAL(flags),
			name = name ?: throw MetaException("class has no name"),
			localDelegatedProperties = localDelegatedProperties.mapOrEmpty { it.build() },
			properties = properties.mapOrEmpty { it.build() },
			supertypes = supertypes.mapOrEmpty { it.build() },
			typeAliases = typeAliases.mapOrEmpty { it.build() },
			types = types.toListOrEmpty(),
			versionRequirements = versionRequirements.mapOrEmpty { it.build() },
			visibility = MVisibility.forFlags(flags)
		)


	private fun buildEnumEntryClass() =
		MEnumEntryClass(
			functions = functions.mapOrEmpty { it.build() },
			name = name ?: throw MetaException("class has no name"),
			properties = properties.mapOrEmpty { it.build() },
			supertype = supertypes?.singleOrNull()?.build()
				?: throw MetaException("an enum entry class must have exactly one supertype"),
			versionRequirements = versionRequirements.mapOrEmpty { it.build() }
		)


	private fun buildInterface() =
		MInterface(
			companionName = companionName,
			functions = functions.mapOrEmpty { it.build() },
			isExpect = Flag.Class.IS_EXPECT(flags),
			isExternal = Flag.Class.IS_EXTERNAL(flags),
			name = name ?: throw MetaException("class has no name"),
			localDelegatedProperties = localDelegatedProperties.mapOrEmpty { it.build() },
			properties = properties.mapOrEmpty { it.build() },
			supertypes = supertypes.mapOrEmpty { it.build() },
			typeAliases = typeAliases.mapOrEmpty { it.build() },
			typeParameters = typeParameters.mapOrEmpty { it.build() },
			types = types.toListOrEmpty(),
			versionRequirements = versionRequirements.mapOrEmpty { it.build() },
			visibility = MVisibility.forFlags(flags)
		)


	private fun buildObject(isCompanion: Boolean) =
		MObject(
			constructor = constructors?.singleOrNull()?.build()
				?: throw MetaException("an object must have exactly one constructor"),
			functions = functions.mapOrEmpty { it.build() },
			isCompanion = isCompanion,
			isExpect = Flag.Class.IS_EXPECT(flags),
			isExternal = Flag.Class.IS_EXTERNAL(flags),
			name = name ?: throw MetaException("object has no name"),
			localDelegatedProperties = localDelegatedProperties.mapOrEmpty { it.build() },
			properties = properties.mapOrEmpty { it.build() },
			supertypes = supertypes.mapOrEmpty { it.build() },
			typeAliases = typeAliases.mapOrEmpty { it.build() },
			types = types.toListOrEmpty(),
			versionRequirements = versionRequirements.mapOrEmpty { it.build() },
			visibility = MVisibility.forFlags(flags)
		)


	override fun visit(flags: Flags, name: ClassName) {
		this.flags = flags
		this.name = MQualifiedTypeName.fromKotlinInternal(name)
	}


	override fun visitCompanionObject(name: ClassName) {
		companionName = MTypeName.fromKotlinInternal(name)
	}


	override fun visitConstructor(flags: Flags) =
		MConstructorBuilder(flags = flags)
			.also {
				constructors?.apply { add(it) } ?: { constructors = mutableListOf(it) }()
			}


	override fun visitEnumEntry(name: String) {
		enumEntryNames?.apply { add(MEnumEntryName(name)) }
			?: { enumEntryNames = mutableListOf(MEnumEntryName(name)) }()
	}


	override fun visitExtensions(type: KmExtensionType) =
		(type == JvmClassExtensionVisitor.TYPE).thenTake {
			object : JvmClassExtensionVisitor() {

				override fun visitAnonymousObjectOriginName(internalName: ClassName) {
					anonymousObjectOriginName = MQualifiedTypeName.fromKotlinInternal(internalName)
				}


				override fun visitLocalDelegatedProperty(flags: Flags, name: String, getterFlags: Flags, setterFlags: Flags) =
					MPropertyBuilder(flags = flags, getterFlags = getterFlags, name = MVariableName(name), setterFlags = setterFlags)
						.also {
							localDelegatedProperties?.apply { add(it) } ?: { localDelegatedProperties = mutableListOf(it) }()
						}
			}
		}


	override fun visitFunction(flags: Flags, name: String) =
		MFunctionBuilder(flags = flags, name = MFunctionName(name))
			.also {
				functions?.apply { add(it) } ?: { functions = mutableListOf(it) }()
			}


	override fun visitNestedClass(name: ClassName) {
		types?.apply { add(MTypeName.fromKotlinInternal(name)) }
			?: { types = mutableListOf(MTypeName.fromKotlinInternal(name)) }()
	}


	override fun visitProperty(flags: Flags, name: String, getterFlags: Flags, setterFlags: Flags) =
		MPropertyBuilder(flags = flags, getterFlags = getterFlags, name = MVariableName(name), setterFlags = setterFlags)
			.also {
				properties?.apply { add(it) } ?: { properties = mutableListOf(it) }()
			}


	override fun visitSealedSubclass(name: ClassName) {
		sealedSubclasses?.apply { add(MQualifiedTypeName.fromKotlinInternal(name)) }
			?: { sealedSubclasses = mutableListOf(MQualifiedTypeName.fromKotlinInternal(name)) }()
	}


	override fun visitSupertype(flags: Flags) =
		MTypeReferenceBuilder(flags = flags)
			.also {
				supertypes?.apply { add(it) } ?: { supertypes = mutableListOf(it) }()
			}


	override fun visitTypeAlias(flags: Flags, name: String) =
		MTypeAliasBuilder(flags = flags, name = MQualifiedTypeName.fromKotlinInternal(name))
			.also {
				typeAliases?.apply { add(it) } ?: { typeAliases = mutableListOf(it) }()
			}


	override fun visitTypeParameter(flags: Flags, name: String, id: Int, variance: KmVariance) =
		MTypeParameterBuilder(flags = flags, id = MTypeParameterId(id), name = MTypeParameterName(name), variance = MVariance(variance))
			.also {
				typeParameters?.apply { add(it) } ?: { typeParameters = mutableListOf(it) }()
			}


	override fun visitVersionRequirement() =
		MVersionRequirementBuilder()
			.also {
				versionRequirements?.apply { add(it) } ?: { versionRequirements = mutableListOf(it) }()
			}
}
