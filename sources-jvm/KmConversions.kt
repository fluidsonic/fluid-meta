package io.fluidsonic.meta

import kotlin.metadata.*
import kotlin.metadata.jvm.*


// -- KmClass ----------------------------------------------------------------------------------------------------------

internal fun KmClass.toMeta(): MType =
	when (kind) {
		ClassKind.ANNOTATION_CLASS -> toAnnotationClassMeta()
		ClassKind.CLASS -> toClassMeta()
		ClassKind.COMPANION_OBJECT -> toObjectMeta(isCompanion = true)
		ClassKind.ENUM_CLASS -> toEnumClassMeta()
		ClassKind.ENUM_ENTRY -> toEnumEntryClassMeta()
		ClassKind.INTERFACE -> toInterfaceMeta()
		ClassKind.OBJECT -> toObjectMeta(isCompanion = false)
	}


private fun KmClass.toAnnotationClassMeta() =
	MAnnotationClass(
		companionName = companionObject?.let { MTypeName.fromKotlinInternal(it) },
		constructor = constructors.singleOrNull()?.toMeta()
			?: throw MetaException("an annotation class must have exactly one constructor"),
		isExpect = isExpect,
		name = MQualifiedTypeName.fromKotlinInternal(name),
		properties = properties.map { it.toMeta() },
		types = nestedClasses.map { MTypeName.fromKotlinInternal(it) },
		versionRequirements = versionRequirements.map { it.toMeta() },
		visibility = MVisibility.from(visibility),
	)


private fun KmClass.toClassMeta() =
	MClass(
		anonymousObjectOriginName = anonymousObjectOriginName?.let { MQualifiedTypeName.fromKotlinInternal(it) },
		companionName = companionObject?.let { MTypeName.fromKotlinInternal(it) },
		constructors = constructors.map { it.toMeta() },
		functions = functions.map { it.toMeta() },
		inheritanceRestriction = MInheritanceRestriction.from(modality),
		isExpect = isExpect,
		isExternal = isExternal,
		isValue = isValue,
		localDelegatedProperties = localDelegatedProperties.map { it.toMeta() },
		name = MQualifiedTypeName.fromKotlinInternal(name),
		properties = properties.map { it.toMeta() },
		specialization = when {
			isData -> MClass.Specialization.Data
			isInner -> MClass.Specialization.Inner
			modality == Modality.SEALED -> MClass.Specialization.Sealed(
				subclassTypes = sealedSubclasses.map { MQualifiedTypeName.fromKotlinInternal(it) },
			)

			else -> null
		},
		supertypes = supertypes.map { it.toMeta() },
		typeAliases = typeAliases.map { it.toMeta() },
		typeParameters = typeParameters.map { it.toMeta() },
		types = nestedClasses.map { MTypeName.fromKotlinInternal(it) },
		versionRequirements = versionRequirements.map { it.toMeta() },
		visibility = MVisibility.from(visibility),
	)


private fun KmClass.toEnumClassMeta() =
	MEnumClass(
		companionName = companionObject?.let { MTypeName.fromKotlinInternal(it) },
		constructors = constructors.map { it.toMeta() },
		entryNames = kmEnumEntries.map { MEnumEntryName(it.name) },
		functions = functions.map { it.toMeta() },
		isExpect = isExpect,
		isExternal = isExternal,
		localDelegatedProperties = localDelegatedProperties.map { it.toMeta() },
		name = MQualifiedTypeName.fromKotlinInternal(name),
		properties = properties.map { it.toMeta() },
		supertypes = supertypes.map { it.toMeta() },
		typeAliases = typeAliases.map { it.toMeta() },
		types = nestedClasses.map { MTypeName.fromKotlinInternal(it) },
		versionRequirements = versionRequirements.map { it.toMeta() },
		visibility = MVisibility.from(visibility),
	)


private fun KmClass.toEnumEntryClassMeta() =
	MEnumEntryClass(
		functions = functions.map { it.toMeta() },
		name = MQualifiedTypeName.fromKotlinInternal(name),
		properties = properties.map { it.toMeta() },
		supertype = supertypes.singleOrNull()?.toMeta()
			?: throw MetaException("an enum entry class must have exactly one supertype"),
		versionRequirements = versionRequirements.map { it.toMeta() },
	)


