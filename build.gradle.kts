import io.fluidsonic.gradle.*

plugins {
	id("io.fluidsonic.gradle") version "1.3.1"
}

fluidLibrary(name = "meta", version = "0.13.0")

fluidLibraryModule(description = "Converts Kotlin metadata into an easily usable data model") {
	targets {
		jvm {
			dependencies {
				implementation(kotlinx("metadata-jvm", "0.6.2"))
			}
		}
	}
}
