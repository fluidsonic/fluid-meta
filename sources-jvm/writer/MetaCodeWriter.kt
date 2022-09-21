package io.fluidsonic.meta


internal class MetaCodeWriter private constructor() {

	private val visitedTypeParameterInCurrentRecursion: MutableSet<MTypeParameterId> = mutableSetOf()
	private val writer = IndentableWriter()


	inline fun <R> indented(block: () -> R) =
		writer.indented(block)


	inline fun <R> indentedIf(condition: Boolean, block: () -> R) =
		if (condition)
			writer.indented(block)
		else
			block()


	override fun toString() =
		writer.toString()


	fun write(meta: Meta) {
		when (meta) {
			is MModule -> writeModule(meta)
			is MType -> writeType(meta)
			else -> write("/* unknown meta element ${meta::class.java} */")
		}
	}


	private fun write(raw: Char) {
		writer.write(raw)
	}


	private fun write(raw: String) {
		writer.write(raw)
	}


	private fun writeAnnotation(value: MAnnotation, inline: Boolean, isNested: Boolean = false) {
		if (!isNested) write("@")
		writeQualifiedTypeName(value.className)

		if (value.arguments.isNotEmpty()) {
			val isMultiline = !inline && value.arguments.size > 1

			write(if (isMultiline) "(\n" else "(")
			indentedIf(isMultiline) {
				value.arguments.entries.forEachIndexed { index, (variableName, argument) ->
					if (index > 0)
						write(if (isMultiline) ",\n" else ", ")

					if (variableName != defaultAnnotationParameterName) {
						writeVariableName(variableName)
						write(" = ")
					}
					writeAnnotationArgument(argument, inline = inline)
				}
			}
			write(if (isMultiline) "\n)" else ")")
		}
	}


	private fun writeAnnotations(values: Collection<MAnnotation>?, inline: Boolean = false) {
		if (values.isNullOrEmpty()) return

		values.forEachIndexed { index, annotation ->
			if (index > 0) write(if (inline) " " else "\n")
			writeAnnotation(annotation, inline = inline)
		}
		if (!inline) write("\n")
	}


	private fun writeAnnotationArgument(value: MAnnotationArgument<*>, inline: Boolean) {
		when (value) {
			is MAnnotationArgument.AnnotationValue -> writeAnnotation(value.value, inline = inline, isNested = true)
			is MAnnotationArgument.ArrayValue -> when {
				value.value.isEmpty() -> write("[]")
				else -> {
					val isMultiline = !inline && value.value.size > 1

					write(if (isMultiline) "[\n" else "[ ")
					indentedIf(isMultiline) {
						value.value.forEachIndexed { index, argument ->
							if (index > 0)
								write(if (isMultiline) ",\n" else ", ")

							writeAnnotationArgument(argument, inline = inline)
						}
					}
					write(if (isMultiline) "\n]" else " ]")
				}
			}

			is MAnnotationArgument.BooleanValue -> writeLiteral(value.value)
			is MAnnotationArgument.ByteValue -> writeLiteral(value.value)
			is MAnnotationArgument.CharValue -> writeLiteral(value.value)
			is MAnnotationArgument.DoubleValue -> writeLiteral(value.value)
			is MAnnotationArgument.EnumValue -> {
				writeQualifiedTypeName(value.className)
				write(".")
				writeEnumEntryName(value.entryName)
			}

			is MAnnotationArgument.FloatValue -> writeLiteral(value.value)
			is MAnnotationArgument.IntValue -> writeLiteral(value.value)
			is MAnnotationArgument.KClassValue -> {
				writeQualifiedTypeName(value.value)
				write("::class")
			}

			is MAnnotationArgument.LongValue -> writeLiteral(value.value)
			is MAnnotationArgument.ShortValue -> writeLiteral(value.value)
			is MAnnotationArgument.StringValue -> writeLiteral(value.value)
			is MAnnotationArgument.UByteValue -> writeLiteral(value.value)
			is MAnnotationArgument.UIntValue -> writeLiteral(value.value)
			is MAnnotationArgument.ULongValue -> writeLiteral(value.value)
			is MAnnotationArgument.UShortValue -> writeLiteral(value.value)
		}
	}


	private inline fun writeBlock(block: () -> Unit) {
		write("{")
		indented(block)
		write("}")
	}


	private inline fun writeBlockIf(condition: Boolean, block: () -> Unit) {
		if (condition) {
			write("{")
			indented(block)
			write("}")
		}
		else
			block()
	}


	private fun writeClassDeclaration(value: MTypeName) {
		write("class ")
		writeTypeName(value)
		write("\n")
	}


	private fun writeClassMemberSource(value: MClassMemberSource?, isolated: Boolean = false) {
		if (value == null || (!isolated && value == MClassMemberSource.DECLARATION)) return

		write(when (value) {
			MClassMemberSource.DECLARATION -> "/* declared */"
			MClassMemberSource.DELEGATION -> "/* delegated */"
			MClassMemberSource.FAKE_OVERRIDE -> "/* fake override */"
			MClassMemberSource.SYNTHESIZED -> "/* synthesized */"
		})
		if (!isolated) write(" ")
	}


	private fun writeCompanionDeclaration(name: MTypeName) {
		write("companion object")
		if (name != kotlinCompanionTypeName) {
			write(" ")
			writeTypeName(name)
		}
		write("\n")
	}


	private fun writeConstKeyword(value: Boolean) {
		if (value) write("const ")
	}


