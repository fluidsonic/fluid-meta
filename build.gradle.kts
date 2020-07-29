import io.fluidsonic.gradle.*

plugins {
	id("io.fluidsonic.gradle") version "1.1.0"
}

fluidLibrary(name = "meta", version = "0.10.0")

fluidLibraryModule(description = "Converts Kotlin metadata into an easily usable data model") {
	publishSingleTargetAsModule()

	targets {
		jvm {
			dependencies {
				api(fluid("stdlib", "0.10.0"))

				implementation(kotlinx("metadata-jvm", "0.1.0"))
			}
		}
	}
}
