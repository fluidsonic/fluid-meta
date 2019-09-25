import com.github.fluidsonic.fluid.library.*

plugins {
	id("com.github.fluidsonic.fluid-library") version "0.9.24"
}

fluidJvmLibrary {
	name = "fluid-meta"
	version = "0.9.7"
}

fluidJvmLibraryVariant {
	description = "Converts Kotlin metadata into a usable data model"
	jdk = JvmTarget.jdk8
}

dependencies {
	api(fluid("stdlib", "0.9.25"))

	implementation(kotlinx("metadata-jvm", "0.1.0"))
}