	private fun writeConstructor(value: MConstructor, resolvedTypeParameters: Collection<MTypeParameter>) {
		writeVersionRequirements(value.versionRequirements)
		writeJvmMemberSignature(value.jvmSignature, usage = "method")
		writeVisibilityKeyword(value.visibility)
		writeSecondary(value.isSecondary)
		write("constructor")
		writeValueParameters(value.valueParameters, resolvedTypeParameters = resolvedTypeParameters)
		write("\n")
	}


	private fun writeContract(value: MContract, function: MFunction?, resolvedTypeParameters: Collection<MTypeParameter>) {
		write("contract ")
		writeBlock {
			write("\n")
			value.effects.forEach { writeEffect(it, function = function, resolvedTypeParameters = resolvedTypeParameters) }
		}
		write("\n")
	}


	private fun writeCrossinlineKeyword(value: Boolean) {
		if (value) write("crossinline ")
	}


	private fun writeDelegation(value: Boolean) {
		if (value) write(" by <delegate>")
	}


	private fun writeEffect(value: MEffect, function: MFunction?, resolvedTypeParameters: Collection<MTypeParameter>) {
		when (value) {
			is MEffect.CallsInPlace -> {
				write("callsInPlace(")
				write(function.kotlinParameterNameForIndex(value.parameterIndex))
				write(", ")
				writeEffectInvocationKind(value.invocationKind)
				write(")")
			}

			is MEffect.Returns -> {
				write(when (value.returnValue) {
					null -> "returns()"
					MEffect.ReturnValue.FALSE -> "returns(false)"
					MEffect.ReturnValue.NOT_NULL -> "returnsNotNull()"
					MEffect.ReturnValue.NULL -> "returns(null)"
					MEffect.ReturnValue.TRUE -> "returns(true)"
				})

				if (value.condition != null) {
					write(" implies ")
					if (value.condition !is MEffectExpression.Empty) write("(")
					writeEffectExpression(value.condition, function = function, resolvedTypeParameters = resolvedTypeParameters)
					if (value.condition !is MEffectExpression.Empty) write(")")
				}
			}
		}

		write("\n")
	}


	private fun writeEffectExpression(value: MEffectExpression, function: MFunction?, resolvedTypeParameters: Collection<MTypeParameter>) {
		when (value) {
			is MEffectExpression.Constant ->
				writeLiteral(value.value)

			is MEffectExpression.IsInstance -> {
				write(function.kotlinParameterNameForIndex(value.parameterIndex))
				write(if (value.isNegated) " !is " else " is ")
				writeTypeReference(value.instanceType, resolvedTypeParameters = resolvedTypeParameters)
			}

			is MEffectExpression.IsNull -> {
				write(function.kotlinParameterNameForIndex(value.parameterIndex))
				write(if (value.isNegated) " != null" else " == null")
			}

			is MEffectExpression.IsTrue -> {
				if (value.isNegated) write("!")
				write(function.kotlinParameterNameForIndex(value.parameterIndex))
			}

			is MEffectExpression.Empty ->
				Unit
		}

		if (value is MEffectExpression.Empty)
			write("(")

		value.andArguments.forEachIndexed { index, argument ->
			if (index > 0 || value !is MEffectExpression.Empty)
				write(" && ")

			writeEffectExpression(argument, function = function, resolvedTypeParameters = resolvedTypeParameters)
		}
		value.orArguments.forEachIndexed { index, argument ->
			if (index > 0 || value !is MEffectExpression.Empty)
				write(" || ")

			writeEffectExpression(argument, function = function, resolvedTypeParameters = resolvedTypeParameters)
		}

		if (value is MEffectExpression.Empty)
			write(")")
	}


	private fun writeEffectInvocationKind(value: MEffect.InvocationKind) {
		write("InvocationKind.")
		write(when (value) {
			MEffect.InvocationKind.AT_LEAST_ONCE -> "AT_LEAST_ONCE"
			MEffect.InvocationKind.AT_MOST_ONCE -> "AT_MOST_ONCE"
			MEffect.InvocationKind.EXACTLY_ONCE -> "EXACTLY_ONCE"
			MEffect.InvocationKind.UNKNOWN -> "UNKNOWN"
		})
	}


	private fun writeEnumEntryName(value: MEnumEntryName) {
		writeIdentifier(value.kotlin)
	}


	private fun writeEnumEntryNames(values: Collection<MEnumEntryName>) {
		values.forEachIndexed { index, entryName ->
			if (index > 0) write(",\n")
			writeEnumEntryName(entryName)
		}
		write(";")
		write("\n")
	}


	private fun writeExpectKeyword(value: Boolean) {
		if (value) write("expect ")
	}


	private fun writeExternalKeyword(value: Boolean) {
		if (value) write("external ")
	}