private fun KmClass.toInterfaceMeta() =
	MInterface(
		companionName = companionObject?.let { MTypeName.fromKotlinInternal(it) },
		functions = functions.map { it.toMeta() },
		isExpect = isExpect,
		isExternal = isExternal,
		isFunctional = isFunInterface,
		localDelegatedProperties = localDelegatedProperties.map { it.toMeta() },
		name = MQualifiedTypeName.fromKotlinInternal(name),
		properties = properties.map { it.toMeta() },
		supertypes = supertypes.map { it.toMeta() },
		typeAliases = typeAliases.map { it.toMeta() },
		typeParameters = typeParameters.map { it.toMeta() },
		types = nestedClasses.map { MTypeName.fromKotlinInternal(it) },
		versionRequirements = versionRequirements.map { it.toMeta() },
		visibility = MVisibility.from(visibility),
	)


private fun KmClass.toObjectMeta(isCompanion: Boolean) =
	MObject(
		constructor = constructors.singleOrNull()?.toMeta()
			?: throw MetaException("an object must have exactly one constructor"),
		functions = functions.map { it.toMeta() },
		isCompanion = isCompanion,
		isExpect = isExpect,
		isExternal = isExternal,
		localDelegatedProperties = localDelegatedProperties.map { it.toMeta() },
		name = MQualifiedTypeName.fromKotlinInternal(name),
		properties = properties.map { it.toMeta() },
		supertypes = supertypes.map { it.toMeta() },
		typeAliases = typeAliases.map { it.toMeta() },
		types = nestedClasses.map { MTypeName.fromKotlinInternal(it) },
		versionRequirements = versionRequirements.map { it.toMeta() },
		visibility = MVisibility.from(visibility),
	)


// -- KmFunction -------------------------------------------------------------------------------------------------------

internal fun KmFunction.toMeta(): MFunction = MFunction(
	contract = contract?.toMeta(),
	inheritanceRestriction = MInheritanceRestriction.from(modality),
	isExpect = isExpect,
	isExternal = isExternal,
	isInfix = isInfix,
	isInline = isInline,
	isOperator = isOperator,
	isSuspend = isSuspend,
	isTailrec = isTailrec,
	jvmSignature = signature?.let(::MJvmMemberSignature),
	lambdaClassOriginName = lambdaClassOriginName?.let { MQualifiedTypeName.fromKotlinInternal(it) },
	name = MFunctionName(name),
	receiverParameterType = receiverParameterType?.toMeta(),
	returnType = returnType.toMeta(),
	source = MClassMemberSource.from(kind),
	typeParameters = typeParameters.map { it.toMeta() },
	valueParameters = valueParameters.map { it.toMeta() },
	versionRequirements = versionRequirements.map { it.toMeta() },
	visibility = MVisibility.from(visibility),
)


// -- KmProperty -------------------------------------------------------------------------------------------------------

internal fun KmProperty.toMeta(): MProperty {
	val returnTypeRef = returnType.toMeta()
	val getterAttrs = getter
	val setterAttrs = setter

	val getterAccessor = MPropertyAccessor.Getter(
		isDefault = !getterAttrs.isNotDefault,
		isExternal = getterAttrs.isExternal,
		isInline = getterAttrs.isInline,
		jvmSignature = getterSignature?.let(::MJvmMemberSignature),
		returnType = returnTypeRef,
		visibility = MVisibility.from(getterAttrs.visibility),
	)

	val setterAccessor = if (setterAttrs != null) MPropertyAccessor.Setter(
		isDefault = !setterAttrs.isNotDefault,
		isExternal = setterAttrs.isExternal,
		isInline = setterAttrs.isInline,
		jvmSignature = setterSignature?.let(::MJvmMemberSignature),
		parameter = setterParameter?.toMeta() ?: MValueParameter(
			declaresDefaultValue = false,
			isCrossinline = false,
			isNoinline = returnTypeRef is MTypeReference.Function,
			name = MVariableName("<set-?>"),
			type = returnTypeRef,
			varargElementType = null,
		),
		visibility = MVisibility.from(setterAttrs.visibility),
	) else null

	return MProperty(
		getter = getterAccessor,
		inheritanceRestriction = MInheritanceRestriction.from(modality),
		isConst = isConst,
		isDelegated = isDelegated,
		isExpect = isExpect,
		isExternal = isExternal,
		isInline = getterAccessor.isInline && (setterAccessor == null || setterAccessor.isInline),
		isLateinit = isLateinit,
		isVar = isVar,
		jvmFieldSignature = fieldSignature?.let(::MJvmMemberSignature),
		jvmFlags = 0,
		jvmSyntheticMethodForAnnotationsSignature = syntheticMethodForAnnotations?.let(::MJvmMemberSignature),
		name = MVariableName(name),
		receiverParameterType = receiverParameterType?.toMeta(),
		setter = setterAccessor,
		source = MClassMemberSource.from(kind),
		typeParameters = typeParameters.map { it.toMeta() },
		versionRequirements = versionRequirements.map { it.toMeta() },
		visibility = MVisibility.from(visibility),
	)
}


