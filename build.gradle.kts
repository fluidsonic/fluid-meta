import io.fluidsonic.gradle.*

plugins {
	id("io.fluidsonic.gradle") version "1.1.11"
}

fluidLibrary(name = "meta", version = "0.11.0")

fluidLibraryModule(description = "Converts Kotlin metadata into an easily usable data model") {
	publishSingleTargetAsModule()

	targets {
		jvm {
			dependencies {
				implementation(kotlinx("metadata-jvm", "0.2.0"))
			}
		}
	}
}