	private fun writeFunction(value: MFunction, resolvedTypeParameters: Collection<MTypeParameter>) {
		@Suppress("NAME_SHADOWING")
		val resolvedTypeParameters = resolvedTypeParameters + value.typeParameters

		writeVersionRequirements(value.versionRequirements)
		value.lambdaClassOriginName?.let { originName ->
			write("// lambda class origin: ")
			writeQualifiedTypeName(originName)
			write("\n")
		}
		writeJvmMemberSignature(value.jvmSignature, usage = "method")
		writeVisibilityKeyword(value.visibility)
		writeClassMemberSource(value.source)
		writeExpectKeyword(value.isExpect)
		writeExternalKeyword(value.isExternal)
		writeInheritanceRestrictionKeyword(value.inheritanceRestriction)
		writeTailrecKeyword(value.isTailrec)
		writeSuspendKeyword(value.isSuspend)
		writeInlineKeyword(value.isInline)
		writeInfixKeyword(value.isInfix)
		writeOperatorKeyword(value.isOperator)
		write("fun ")
		writeTypeParameters(value.typeParameters, resolvedTypeParameters = resolvedTypeParameters)
		if (value.typeParameters.isNotEmpty()) write(" ")
		writeReceiverParameterType(value.receiverParameterType, resolvedTypeParameters = resolvedTypeParameters)
		writeFunctionName(value.name)
		writeValueParameters(value.valueParameters, resolvedTypeParameters = resolvedTypeParameters)
		writeReturnType(value.returnType, resolvedTypeParameters = resolvedTypeParameters)
		val hasWhereClause = writeWhereClause(value.typeParameters, resolvedTypeParameters = resolvedTypeParameters)

		if (value.contract != null) {
			write(if (hasWhereClause) "\n" else " ")
			writeBlock {
				write("\n")
				writeContract(value.contract, function = value, resolvedTypeParameters = resolvedTypeParameters)
			}
		}

		write("\n")
	}


	private fun writeFunctionName(value: MFunctionName) {
		if (value == anonymousFunctionName) return

		writeIdentifier(value.kotlin)
	}


	private fun writeIdentifier(value: String) {
		value
			.splitToSequence('.')
			.forEachIndexed { index, part ->
				if (index > 0) write(".")

				val escapingNeeded = part.isNotEmpty() &&
					(!part.first().isJavaIdentifierStart() || !part.substring(1).all { it.isJavaIdentifierPart() } || kotlinKeywords.contains(part))

				if (escapingNeeded)
					write("`")
				write(part)
				if (escapingNeeded)
					write("`")
			}
	}


	private fun writeInfixKeyword(value: Boolean) {
		if (value) write("infix ")
	}


	private fun writeInheritanceRestrictionKeyword(value: MInheritanceRestriction?, isolated: Boolean = false) {
		if (value == null || (!isolated && value == MInheritanceRestriction.FINAL)) return

		write(when (value) {
			MInheritanceRestriction.ABSTRACT -> "abstract"
			MInheritanceRestriction.FINAL -> "final"
			MInheritanceRestriction.OPEN -> "open"
		})
		if (!isolated) write(" ")
	}


	private fun writeInlineKeyword(value: Boolean) {
		if (value) write("inline ")
	}


	private fun writeJvmMemberSignature(value: MJvmMemberSignature?, usage: String) {
		value ?: return

		write("// JVM ")
		write(usage)
		write(" = ")
		write(value.toString())
		write("\n")
	}


	private fun writeLateinitKeyword(value: Boolean) {
		if (value) write("lateinit ")
	}


	private fun writeLiteral(value: Boolean) {
		write(if (value) "true" else "false")
	}


	private fun writeLiteral(value: Byte) {
		write(value.toString())
		write(".toByte()")
	}


	private fun writeLiteral(value: Char) {
		write("'")

		if (value.isISOControl())
			write(String.format("\\u%04X", value.code))
		else
			when (value) {
				'\b' -> write("\\b")
				'\n' -> write("\\n")
				'\r' -> write("\\r")
				'\t' -> write("\\t")
				'\'' -> write("\\'")
				'\\' -> write("\\\\")
				else -> write(value)
			}

		write("'")
	}


	private fun writeLiteral(value: Double) {
		when {
			value.isFinite() -> write(value.toString())
			value.isInfinite() -> write(if (value > 0) "Double.POSITIVE_INFINITY" else "Double.NEGATIVE_INFINITY")
			else -> write("Double.NaN")
		}
	}


	private fun writeLiteral(value: Float) {
		when {
			value.isFinite() -> {
				write(value.toString())
				write("f")
			}

			value.isInfinite() -> write(if (value > 0) "Float.POSITIVE_INFINITY" else "Float.NEGATIVE_INFINITY")
			else -> write("Float.NaN")
		}
	}


	private fun writeLiteral(value: Int) {
		write(value.toString())
	}


	private fun writeLiteral(value: Long) {
		write(value.toString())
		write("L")
	}


	private fun writeLiteral(value: Short) {
		write(value.toString())
		write(".toShort()")
	}


	private fun writeLiteral(value: String) {
		write("\"")
		for (char in value) {
			if (char.isISOControl())
				write(String.format("\\u%04X", value.toInt()))
			else
				when (char) {
					'$' -> write("\\$")
					'\b' -> write("\\b")
					'\n' -> write("\\n")
					'\r' -> write("\\r")
					'\t' -> write("\\t")
					'\"' -> write("\\\"")
					'\\' -> write("\\\\")
					else -> write(char)
				}
		}
		write("\"")
	}


	private fun writeLiteral(value: UByte) {
		write(value.toString())
		write("u.toUByte()")
	}


	private fun writeLiteral(value: UInt) {
		write(value.toString())
		write("u")
	}


	private fun writeLiteral(value: ULong) {
		write(value.toString())
		write("uL")
	}


	private fun writeLiteral(value: UShort) {
		write(value.toString())
		write("u.toUShort()")
	}


	private fun writeModule(value: MModule) {
		writeAnnotations(value.annotations)
		write("module ")
		writeModuleName(value.name)

		if (value.packages.isNotEmpty()) {
			write(" ")
			writeBlock {
				write("\n\n")
				writePackages(value.packages)
			}
		}

		write("\n")
	}


