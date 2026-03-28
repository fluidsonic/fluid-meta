# fluid-meta

Kotlin library that converts Kotlin metadata into an easily usable data model (JVM only).

## Build

Uses `io.fluidsonic.gradle` plugin. Build and test:

    ./gradlew build

Run tests only:

    ./gradlew jvmTest

## Conventions

- Tab indentation in all Kotlin and Gradle files.
- Source directories: `sources-jvm/` and `tests-jvm/`, configured by the fluid-gradle plugin.
- Release tags have no `v` prefix (e.g. `0.15.0`, not `v0.15.0`).
- Kotlin badge format: `Kotlin-2.3.20%20(JVM)`.

## Architecture

- `Meta.kt` — public API entry point (`Meta.of()`, `Meta.everythingFromSameSourceAs()`)
- `KmConversions.kt` — internal `toMeta()` extensions converting `kotlin.metadata` Km* types to M* model types
- `model/` — immutable data model (MType, MFunction, MProperty, etc.)
- `traits/` — mixin interfaces (MAnnotatable, MExecutable, etc.)
- `writer/` — MetaCodeWriter for pretty-printing metadata as Kotlin-like code
- `utility/` — small helpers

## Dependencies

Single runtime dependency: `org.jetbrains.kotlin:kotlin-metadata-jvm` (version aligned with Kotlin via BOM).
