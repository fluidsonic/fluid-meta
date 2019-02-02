import com.github.fluidsonic.fluid.library.*

plugins {
	id("com.github.fluidsonic.fluid-library") version "0.9.0"
}

fluidLibrary {
	name = "fluid-meta"
	version = "0.9.1"
}

fluidLibraryVariant {
	description = "Converts Kotlin metadata into a usable data model"
	jdk = JDK.v1_8
}

dependencies {
	api(fluid("stdlib-jdk8", "0.9.1"))

	implementation(kotlinx("metadata-jvm", "0.0.5"))
}

repositories {
	bintray("fluidsonic/maven")
}