	private fun writeModuleName(value: MModuleName) {
		writeIdentifier(value.kotlin)
	}


	private fun writeMultiFileClassParts(values: Map<MQualifiedTypeName, MQualifiedTypeName>) {
		values.entries
			.groupBy({ it.value }) { it.key }
			.forEach { (className, partClassNames) ->
				write("\n/* multipart */ class ")
				writeQualifiedTypeName(className)
				write(" ")
				writeBlock {
					write("\n\n")
					partClassNames.forEach { partClassName ->
						write("/* part */ class ")
						writeQualifiedTypeName(partClassName)
						write("\n")
					}
				}
				write("\n")
			}
	}


	private fun writeNoinlineKeyword(value: Boolean) {
		if (value) write("noinline ")
	}


	private fun writeOperatorKeyword(value: Boolean) {
		if (value) write("operator ")
	}


	private fun writeOuterType(value: MTypeReference?, resolvedTypeParameters: Collection<MTypeParameter>) {
		value ?: return

		writeTypeReference(value, resolvedTypeParameters = resolvedTypeParameters)
		write(".")
	}


	private fun writePackage(value: MPackage) {
		write("package ")
		writePackageName(value.name)

		if (value.multiFileClassParts.isNotEmpty()) {
			write(" ")
			writeBlock {
				write("\n")
				writeMultiFileClassParts(value.multiFileClassParts)
			}
		}

		write("\n")
	}


	private fun writePackageName(name: MPackageName) {
		if (name.isNotEmpty()) writeIdentifier(name.kotlin)
		else write("/* root */")
	}


	private fun writePackages(packages: Collection<MPackage>) {
		packages.forEachIndexed { index, pkg ->
			if (index > 0) write("\n")
			writePackage(pkg)
		}
	}


	private fun writeSecondary(value: Boolean) {
		if (value) write("/* secondary */ ")
	}


	private fun writeProperty(value: MProperty, resolvedTypeParameters: Collection<MTypeParameter>) {
		@Suppress("NAME_SHADOWING")
		val resolvedTypeParameters = resolvedTypeParameters + value.typeParameters

		writeVersionRequirements(value.versionRequirements)
		writeJvmMemberSignature(value.jvmFieldSignature, usage = "field")
		writeJvmMemberSignature(value.jvmSyntheticMethodForAnnotationsSignature, usage = "annotation-holding method")
		if (value.visibility == MVisibility.LOCAL) write("// ")
		writeVisibilityKeyword(value.visibility)
		writeClassMemberSource(value.source)
		writeInheritanceRestrictionKeyword(value.inheritanceRestriction)
		writeExpectKeyword(value.isExpect)
		writeExternalKeyword(value.isExternal)
		writeInlineKeyword(value.isInline)
		writeLateinitKeyword(value.isLateinit)
		writeConstKeyword(value.isConst)
		write(if (value.isVar) "var " else "val ")
		writeTypeParameters(value.typeParameters, resolvedTypeParameters = resolvedTypeParameters)
		if (value.typeParameters.isNotEmpty()) write(" ")
		writeReceiverParameterType(value.receiverParameterType, resolvedTypeParameters = resolvedTypeParameters)
		writeVariableName(value.name)
		writeReturnType(value.getter.returnType, includingUnit = true, resolvedTypeParameters = resolvedTypeParameters)
		writeWhereClause(value.typeParameters, resolvedTypeParameters = resolvedTypeParameters)
		writeDelegation(value.isDelegated)
		write("\n")
		writePropertyGetter(value.getter, property = value)
		writePropertySetter(value.setter, property = value, resolvedTypeParameters = resolvedTypeParameters)
	}


	private fun writePropertyGetter(value: MPropertyAccessor.Getter?, property: MProperty?) {
		if (value == null || (property != null && value.isDefault && (property.isInline || !value.isInline))) return

		indented {
			writeJvmMemberSignature(value.jvmSignature, usage = "method")
			if (property?.visibility == MVisibility.LOCAL) write("// ")
			writeExternalKeyword(value.isExternal)
			writeInlineKeyword(value.isInline && (property == null || !property.isInline))
			write("get")
			if (!value.isDefault) write("() = /* non-default */")
			write("\n")
		}
	}


	private fun writePropertySetter(value: MPropertyAccessor.Setter?, property: MProperty?, resolvedTypeParameters: Collection<MTypeParameter>) {
		if (value == null || (property != null && value.isDefault && (property.isInline || !value.isInline) && (value.visibility == property.visibility))) return

		indented {
			writeJvmMemberSignature(value.jvmSignature, usage = "method")
			if (property?.visibility == MVisibility.LOCAL) write("// ")
			writeVisibilityKeyword(value.visibility)
			writeExternalKeyword(value.isExternal)
			writeInlineKeyword(value.isInline && (property == null || !property.isInline))
			write("set")
			if (!value.isDefault) {
				writeValueParameters(listOf(value.parameter), resolvedTypeParameters = resolvedTypeParameters)
				write(" { /* non-default */ }")
			}
			write("\n")
		}
	}


	private fun writeQualifiedTypeName(qualifiedTypeName: MQualifiedTypeName, forceQualified: Boolean = false) {
		// TODO prevent unqualifying when resulting type name would become ambiguous
		if (!forceQualified && qualifiedTypeName.packageName.isDefaultImport)
			writeTypeName(qualifiedTypeName.withoutPackage())
		else
			writeIdentifier(qualifiedTypeName.kotlin)
	}


