import io.fluidsonic.gradle.*

fluidJvmLibraryVariant(JvmTarget.jdk8) {
	publishing = false
}

dependencies {
	implementation(rootProject)
}
