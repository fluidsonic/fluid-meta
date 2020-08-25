fluid-meta
==========

[![Maven Central](https://img.shields.io/maven-central/v/io.fluidsonic.meta/fluid-meta?label=Maven%20Central)](https://search.maven.org/artifact/io.fluidsonic.meta/fluid-meta)
[![JCenter](https://img.shields.io/bintray/v/fluidsonic/kotlin/meta?label=JCenter)](https://bintray.com/fluidsonic/kotlin/meta)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.4.0-blue.svg)](https://github.com/JetBrains/kotlin/releases/v1.4.0)
[![#fluid-libraries Slack Channel](https://img.shields.io/badge/slack-%23fluid--libraries-543951.svg)](https://kotlinlang.slack.com/messages/C7UDFSVT2/)

Converts Kotlin metadata into a usable data model.
Includes pretty printing for easy inspection, see the output for [Kotlin's Standard Library](https://github.com/fluidsonic/fluid-meta/blob/master/kotlin-stdlib.kt) for example :)



Installation
------------

`build.gradle.kts`:
```kotlin
dependencies {
    implementation("io.fluidsonic.meta:fluid-meta:0.10.1")
}
```



Example
-------

Let's say you want to inspect the type `Hello` at runtime or during annotation processing and get additional type information which is specific to Kotlin, e.g.
nullability, internal visibility, data class, inline and default parameters.

```kotlin
package hello.world

internal data class Hello(
	private val world: String = "cool!",
	val foo: Int?
) {

	constructor() : this(foo = 3)

	inline fun hey() = println("hey")
}
```

All you need to do is to use `Meta.of(…)` to inspect the respective `KClass` (or `Element` when processing annotations):

```kotlin
package hello.world

import io.fluidsonic.meta.*

fun main() {
	println(Meta.of(Hello::class))
}
```

And you'll get well-structured metadata objects like `MClass` which print output like the following when using `.toString()`:

```kotlin
internal data class hello.world.Hello {

	// *** PROPERTIES ***

	// JVM field = foo:Ljava/lang/Integer;
	val foo: Int?

	// JVM field = world:Ljava/lang/String;
	private val world: String


	// *** CONSTRUCTORS ***

	// JVM method = <init>()V
	constructor()

	// JVM method = <init>(Ljava/lang/String;Ljava/lang/Integer;)V
	/* primary */ constructor(world: String /* = default */, foo: Int?)


	// *** FUNCTIONS ***

	// JVM method = component1()Ljava/lang/String;
	private /* synthesized */ operator fun component1(): String

	// JVM method = component2()Ljava/lang/Integer;
	/* synthesized */ operator fun component2(): Int?

	// JVM method = copy(Ljava/lang/String;Ljava/lang/Integer;)Lhello/world/Hello;
	/* synthesized */ fun copy(world: String /* = default */, foo: Int? /* = default */): hello.world.Hello

	// JVM method = equals(Ljava/lang/Object;)Z
	/* synthesized */ open operator fun equals(other: Any?): Boolean

	// JVM method = hashCode()I
	/* synthesized */ open fun hashCode(): Int

	// JVM method = hey()V
	inline fun hey()

	// JVM method = toString()Ljava/lang/String;
	/* synthesized */ open fun toString(): String
}
```



License
-------

Apache 2.0