	private fun writeReceiverParameterType(value: MTypeReference?, resolvedTypeParameters: Collection<MTypeParameter>) {
		value ?: return

		writeTypeReference(value, resolvedTypeParameters = resolvedTypeParameters)
		write(".")
	}


	private fun writeReifiedKeyword(value: Boolean) {
		if (value) write("reified ")
	}


	private fun writeReturnType(value: MTypeReference?, includingUnit: Boolean = false, resolvedTypeParameters: Collection<MTypeParameter>) {
		if (value == null || (!includingUnit && value is MTypeReference.Class && value.name == kotlinUnitTypeName)) return

		write(": ")
		writeTypeReference(value, resolvedTypeParameters = resolvedTypeParameters)
	}


	private fun writeSuspendKeyword(value: Boolean) {
		if (value) write("suspend ")
	}


	private fun writeTailrecKeyword(value: Boolean) {
		if (value) write("tailrec ")
	}


	private fun writeType(value: MType) {
		val typeParameters = (value as? MGeneralizable)?.typeParameters ?: emptyList()

		if (value is MVersionRestrictable) writeVersionRequirements(value.versionRequirements)
		if (value is MClass) value.anonymousObjectOriginName?.let { originName ->
			write("// anonymous object origin: ")
			writeQualifiedTypeName(originName)
			write("\n")
		}
		if (value is MAnnotatable) writeAnnotations(value.annotations)
		if (value is MVisibilityRestrictable) writeVisibilityKeyword(value.visibility)
		if (value is MExpectable) writeExpectKeyword(value.isExpect)
		if (value is MExternalizable) writeExternalKeyword(value.isExternal)
		if (value is MInheritanceRestrictable) writeInheritanceRestrictionKeyword(value.inheritanceRestriction)
		if (value is MInlineable) writeInlineKeyword(value.isInline)

		write(when (value) {
			is MAnnotationClass -> "annotation class"
			is MClass -> {
				when (value.specialization) {
					MClass.Specialization.Data -> "data class"
					MClass.Specialization.Inner -> "inner class"
					is MClass.Specialization.Sealed -> "sealed class"
					null -> when (value.isValue) {
						true -> "value class"
						false -> "class"
					}
				}
			}

			is MEnumClass -> "enum class"
			is MEnumEntryClass -> "/* enum entry */ class"
			is MFile -> "/* file-level declarations */"
			is MInterface -> when (value.isFunctional) {
				true -> "fun interface"
				false -> "interface"
			}

			is MLambda -> {
				write("/* lambda */\n\n")
				writeFunction(value.function, resolvedTypeParameters = emptyList())
				return
			}

			is MMultiFileClass -> "/* multi-file class */"
			is MMultiFileClassPart -> "/* multi-file part */"
			is MObject ->
				if (value.isCompanion) "companion object"
				else "object"

			is MUnknown -> "/* unknown type */"
		})

		if (value is MNamedType) {
			write(" ")
			writeQualifiedTypeName(value.name, forceQualified = true)
		}

		writeTypeParameters(typeParameters, resolvedTypeParameters = typeParameters)

		if (value is MSupertypable && value.supertypes.isNotEmpty()) {
			val isMultiline = value.supertypes.size > 2

			indentedIf(isMultiline) {
				value.supertypes
					.filterNot { it is MTypeReference.Class && it.name == kotlinAnyTypeName }
					.filterNot { value is MEnumClass && it.name == kotlinEnumTypeName }
					.forEachIndexed { index, typeReference ->
						if (index > 0)
							write(if (isMultiline) ",\n" else ", ")
						else
							write(if (isMultiline) " :\n" else " : ")

						writeTypeReference(typeReference, resolvedTypeParameters = typeParameters)
					}
			}
		}

		val hasWhereClause = if (value is MGeneralizable) writeWhereClause(typeParameters, resolvedTypeParameters = typeParameters) else false

		val hasContent = when (value) {
			is MEnumClass, is MFile, is MMultiFileClass, is MMultiFileClassPart -> true
			else ->
				(value is MTypeAliasContainer && value.typeAliases.isNotEmpty()) ||
					(value is MTypeContainer && value.types.isNotEmpty()) ||
					(value is MPropertyContainer && value.properties.isNotEmpty()) ||
					(value is MConstructable && value.constructors.isNotEmpty()) ||
					(value is MFunctionContainer && value.functions.isNotEmpty()) ||
					(value is MLocalDelegatedPropertyContainer && value.localDelegatedProperties.isNotEmpty())
		}
		if (hasContent) {
			val wrapsInBlock = when (value) {
				is MFile, is MMultiFileClass, is MMultiFileClassPart -> false
				else -> true
			}

			if (wrapsInBlock) write(if (hasWhereClause) "\n" else " ")
			writeBlockIf(wrapsInBlock) {
				when (value) {
					is MFile -> {
						write("\n\n")
						if (value.jvmPackageName != null) {
							write("// JVM: ")
							write(value.facadeClassName.withPackage(value.jvmPackageName).jvmInternal)
							write("\n")
						}
						write("/* facade */ class ")
						writeQualifiedTypeName(value.facadeClassName, forceQualified = true)
						write("\n")
					}

					is MMultiFileClass -> {
						write("\n\n")
						write("/* facade */ class ")
						writeQualifiedTypeName(value.className, forceQualified = true)
						write("\n")
						value.partClassNames.forEach { partClassName ->
							write("\n/* part */ class ")
							writeQualifiedTypeName(partClassName)
						}
						write("\n")
					}

					is MMultiFileClassPart -> {
						write("\n")
						writeType(value.file)
					}

					else -> Unit
				}

				if (value is MEnumClass) {
					write("\n\n// *** ENUM ENTRIES ***\n")
					writeEnumEntryNames(value.entryNames)
				}

				if (value is MTypeAliasContainer && value.typeAliases.isNotEmpty()) {
					write("\n\n// *** TYPE ALIASES ***\n")
					value.typeAliases.forEach {
						write("\n")
						writeTypeAlias(it)
					}
				}

				if (value is MTypeContainer && value.types.isNotEmpty()) {
					write("\n\n// *** NESTED TYPES ***\n\n")

					if (value is MCompanionable) value.companionName?.let { writeCompanionDeclaration(it) } // https://youtrack.jetbrains.com/issue/KT-44722

					value.types
						.filterNot { value is MCompanionable && it == value.companionName }
						.forEach { writeClassDeclaration(it) } // https://youtrack.jetbrains.com/issue/KT-44722
				}

				if (value is MPropertyContainer && value.properties.isNotEmpty()) {
					write("\n\n// *** PROPERTIES ***\n")
					value.properties.forEach {
						write("\n")
						writeProperty(it, resolvedTypeParameters = typeParameters)
					}
				}

				if (value is MConstructable && value.constructors.isNotEmpty()) {
					write("\n\n// *** CONSTRUCTORS ***\n")
					value.constructors.forEach {
						write("\n")
						writeConstructor(it, resolvedTypeParameters = typeParameters)
					}
				}

				if (value is MFunctionContainer && value.functions.isNotEmpty()) {
					write("\n\n// *** FUNCTIONS ***\n")
					value.functions.forEach {
						write("\n")
						writeFunction(it, resolvedTypeParameters = typeParameters)
					}
				}

				if (value is MLocalDelegatedPropertyContainer && value.localDelegatedProperties.isNotEmpty()) {
					write("\n\n// *** LOCAL DELEGATED PROPERTIES ***\n")
					value.localDelegatedProperties.forEach {
						write("\n")
						writeProperty(it, resolvedTypeParameters = emptyList())
					}
				}
			}
			if (wrapsInBlock) write("\n")
		}
		else
			write("\n")
	}


