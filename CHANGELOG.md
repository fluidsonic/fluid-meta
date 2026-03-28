# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/) and this project adheres to [Semantic Versioning](https://semver.org/).

## [0.15.0] - 2026-03-28

### Changed
- Migrated from `kotlinx-metadata-jvm` to `kotlin-metadata-jvm` 2.x (property-based API).
- Updated to Kotlin 2.3.20 and Gradle 9.4.1.
- Updated to `io.fluidsonic.gradle` plugin 3.0.0.
- Requires JDK 21+.
- Default branch renamed from `master` to `main`.

### Added
- KDoc on all public API declarations.
- Comprehensive unit test suite (40 tests).
- GitHub Actions CI workflow.
- `CLAUDE.md` project documentation.
- `CHANGELOG.md`.

### Removed
- Visitor-based builder classes (replaced by direct property-based conversion).
- `Flag.kt` utility (Flag/Flags API removed in kotlin-metadata 2.x).
- Deprecated `mapOrEmpty()` and `toListOrEmpty()` utility functions.

## [0.14.0]

## [0.13.0]

## [0.12.0]
