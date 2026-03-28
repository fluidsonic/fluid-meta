import io.fluidsonic.gradle.*

plugins {
	id("io.fluidsonic.gradle") version "3.0.0"
}

fluidLibrary(name = "meta", version = "0.15.0")

fluidLibraryModule(description = "Converts Kotlin metadata into an easily usable data model") {
	targets {
		jvm {
			dependencies {
				implementation(kotlin("metadata-jvm"))
			}
		}
	}
}