	private fun writeTypeAlias(value: MTypeAlias) {
		writeVersionRequirements(value.versionRequirements)
		writeAnnotations(value.annotations)
		writeVisibilityKeyword(value.visibility)
		write("typealias ")
		writeQualifiedTypeName(value.name)
		writeTypeParameters(value.typeParameters, resolvedTypeParameters = value.typeParameters)
		write(" = ")
		writeTypeReference(value.underlyingType, resolvedTypeParameters = value.typeParameters)
		if (value.expandedType != value.underlyingType) {
			write(" /* = ")
			writeTypeReference(value.expandedType, resolvedTypeParameters = value.typeParameters)
			write(" */")
		}
		write("\n")
	}


	private fun writeTypeArgument(value: MTypeArgument, resolvedTypeParameters: Collection<MTypeParameter>) {
		when (value) {
			MTypeArgument.StarProjection ->
				write("*")

			is MTypeArgument.Type -> {
				writeVarianceKeyword(value.variance)
				writeTypeReference(value.type, resolvedTypeParameters = resolvedTypeParameters)
			}
		}
	}


	private fun writeTypeArguments(values: Collection<MTypeArgument>?, resolvedTypeParameters: Collection<MTypeParameter>) {
		if (values.isNullOrEmpty()) return

		write("<")
		values.forEachIndexed { index, argument ->
			if (index > 0) write(", ")
			writeTypeArgument(argument, resolvedTypeParameters = resolvedTypeParameters)
		}
		write(">")
	}


	private fun writeTypeName(value: MTypeName) {
		writeIdentifier(value.kotlin)
	}


	private fun writeTypeParameter(value: MTypeParameter, isolated: Boolean = false, resolvedTypeParameters: Collection<MTypeParameter>) {
		if (visitedTypeParameterInCurrentRecursion.contains(value.id)) {
			writeTypeParameterName(value.name)
			return
		}

		visitedTypeParameterInCurrentRecursion += value.id

		writeAnnotations(value.annotations, inline = true)
		if (value.annotations.isNotEmpty()) write(" ")
		writeReifiedKeyword(value.isReified)
		writeVarianceKeyword(value.variance)
		writeTypeParameterName(value.name)

		if (isolated) {
			value.upperBounds.forEachIndexed { index, upperBound ->
				write(if (index == 0) " : " else ", ")
				writeTypeReference(upperBound, resolvedTypeParameters = resolvedTypeParameters)
			}
		}
		else {
			value.upperBounds.singleOrNull()?.let { upperBound ->
				write(" : ")
				writeTypeReference(upperBound, resolvedTypeParameters = resolvedTypeParameters)
			}
		}

		visitedTypeParameterInCurrentRecursion -= value.id
	}


	private fun writeTypeParameterName(value: MTypeParameterName) {
		writeIdentifier(value.kotlin)
	}


	private fun writeTypeParameters(values: Collection<MTypeParameter>?, resolvedTypeParameters: Collection<MTypeParameter>) {
		if (values.isNullOrEmpty()) return

		write("<")
		values.forEachIndexed { index, parameter ->
			if (index > 0) write(", ")
			writeTypeParameter(parameter, resolvedTypeParameters = resolvedTypeParameters)
		}
		write(">")
	}


