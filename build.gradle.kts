import io.fluidsonic.gradle.*

plugins {
	id("io.fluidsonic.gradle") version "1.1.25"
}

fluidLibrary(name = "meta", version = "0.12.0")

fluidLibraryModule(description = "Converts Kotlin metadata into an easily usable data model") {
	targets {
		jvm {
			dependencies {
				implementation(kotlinx("metadata-jvm", "0.4.1"))
			}
		}
	}
}
