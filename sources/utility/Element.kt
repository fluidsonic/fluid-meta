package com.github.fluidsonic.fluid.meta

import javax.lang.model.element.*


internal val Element.`package`: PackageElement
	get() = this as? PackageElement ?: enclosingElement?.`package` ?: error("element has no package: $this")
