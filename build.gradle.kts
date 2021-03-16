import io.fluidsonic.gradle.*

plugins {
	id("io.fluidsonic.gradle") version "1.1.22"
}

fluidLibrary(name = "meta", version = "0.11.2")

fluidLibraryModule(description = "Converts Kotlin metadata into an easily usable data model") {
	targets {
		jvm {
			dependencies {
				implementation(kotlinx("metadata-jvm", "0.2.0"))
			}
		}
	}
}
