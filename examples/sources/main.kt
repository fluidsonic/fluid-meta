import com.github.fluidsonic.fluid.meta.*
import java.io.FileWriter


fun main() {
	FileWriter("kotlin-stdlib.kt").use { writer ->
		writer.apply {
			write("// ------------------------------------------------------------------------------------------------------\n")
			write("// Example output for all elements for Kotlin's Standard Library ${KotlinVersion.CURRENT}\n")
			write("// ------------------------------------------------------------------------------------------------------\n\n")
		}

		Meta.printAllElementsFromSameSourceAs(Pair::class.java, destination = writer)
	}
}