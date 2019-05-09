import com.github.fluidsonic.fluid.library.*

plugins {
	id("com.github.fluidsonic.fluid-library") version "0.9.10"
}

fluidJvmLibrary {
	name = "fluid-meta"
	version = "0.9.7"
}

fluidJvmLibraryVariant {
	description = "Converts Kotlin metadata into a usable data model"
	jdk = JDK.v1_8
}

dependencies {
	api(fluid("stdlib-jvm", "0.9.4"))

	implementation(kotlinx("metadata-jvm", "0.0.5.1"))
}
