import com.github.fluidsonic.fluid.library.*
import org.jetbrains.kotlin.gradle.plugin.*

plugins {
	id("com.github.fluidsonic.fluid-library") version "0.9.25"
}

fluidJvmLibrary {
	name = "fluid-meta"
	version = "0.9.10"
}

fluidJvmLibraryVariant {
	description = "Converts Kotlin metadata into a usable data model"
	jdk = JvmTarget.jdk8
}

dependencies {
	api(fluid("stdlib", "0.9.25")) {
		attributes {
			attribute(KotlinPlatformType.attribute, KotlinPlatformType.jvm)
			attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 8)
		}
	}

	implementation(kotlinx("metadata-jvm", "0.1.0"))
}
