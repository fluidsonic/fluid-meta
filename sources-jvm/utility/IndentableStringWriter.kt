package io.fluidsonic.meta


internal class IndentableWriter {

	private val builder = StringBuilder()
	private var currentLineIsIndented = false
	private var indentationLevel = 0


	inline fun <R> indented(block: () -> R) =
		try {
			indentationLevel += 1
			block()
		}
		finally {
			indentationLevel -= 1
		}


	override fun toString() =
		builder.toString()


	fun write(char: Char) {
		if (char == '\n')
			writeLinebreak()
		else {
			writeIndentationIfNeeded()
			builder.append(char)
		}
	}


	fun write(string: String) {
		var startIndex = 0
		while (startIndex < string.length) {
			val newlineIndex = string.indexOf('\n', startIndex = startIndex)
			val endIndex = if (newlineIndex >= 0) newlineIndex else string.length
			if (endIndex > startIndex) {
				writeIndentationIfNeeded()
				builder.append(string, startIndex, endIndex)
			}
			if (newlineIndex >= 0) {
				writeLinebreak()
			}

			startIndex = endIndex + 1
		}
	}


	private fun writeIndentationIfNeeded() {
		if (currentLineIsIndented) return
		repeat(indentationLevel) { builder.append('\t') }
		currentLineIsIndented = true
	}


	private fun writeLinebreak() {
		builder.append('\n')
		currentLineIsIndented = false
	}
}
