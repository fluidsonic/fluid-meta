package com.github.fluidsonic.fluid.meta


internal inline fun <T, R> Iterable<T>?.mapOrEmpty(transform: (T) -> R) =
	this?.map(transform) ?: emptyList()


internal fun <T> Iterable<T>?.toListOrEmpty() =
	this?.toList() ?: emptyList()


internal fun Any.typeToString(vararg properties: Pair<String, Any?>) =
	buildString {
		if (this@typeToString is MIdentifyable)
			append(localId)
		else
			append(this@typeToString::class.java.simpleName ?: "?")

		if (properties.isNotEmpty()) {
			append(" {")

			for ((property, value) in properties) {
				append("\n\t")
				append(property)
				append(": ")

				if (value is Iterable<*>) {
					append('[')

					var hasValues = false

					for (listValue in value) {
						append("\n\t\t")
						appendLines(listValue, indentation = "\n\t\t")

						hasValues = true
					}

					if (hasValues) {
						append("\n\t")
					}

					append(']')
				}
				else {
					appendLines(value, indentation = "\n\t")
				}
			}

			append("\n}")
		}
	}


private fun StringBuilder.appendLines(value: Any?, indentation: String) {
	value.toString()
		.splitToSequence('\n')
		.withIndex()
		.forEach { (index, line) ->
			if (index > 0)
				append(indentation)

			append(line)
		}
}