	private fun writeTypeReference(value: MTypeReference, resolvedTypeParameters: Collection<MTypeParameter>) {
		val flexibleTypeUpperBound = value.flexibleTypeUpperBound
		val flexibleAndEqualsExceptForNullability = flexibleTypeUpperBound != null && value.equalsExceptForNullability(flexibleTypeUpperBound.type)

		writeAnnotations(value.annotations, inline = true)
		if (value.annotations.isNotEmpty()) write(" ")

		if (flexibleTypeUpperBound != null && !flexibleAndEqualsExceptForNullability)
			write("(")

		when (value) {
			is MTypeReference.Class -> {
				if (value.isRaw) write("/* raw */ ")
				writeOuterType(value.outerType, resolvedTypeParameters = resolvedTypeParameters)
				if (value.abbreviatedType != null) {
					writeTypeReference(value.abbreviatedType, resolvedTypeParameters = resolvedTypeParameters)
					write(" /* = ")
				}
				if (value.outerType != null)
					writeTypeName(value.name.withoutPackage())
				else
					writeQualifiedTypeName(value.name)
				writeTypeArguments(value.arguments, resolvedTypeParameters = resolvedTypeParameters)
				if (value.isNullable) write("?")
				if (value.abbreviatedType != null) {
					write(" */")
				}
			}

			is MTypeReference.Function -> {
				if (value.isNullable) write("(")
				writeSuspendKeyword(value.isSuspend)
				if (value.receiverArgument != null) {
					writeTypeArgument(value.receiverArgument, resolvedTypeParameters = resolvedTypeParameters)
					write(".")
				}
				write("(")
				value.parameterArguments.forEachIndexed { index, argument ->
					if (index > 0) write(", ")
					writeTypeArgument(argument, resolvedTypeParameters = resolvedTypeParameters)
				}
				write(") -> ")
				writeTypeArgument(value.returnArgument, resolvedTypeParameters = resolvedTypeParameters)
				if (value.isNullable) write(")?")
			}

			is MTypeReference.TypeAlias -> {
				if (value.isRaw) write("/* raw */ ")
				if (value.abbreviatedType != null) {
					writeTypeReference(value.abbreviatedType, resolvedTypeParameters = resolvedTypeParameters)
					write(" /* ")
				}
				writeQualifiedTypeName(value.name)
				writeTypeArguments(value.arguments, resolvedTypeParameters = resolvedTypeParameters)
				if (value.isNullable) write("?")
				if (value.abbreviatedType != null) {
					write(" */")
				}
			}

			is MTypeReference.TypeParameter -> {
				if (value.isRaw) write("/* raw */ ")

				val typeParameter = resolvedTypeParameters.firstOrNull { it.id == value.id }
				if (typeParameter != null)
					writeTypeParameter(typeParameter, resolvedTypeParameters = resolvedTypeParameters)
				else
					write("T#${value.id}")

				writeTypeArguments(value.arguments, resolvedTypeParameters = resolvedTypeParameters)
				if (value.isNullable) write("?")
			}
		}

		value.flexibleTypeUpperBound?.let { flexibleUpperBound ->
			if (flexibleAndEqualsExceptForNullability)
				write("!")
			else {
				write("..")
				writeTypeReference(flexibleUpperBound.type, resolvedTypeParameters = resolvedTypeParameters)
			}
		}

		if (flexibleTypeUpperBound != null && !flexibleAndEqualsExceptForNullability)
			write(")")
	}


	private fun writeValueParameter(value: MValueParameter, resolvedTypeParameters: Collection<MTypeParameter>) {
		writeCrossinlineKeyword(value.isCrossinline)
		writeNoinlineKeyword(value.isNoinline)
		writeVarargKeyword(value.varargElementType != null)
		if (value.name.kotlin.startsWith(anonymousFunctionParameterNamePrefix))
			write("_")
		else
			writeVariableName(value.name)
		write(": ")
		writeTypeReference(value.varargElementType ?: value.type, resolvedTypeParameters = resolvedTypeParameters)
		if (value.varargElementType != null) {
			write(" /* ")
			writeTypeReference(value.type, resolvedTypeParameters = resolvedTypeParameters)
			write(" */")
		}
		if (value.declaresDefaultValue) write(" /* = default */")
	}


	private fun writeValueParameters(values: Collection<MValueParameter>?, resolvedTypeParameters: Collection<MTypeParameter>) {
		write("(")
		values?.forEachIndexed { index, parameter ->
			if (index > 0) write(", ")
			writeValueParameter(parameter, resolvedTypeParameters = resolvedTypeParameters)
		}
		write(")")
	}


	private fun writeVarargKeyword(value: Boolean) {
		if (value) write("vararg ")
	}


	private fun writeVariableName(value: MVariableName) {
		writeIdentifier(value.kotlin)
	}


	private fun writeVarianceKeyword(value: MVariance?, isolated: Boolean = false) {
		if (value == null || (!isolated && value == MVariance.INVARIANT)) return

		when (value) {
			MVariance.IN -> write("in")
			MVariance.OUT -> write("out")
			MVariance.INVARIANT -> return
		}
		if (!isolated) write(" ")
	}


	private fun writeVersionRequirement(value: MVersionRequirement) {
		write("// requires ")
		write(value.kind.toString())
		write(" version >= ")
		write(value.version.toString())
		write(", otherwise diagnostic ")
		write(value.level.toString())
		if (value.errorCode != null) {
			write(" ")
			write(value.errorCode.toString())
		}
		if (value.message != null) {
			write(": ")
			write(value.message)
		}
		write("\n")
	}


