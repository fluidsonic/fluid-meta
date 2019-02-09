pluginManagement {
	repositories {
		gradlePluginPortal()
		maven("https://dl.bintray.com/fluidsonic/maven")
	}
}

rootProject.name = "fluid-meta-jvm"

include("examples")

project(":examples").name = "fluid-meta-examples"
