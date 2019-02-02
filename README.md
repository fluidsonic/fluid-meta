fluid-meta
==========

[![Kotlin 1.3.20](https://img.shields.io/badge/Kotlin-1.3.20-blue.svg)](http://kotlinlang.org)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.fluidsonic/fluid-meta.svg)](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.github.fluidsonic%22%20a%3A%22fluid-meta%22)
[![#fluid-meta Slack Channel](https://img.shields.io/badge/slack-%23fluid--meta-543951.svg)](https://kotlinlang.slack.com/messages/CFW515D1A)

Converts Kotlin metadata into a usable data model



Installation
------------

This library is [available in Maven Central](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.github.fluidsonic%22%20a%3A%22fluid-meta%22) as `fluid-meta`
in the group `com.github.fluidsonic`.

`build.gradle.kts`:
```kotlin
dependencies {
    implementation("com.github.fluidsonic:fluid-meta:0.9.1")
}
```



Example
-------

Let's say you want to inspect the type `Hello` at runtime or during annotation processing and get additional type information which is specific to Kotlin, e.g.
nullability, internal visibility, data class, inline and default parameters.

```kotlin
package hello.world

internal data class Hello(
	val world: String = "cool!",
	val foo: Int
) {

	constructor() : this(foo = 3)

	inline fun hey() = println("hey")
}

```

All you need to do is to use `Meta.of(…)` to inspect the respective `KClass` (or `Element` when processing annotations):

```kotlin
package hello.world

import com.github.fluidsonic.fluid.meta.*

fun main() {
	println(Meta.of(Hello::class))
}
```

And you'll get this:

```text
MClass(
	anonymousObjectOriginName: null
	name: hello.world.Hello
	companion: null
	constructors: [
		MConstructor(
			hasAnnotations: false
			isPrimary: false
			jvmSignature: <init>()V
			modality: open
			valueParameters: []
			visibility: public
			versionRequirement: null
		)
		MConstructor(
			hasAnnotations: false
			isPrimary: true
			jvmSignature: <init>(Ljava/lang/String;I)V
			modality: final
			valueParameters: [
				MValueParameter(
					name: world
					declaresDefaultValue: true
					hasAnnotations: false
					isCrossinline: false
					isNoinline: false
					isVariadic: false
					type: MClassReference(
						abbreviatedType: null
						name: kotlin.String
						annotations: []
						arguments: []
						flexibilityTypeUpperBound: null
						hasAnnotations: false
						isNullable: false
						isRaw: false
						isSuspend: false
						outerType: null
					)
				)
				MValueParameter(
					name: foo
					declaresDefaultValue: false
					hasAnnotations: false
					isCrossinline: false
					isNoinline: false
					isVariadic: false
					type: MClassReference(
						abbreviatedType: null
						name: kotlin.Int
						annotations: []
						arguments: []
						flexibilityTypeUpperBound: null
						hasAnnotations: false
						isNullable: false
						isRaw: false
						isSuspend: false
						outerType: null
					)
				)
			]
			visibility: public
			versionRequirement: null
		)
	]
	enumEntries: []
	hasAnnotations: false
	functions: [
		MFunction(
			name: component1
			contract: null
			hasAnnotations: false
			isExpect: false
			isExternal: false
			isInfix: false
			isInline: false
			isOperator: true
			isSuspend: false
			isTailrec: false
			jvmSignature: component1()Ljava/lang/String;
			kind: synthesized
			lambdaClassOriginName: null
			modality: final
			receiverParameter: null
			returnType: MClassReference(
				abbreviatedType: null
				name: kotlin.String
				annotations: []
				arguments: []
				flexibilityTypeUpperBound: null
				hasAnnotations: false
				isNullable: false
				isRaw: false
				isSuspend: false
				outerType: null
			)
			typeParameters: []
			valueParameters: []
			visibility: public
			versionRequirement: null
		)
		MFunction(
			name: component2
			contract: null
			hasAnnotations: false
			isExpect: false
			isExternal: false
			isInfix: false
			isInline: false
			isOperator: true
			isSuspend: false
			isTailrec: false
			jvmSignature: component2()I
			kind: synthesized
			lambdaClassOriginName: null
			modality: final
			receiverParameter: null
			returnType: MClassReference(
				abbreviatedType: null
				name: kotlin.Int
				annotations: []
				arguments: []
				flexibilityTypeUpperBound: null
				hasAnnotations: false
				isNullable: false
				isRaw: false
				isSuspend: false
				outerType: null
			)
			typeParameters: []
			valueParameters: []
			visibility: public
			versionRequirement: null
		)
		MFunction(
			name: copy
			contract: null
			hasAnnotations: false
			isExpect: false
			isExternal: false
			isInfix: false
			isInline: false
			isOperator: false
			isSuspend: false
			isTailrec: false
			jvmSignature: copy(Ljava/lang/String;I)Lhello/world/Hello;
			kind: synthesized
			lambdaClassOriginName: null
			modality: final
			receiverParameter: null
			returnType: MClassReference(
				abbreviatedType: null
				name: hello.world.Hello
				annotations: []
				arguments: []
				flexibilityTypeUpperBound: null
				hasAnnotations: false
				isNullable: false
				isRaw: false
				isSuspend: false
				outerType: null
			)
			typeParameters: []
			valueParameters: [
				MValueParameter(
					name: world
					declaresDefaultValue: true
					hasAnnotations: false
					isCrossinline: false
					isNoinline: false
					isVariadic: false
					type: MClassReference(
						abbreviatedType: null
						name: kotlin.String
						annotations: []
						arguments: []
						flexibilityTypeUpperBound: null
						hasAnnotations: false
						isNullable: false
						isRaw: false
						isSuspend: false
						outerType: null
					)
				)
				MValueParameter(
					name: foo
					declaresDefaultValue: true
					hasAnnotations: false
					isCrossinline: false
					isNoinline: false
					isVariadic: false
					type: MClassReference(
						abbreviatedType: null
						name: kotlin.Int
						annotations: []
						arguments: []
						flexibilityTypeUpperBound: null
						hasAnnotations: false
						isNullable: false
						isRaw: false
						isSuspend: false
						outerType: null
					)
				)
			]
			visibility: public
			versionRequirement: null
		)
		MFunction(
			name: equals
			contract: null
			hasAnnotations: false
			isExpect: false
			isExternal: false
			isInfix: false
			isInline: false
			isOperator: true
			isSuspend: false
			isTailrec: false
			jvmSignature: equals(Ljava/lang/Object;)Z
			kind: synthesized
			lambdaClassOriginName: null
			modality: open
			receiverParameter: null
			returnType: MClassReference(
				abbreviatedType: null
				name: kotlin.Boolean
				annotations: []
				arguments: []
				flexibilityTypeUpperBound: null
				hasAnnotations: false
				isNullable: false
				isRaw: false
				isSuspend: false
				outerType: null
			)
			typeParameters: []
			valueParameters: [
				MValueParameter(
					name: other
					declaresDefaultValue: false
					hasAnnotations: false
					isCrossinline: false
					isNoinline: false
					isVariadic: false
					type: MClassReference(
						abbreviatedType: null
						name: kotlin.Any
						annotations: []
						arguments: []
						flexibilityTypeUpperBound: null
						hasAnnotations: true
						isNullable: true
						isRaw: false
						isSuspend: false
						outerType: null
					)
				)
			]
			visibility: public
			versionRequirement: null
		)
		MFunction(
			name: hashCode
			contract: null
			hasAnnotations: false
			isExpect: false
			isExternal: false
			isInfix: false
			isInline: false
			isOperator: false
			isSuspend: false
			isTailrec: false
			jvmSignature: hashCode()I
			kind: synthesized
			lambdaClassOriginName: null
			modality: open
			receiverParameter: null
			returnType: MClassReference(
				abbreviatedType: null
				name: kotlin.Int
				annotations: []
				arguments: []
				flexibilityTypeUpperBound: null
				hasAnnotations: false
				isNullable: false
				isRaw: false
				isSuspend: false
				outerType: null
			)
			typeParameters: []
			valueParameters: []
			visibility: public
			versionRequirement: null
		)
		MFunction(
			name: hey
			contract: null
			hasAnnotations: false
			isExpect: false
			isExternal: false
			isInfix: false
			isInline: true
			isOperator: false
			isSuspend: false
			isTailrec: false
			jvmSignature: hey()V
			kind: declaration
			lambdaClassOriginName: null
			modality: final
			receiverParameter: null
			returnType: MClassReference(
				abbreviatedType: null
				name: kotlin.Unit
				annotations: []
				arguments: []
				flexibilityTypeUpperBound: null
				hasAnnotations: false
				isNullable: false
				isRaw: false
				isSuspend: false
				outerType: null
			)
			typeParameters: []
			valueParameters: []
			visibility: public
			versionRequirement: null
		)
		MFunction(
			name: toString
			contract: null
			hasAnnotations: false
			isExpect: false
			isExternal: false
			isInfix: false
			isInline: false
			isOperator: false
			isSuspend: false
			isTailrec: false
			jvmSignature: toString()Ljava/lang/String;
			kind: synthesized
			lambdaClassOriginName: null
			modality: open
			receiverParameter: null
			returnType: MClassReference(
				abbreviatedType: null
				name: kotlin.String
				annotations: []
				arguments: []
				flexibilityTypeUpperBound: null
				hasAnnotations: false
				isNullable: false
				isRaw: false
				isSuspend: false
				outerType: null
			)
			typeParameters: []
			valueParameters: []
			visibility: public
			versionRequirement: null
		)
	]
	isExpect: false
	isExternal: false
	isInline: false
	isInner: false
	kind: data class
	localDelegatedProperties: []
	modality: final
	nestedClasses: []
	properties: [
		MProperty(
			name: foo
			getterIsExternal: false
			getterIsInline: false
			getterIsNotDefault: false
			hasAnnotations: false
			hasConstant: false
			hasGetter: true
			hasSetter: false
			isConst: false
			isDelegated: false
			isExpect: false
			isExternal: false
			isLateinit: false
			isVar: false
			jvmFieldSignature: foo:I
			jvmGetterSignature: getFoo()I
			jvmSetterSignature: null
			jvmSyntheticMethodForAnnotationsSignature: null
			kind: declaration
			modality: final
			receiverParameter: null
			returnType: MClassReference(
				abbreviatedType: null
				name: kotlin.Int
				annotations: []
				arguments: []
				flexibilityTypeUpperBound: null
				hasAnnotations: false
				isNullable: false
				isRaw: false
				isSuspend: false
				outerType: null
			)
			setterIsExternal: false
			setterIsInline: false
			setterIsNotDefault: false
			setterParameter: null
			typeParameters: []
			visibility: public
			versionRequirement: null
		)
		MProperty(
			name: world
			getterIsExternal: false
			getterIsInline: false
			getterIsNotDefault: false
			hasAnnotations: false
			hasConstant: false
			hasGetter: true
			hasSetter: false
			isConst: false
			isDelegated: false
			isExpect: false
			isExternal: false
			isLateinit: false
			isVar: false
			jvmFieldSignature: world:Ljava/lang/String;
			jvmGetterSignature: getWorld()Ljava/lang/String;
			jvmSetterSignature: null
			jvmSyntheticMethodForAnnotationsSignature: null
			kind: declaration
			modality: final
			receiverParameter: null
			returnType: MClassReference(
				abbreviatedType: null
				name: kotlin.String
				annotations: []
				arguments: []
				flexibilityTypeUpperBound: null
				hasAnnotations: false
				isNullable: false
				isRaw: false
				isSuspend: false
				outerType: null
			)
			setterIsExternal: false
			setterIsInline: false
			setterIsNotDefault: false
			setterParameter: null
			typeParameters: []
			visibility: public
			versionRequirement: null
		)
	]
	sealedSubclasses: []
	supertype: MClassReference(
		abbreviatedType: null
		name: kotlin.Any
		annotations: []
		arguments: []
		flexibilityTypeUpperBound: null
		hasAnnotations: false
		isNullable: false
		isRaw: false
		isSuspend: false
		outerType: null
	)
	typeAliases: []
	typeParameters: []
	visibility: internal
	versionRequirement: null
)
```



License
-------

Apache 2.0