// -- KmConstructor ----------------------------------------------------------------------------------------------------

internal fun KmConstructor.toMeta(): MConstructor = MConstructor(
	isSecondary = isSecondary,
	jvmSignature = signature?.let(::MJvmMemberSignature),
	valueParameters = valueParameters.map { it.toMeta() },
	versionRequirements = versionRequirements.map { it.toMeta() },
	visibility = MVisibility.from(visibility),
)


// -- KmValueParameter -------------------------------------------------------------------------------------------------

internal fun KmValueParameter.toMeta(): MValueParameter = MValueParameter(
	declaresDefaultValue = declaresDefaultValue,
	isCrossinline = isCrossinline,
	isNoinline = isNoinline,
	name = MVariableName(name),
	type = type.toMeta(),
	varargElementType = varargElementType?.toMeta(),
)


// -- KmTypeParameter --------------------------------------------------------------------------------------------------

internal fun KmTypeParameter.toMeta(): MTypeParameter = MTypeParameter(
	annotations = annotations.map(::MAnnotation),
	id = MTypeParameterId(id),
	isReified = isReified,
	name = MTypeParameterName(name),
	upperBounds = upperBounds.map { it.toMeta() },
	variance = MVariance(variance),
)


// -- KmType -----------------------------------------------------------------------------------------------------------

private val kotlinFunctionTypeNameRegex = Regex("^kotlin/Function\\d+$")
private val kotlinSuspendFunctionTypeNameRegex = Regex("^kotlin/coroutines/SuspendFunction\\d+$")
private const val extensionFunctionTypeAnnotation = "kotlin/ExtensionFunctionType"


internal fun KmType.toMeta(): MTypeReference {
	val classifier = classifier
	val arguments = arguments.map { it.toMeta() }
	val annotations = annotations.map(::MAnnotation)
	val flexBound = flexibleTypeUpperBound?.toMeta()
	val isNullable = isNullable

	return when (classifier) {
		is KmClassifier.Class -> {
			val classifierName = classifier.name

			when {
				kotlinFunctionTypeNameRegex.matches(classifierName) ->
					buildFunctionTypeReference(
						isSuspend = false,
						arguments = arguments,
						annotations = annotations,
						flexBound = flexBound,
						isNullable = isNullable,
					)

				kotlinSuspendFunctionTypeNameRegex.matches(classifierName) ->
					buildFunctionTypeReference(
						isSuspend = true,
						arguments = arguments,
						annotations = annotations,
						flexBound = flexBound,
						isNullable = isNullable,
					)

				else -> MTypeReference.Class(
					abbreviatedType = abbreviatedType?.toMeta(),
					annotations = annotations,
					arguments = arguments,
					flexibleTypeUpperBound = flexBound,
					isNullable = isNullable,
					isRaw = isRaw,
					name = MQualifiedTypeName.fromKotlinInternal(classifierName),
					outerType = outerType?.toMeta(),
				)
			}
		}

		is KmClassifier.TypeAlias -> MTypeReference.TypeAlias(
			abbreviatedType = abbreviatedType?.toMeta(),
			annotations = annotations,
			arguments = arguments,
			flexibleTypeUpperBound = flexBound,
			isNullable = isNullable,
			isRaw = isRaw,
			name = MQualifiedTypeName.fromKotlinInternal(classifier.name),
		)

		is KmClassifier.TypeParameter -> MTypeReference.TypeParameter(
			annotations = annotations,
			arguments = arguments,
			flexibleTypeUpperBound = flexBound,
			id = MTypeParameterId(classifier.id),
			isRaw = isRaw,
			isNullable = isNullable,
		)
	}
}


