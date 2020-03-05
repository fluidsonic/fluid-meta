import io.fluidsonic.gradle.*
import org.jetbrains.kotlin.gradle.plugin.*

plugins {
	id("io.fluidsonic.gradle") version "1.0.9"
}

fluidJvmLibrary(name = "meta", version = "0.9.16")

fluidJvmLibraryVariant(JvmTarget.jdk8) {
	description = "Converts Kotlin metadata into an easily usable data model"
}

dependencies {
	api(fluid("stdlib", "0.9.30")) {
		attributes {
			attribute(KotlinPlatformType.attribute, KotlinPlatformType.jvm)
			attribute(Usage.USAGE_ATTRIBUTE, objects.named(Usage::class.java, Usage.JAVA_RUNTIME))
			attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 8)
		}
	}

	implementation(kotlinx("metadata-jvm", "0.1.0"))
}
