pluginManagement {
	repositories {
		gradlePluginPortal()
		maven("https://dl.bintray.com/fluidsonic/kotlin")
	}
}

rootProject.name = "fluid-meta"

include("examples")

enableFeaturePreview("GRADLE_METADATA")