private fun buildFunctionTypeReference(
	isSuspend: Boolean,
	arguments: List<MTypeArgument>,
	annotations: List<MAnnotation>,
	flexBound: MFlexibleTypeUpperBound?,
	isNullable: Boolean,
): MTypeReference.Function {
	val isExtension = annotations.any { it.className.kotlinInternal == extensionFunctionTypeAnnotation }
	val filteredAnnotations = annotations.filterNot { it.className.kotlinInternal == extensionFunctionTypeAnnotation }

	val receiverArgument: MTypeArgument?
	val parameterArguments: List<MTypeArgument>
	val returnArgument: MTypeArgument

	if (arguments.isEmpty()) {
		receiverArgument = null
		parameterArguments = emptyList()
		returnArgument = MTypeArgument.StarProjection
	}
	else {
		returnArgument = arguments.last()

		val nonReturnArguments = arguments.dropLast(1)
		if (isExtension && nonReturnArguments.isNotEmpty()) {
			receiverArgument = nonReturnArguments.first()
			parameterArguments = nonReturnArguments.drop(1)
		}
		else {
			receiverArgument = null
			parameterArguments = nonReturnArguments
		}
	}

	return MTypeReference.Function(
		annotations = filteredAnnotations,
		flexibleTypeUpperBound = flexBound,
		isNullable = isNullable,
		isSuspend = isSuspend,
		parameterArguments = parameterArguments,
		receiverArgument = receiverArgument,
		returnArgument = returnArgument,
	)
}


// -- KmFlexibleTypeUpperBound -----------------------------------------------------------------------------------------

internal fun KmFlexibleTypeUpperBound.toMeta(): MFlexibleTypeUpperBound = MFlexibleTypeUpperBound(
	type = type.toMeta(),
	typeFlexibilityId = typeFlexibilityId?.let(::MTypeFlexibilityId),
)


// -- KmTypeProjection -------------------------------------------------------------------------------------------------

internal fun KmTypeProjection.toMeta(): MTypeArgument {
	val variance = variance ?: return MTypeArgument.StarProjection
	return MTypeArgument.Type(
		type = type!!.toMeta(),
		variance = MVariance(variance),
	)
}


// -- KmContract -------------------------------------------------------------------------------------------------------

internal fun KmContract.toMeta(): MContract = MContract(
	effects = effects.map { it.toMeta() },
)


// -- KmEffect ---------------------------------------------------------------------------------------------------------

internal fun KmEffect.toMeta(): MEffect = when (type) {
	KmEffectType.CALLS -> MEffect.CallsInPlace(
		invocationKind = when (invocationKind) {
			KmEffectInvocationKind.AT_LEAST_ONCE -> MEffect.InvocationKind.AT_LEAST_ONCE
			KmEffectInvocationKind.AT_MOST_ONCE -> MEffect.InvocationKind.AT_MOST_ONCE
			KmEffectInvocationKind.EXACTLY_ONCE -> MEffect.InvocationKind.EXACTLY_ONCE
			null -> MEffect.InvocationKind.UNKNOWN
		},
		parameterIndex = constructorArguments.firstOrNull()?.parameterIndex?.let(::MValueParameterIndex)
			?: error("callsInPlace() effect is missing a parameter index"),
	)

	KmEffectType.RETURNS_CONSTANT -> MEffect.Returns(
		condition = conclusion?.toMeta(),
		returnValue = constructorArguments.firstOrNull()?.let { expr ->
			when (val cv = expr.constantValue) {
				null -> null
				else -> when (cv.value) {
					true -> MEffect.ReturnValue.TRUE
					false -> MEffect.ReturnValue.FALSE
					null -> MEffect.ReturnValue.NULL
					else -> null
				}
			}
		},
	)

	KmEffectType.RETURNS_NOT_NULL -> MEffect.Returns(
		condition = conclusion?.toMeta(),
		returnValue = MEffect.ReturnValue.NOT_NULL,
	)
}


// -- KmEffectExpression -----------------------------------------------------------------------------------------------

