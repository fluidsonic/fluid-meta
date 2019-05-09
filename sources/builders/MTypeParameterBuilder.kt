package com.github.fluidsonic.fluid.meta

import com.github.fluidsonic.fluid.stdlib.*
import kotlinx.metadata.*
import kotlinx.metadata.jvm.*


internal class MTypeParameterBuilder(
	private val flags: Flags,
	private val id: MTypeParameterId,
	private val name: MTypeParameterName,
	private val variance: MVariance
) : KmTypeParameterVisitor() {

	private var annotations: MutableList<MAnnotation>? = null
	private var upperBounds: MutableList<MTypeReferenceBuilder>? = null


	fun build() = MTypeParameter(
		annotations = annotations.toListOrEmpty(),
		id = id,
		isReified = Flag.TypeParameter.IS_REIFIED(flags),
		name = name,
		upperBounds = upperBounds.mapOrEmpty { it.build() },
		variance = variance
	)


	override fun visitExtensions(type: KmExtensionType) =
		(type == JvmTypeParameterExtensionVisitor.TYPE).thenTake {
			object : JvmTypeParameterExtensionVisitor() {

				override fun visitAnnotation(annotation: KmAnnotation) {
					MAnnotation(annotation).let {
						annotations?.apply { add(it) } ?: { annotations = mutableListOf(it) }()
					}
				}
			}
		}


	override fun visitUpperBound(flags: Flags) =
		MTypeReferenceBuilder(flags = flags)
			.also {
				upperBounds?.apply { add(it) } ?: { upperBounds = mutableListOf(it) }()
			}
}