	private fun writeVersionRequirements(values: Collection<MVersionRequirement>?) {
		values?.forEach(::writeVersionRequirement)
	}


	private fun writeVisibilityKeyword(value: MVisibility?, isolated: Boolean = false) {
		if (value == null || (!isolated && value == MVisibility.PUBLIC)) return

		write(when (value) {
			MVisibility.INTERNAL -> "internal"
			MVisibility.LOCAL -> "/* local */"
			MVisibility.PROTECTED -> "protected"
			MVisibility.PRIVATE -> "private"
			MVisibility.PRIVATE_TO_THIS -> "private /* to this */"
			MVisibility.PUBLIC -> "public"
		})
		if (!isolated) write(" ")
	}


	private fun writeWhereClause(values: Collection<MTypeParameter>?, resolvedTypeParameters: Collection<MTypeParameter>): Boolean {
		values ?: return false

		val upperBounds = values
			.filter { it.upperBounds.size >= 2 }
			.flatMap { typeParameter -> typeParameter.upperBounds.map { typeParameter.name to it } }
		if (upperBounds.isEmpty()) return false

		write(" where\n")
		indented {
			upperBounds.forEachIndexed { index, (name, upperBound) ->
				if (index > 0) write(",\n")
				writeTypeParameterName(name)
				write(" : ")
				writeTypeReference(upperBound, resolvedTypeParameters = resolvedTypeParameters)
			}
		}

		return true
	}


	private fun MFunction?.kotlinParameterNameForIndex(index: MValueParameterIndex) =
		when (val position = index.value) {
			0 -> "this@${this?.name ?: "fun"}"
			else -> this?.valueParameters?.getOrNull(position - 1)?.name?.kotlin ?: "\$p$position"
		}


	companion object {

		private val anonymousFunctionName = MFunctionName("<anonymous>")
		private const val anonymousFunctionParameterNamePrefix = "<anonymous parameter"
		private val defaultAnnotationParameterName = MVariableName("value")
		private val kotlinAnyTypeName = MQualifiedTypeName.fromKotlinInternal("kotlin/Any")
		private val kotlinCompanionTypeName = MTypeName.fromKotlinInternal("Companion")
		private val kotlinEnumTypeName = MQualifiedTypeName.fromKotlinInternal("kotlin/Enum")
		private val kotlinUnitTypeName = MQualifiedTypeName.fromKotlinInternal("kotlin/Unit")

		private val kotlinKeywords = setOf(
			"as",
			"break",
			"class",
			"continue",
			"do",
			"else",
			"false",
			"for",
			"fun",
			"if",
			"in",
			"interface",
			"is",
			"null",
			"object",
			"package",
			"return",
			"super",
			"this",
			"throw",
			"true",
			"try",
			"typealias",
			"typeof",
			"val",
			"var",
			"when",
			"while"
		)


		private inline fun write(block: MetaCodeWriter.() -> Unit) =
			MetaCodeWriter().apply(block).toString()


		fun write(meta: Meta) =
			write { write(meta) }


		fun write(meta: MAnnotation) =
			write { writeAnnotation(meta, inline = false) }


		fun write(meta: MAnnotationArgument<*>) =
			write { writeAnnotationArgument(meta, inline = false) }


		fun write(meta: MClassMemberSource) =
			write { writeClassMemberSource(meta, isolated = true) }


		fun write(meta: MConstructor) =
			write { writeConstructor(meta, resolvedTypeParameters = emptyList()) }


		fun write(meta: MContract) =
			write { writeContract(meta, function = null, resolvedTypeParameters = emptyList()) }


		fun write(meta: MEffect) =
			write { writeEffect(meta, function = null, resolvedTypeParameters = emptyList()) }


		fun write(meta: MEffect.InvocationKind) =
			write { writeEffectInvocationKind(meta) }


		fun write(meta: MEffectExpression) =
			write { writeEffectExpression(meta, function = null, resolvedTypeParameters = emptyList()) }


		fun write(meta: MFunction) =
			write { writeFunction(meta, resolvedTypeParameters = emptyList()) }


		fun write(meta: MInheritanceRestriction) =
			write { writeInheritanceRestrictionKeyword(meta, isolated = true) }


		fun write(meta: MPackage) =
			write { writePackage(meta) }


		fun write(meta: MProperty) =
			write { writeProperty(meta, resolvedTypeParameters = emptyList()) }


		fun write(meta: MPropertyAccessor.Getter) =
			write { writePropertyGetter(meta, property = null) }


		fun write(meta: MPropertyAccessor.Setter) =
			write { writePropertySetter(meta, property = null, resolvedTypeParameters = emptyList()) }


		fun write(meta: MTypeAlias) =
			write { writeTypeAlias(meta) }


		fun write(meta: MTypeArgument) =
			write { writeTypeArgument(meta, resolvedTypeParameters = emptyList()) }


		fun write(meta: MTypeParameter) =
			write { writeTypeParameter(meta, isolated = true, resolvedTypeParameters = emptyList()) }


		fun write(meta: MTypeReference) =
			write { writeTypeReference(meta, resolvedTypeParameters = emptyList()) }


		fun write(meta: MValueParameter) =
			write { writeValueParameter(meta, resolvedTypeParameters = emptyList()) }


		fun write(meta: MVariance) =
			write { writeVarianceKeyword(meta, isolated = true) }


		fun write(meta: MVersionRequirement) =
			write { writeVersionRequirement(meta) }


		fun write(meta: MVisibility) =
			write { writeVisibilityKeyword(meta, isolated = true) }
	}
}