internal fun KmEffectExpression.toMeta(): MEffectExpression {
	val andArguments = andArguments.map { it.toMeta() }
	val orArguments = orArguments.map { it.toMeta() }
	val parameterIndex = parameterIndex?.let(::MValueParameterIndex)

	return when {
		isNullCheckPredicate -> MEffectExpression.IsNull(
			andArguments = andArguments,
			isNegated = isNegated,
			orArguments = orArguments,
			parameterIndex = parameterIndex ?: error("is-null effect expression is missing a parameter index"),
		)

		isInstanceType != null -> MEffectExpression.IsInstance(
			andArguments = andArguments,
			instanceType = isInstanceType!!.toMeta(),
			isNegated = isNegated,
			orArguments = orArguments,
			parameterIndex = parameterIndex ?: error("is-instance effect expression is missing a parameter index"),
		)

		parameterIndex != null -> MEffectExpression.IsTrue(
			andArguments = andArguments,
			isNegated = isNegated,
			orArguments = orArguments,
			parameterIndex = parameterIndex,
		)

		constantValue != null && constantValue!!.value is Boolean -> MEffectExpression.Constant(
			andArguments = andArguments,
			orArguments = orArguments,
			value = constantValue!!.value as Boolean,
		)

		else -> MEffectExpression.Empty(
			andArguments = andArguments,
			orArguments = orArguments,
		)
	}
}


// -- KmTypeAlias ------------------------------------------------------------------------------------------------------

internal fun KmTypeAlias.toMeta(): MTypeAlias = MTypeAlias(
	annotations = annotations.map(::MAnnotation),
	expandedType = expandedType.toMeta(),
	name = MQualifiedTypeName.fromKotlinInternal(name),
	typeParameters = typeParameters.map { it.toMeta() },
	underlyingType = underlyingType.toMeta(),
	versionRequirements = versionRequirements.map { it.toMeta() },
	visibility = MVisibility.from(visibility),
)


// -- KmVersionRequirement ---------------------------------------------------------------------------------------------

internal fun KmVersionRequirement.toMeta(): MVersionRequirement = MVersionRequirement(
	errorCode = errorCode,
	kind = when (kind) {
		KmVersionRequirementVersionKind.API_VERSION -> MVersionRequirement.Kind.API
		KmVersionRequirementVersionKind.COMPILER_VERSION -> MVersionRequirement.Kind.COMPILER
		KmVersionRequirementVersionKind.LANGUAGE_VERSION -> MVersionRequirement.Kind.LANGUAGE
		KmVersionRequirementVersionKind.UNKNOWN -> MVersionRequirement.Kind.LANGUAGE
	},
	level = when (level) {
		KmVersionRequirementLevel.ERROR -> MVersionRequirement.Level.ERROR
		KmVersionRequirementLevel.HIDDEN -> MVersionRequirement.Level.HIDDEN
		KmVersionRequirementLevel.WARNING -> MVersionRequirement.Level.WARNING
	},
	message = message,
	version = MVersion(version.major, version.minor, version.patch),
)


// -- KmPackage --------------------------------------------------------------------------------------------------------

internal fun KmPackage.toMeta(facadeClassName: MQualifiedTypeName, kotlinPackageName: MPackageName?): MFile {
	var resolvedFacadeClassName = facadeClassName
	var jvmPackageName: MPackageName? = null

	if (kotlinPackageName != null) {
		jvmPackageName = facadeClassName.packageName
		resolvedFacadeClassName = facadeClassName.withPackage(kotlinPackageName)
	}

	return MFile(
		facadeClassName = resolvedFacadeClassName,
		functions = functions.map { it.toMeta() },
		localDelegatedProperties = localDelegatedProperties.map { it.toMeta() },
		jvmPackageName = jvmPackageName,
		properties = properties.map { it.toMeta() },
		typeAliases = typeAliases.map { it.toMeta() },
	)
}


// -- KmModule ---------------------------------------------------------------------------------------------------------

@OptIn(UnstableMetadataApi::class)
internal fun KmModule.toMeta(name: MModuleName): MModule = MModule(
	annotations = emptyList(),
	name = name,
	packages = packageParts.map { (fqName, parts) ->
		MPackage(
			fileTypes = parts.fileFacades.map { MQualifiedTypeName.fromKotlinInternal(it) },
			multiFileClassParts = parts.multiFileClassParts.entries
				.associate { MQualifiedTypeName.fromKotlinInternal(it.key) to MQualifiedTypeName.fromKotlinInternal(it.value) },
			name = MPackageName.fromKotlinInternal(fqName),
		)
	},
)


// -- KmLambda ---------------------------------------------------------------------------------------------------------

internal fun KmLambda.toMeta(): MLambda = MLambda(
	function = function.toMeta(),
)
