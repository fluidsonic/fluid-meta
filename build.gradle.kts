import io.fluidsonic.gradle.*

plugins {
	id("io.fluidsonic.gradle") version "1.1.8"
}

fluidLibrary(name = "meta", version = "0.10.1")

fluidLibraryModule(description = "Converts Kotlin metadata into an easily usable data model") {
	publishSingleTargetAsModule()

	targets {
		jvm {
			dependencies {
				implementation(kotlinx("metadata-jvm", "0.1.0"))
			}
		}
	}
}
