import com.github.fluidsonic.fluid.library.*

plugins {
	id("com.github.fluidsonic.fluid-library")
}

fluidLibraryVariant {
	publishing = false
}

dependencies {
	implementation(rootProject)
}
