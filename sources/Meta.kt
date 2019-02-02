package com.github.fluidsonic.fluid.meta

import kotlinx.metadata.InconsistentKotlinMetadataException
import kotlinx.metadata.jvm.KotlinClassHeader
import kotlinx.metadata.jvm.KotlinClassMetadata
import kotlinx.metadata.jvm.KotlinModuleMetadata
import javax.lang.model.element.Element
import kotlin.reflect.KClass


object Meta {

	fun of(javaClass: Class<*>) =
		javaClass.getAnnotation(Metadata::class.java)?.let { of(it) }


	fun of(element: Element) =
		element.getAnnotation(Metadata::class.java)?.let { of(it) }


	fun of(kotlinClass: KClass<*>) =
		of(kotlinClass.java)


	fun of(metadata: Metadata) =
		KotlinClassMetadata(metadata)?.let { of(it) }


	fun of(metadata: KotlinClassMetadata): MType? =
		when (metadata) {
			is KotlinClassMetadata.Class -> of(metadata)
			is KotlinClassMetadata.FileFacade -> of(metadata)
			is KotlinClassMetadata.SyntheticClass -> of(metadata)
			is KotlinClassMetadata.MultiFileClassFacade -> of(metadata)
			is KotlinClassMetadata.MultiFileClassPart -> of(metadata)
			is KotlinClassMetadata.Unknown -> MUnknown
		}


	fun of(metadata: KotlinClassMetadata.Class) =
		withExceptionWrapping {
			MClassBuilder()
				.also { metadata.accept(it) }
				.build()
		}


	fun of(metadata: KotlinClassMetadata.FileFacade) =
		withExceptionWrapping {
			MFileFacadeBuilder()
				.also { metadata.accept(it) }
				.build()
		}


	fun of(metadata: KotlinClassMetadata.MultiFileClassFacade) =
		MMultiFileClassFacade(
			partClassNames = metadata.partClassNames.map { MQualifiedTypeName.fromKotlinInternal(it) }
		)


	fun of(metadata: KotlinClassMetadata.MultiFileClassPart) =
		withExceptionWrapping {
			MMultiFileClassPart(
				facadeClassName = MQualifiedTypeName.fromKotlinInternal(metadata.facadeClassName),
				fileFacade = MFileFacadeBuilder()
					.also { metadata.accept(it) }
					.build()
			)
		}


	fun of(metadata: KotlinClassMetadata.SyntheticClass) =
		if (metadata.isLambda)
			withExceptionWrapping {
				MLambdaBuilder()
					.also { metadata.accept(it) }
					.build()
			}
		else
			null


	fun of(metadata: KotlinModuleMetadata) =
		withExceptionWrapping {
			MModuleBuilder()
				.also { metadata.accept(it) }
				.build()
		}
}


@Suppress("FunctionName")
private fun KotlinClassMetadata(metadata: Metadata) =
	withExceptionWrapping {
		KotlinClassMetadata.read(KotlinClassHeader(
			kind = metadata.kind,
			metadataVersion = metadata.metadataVersion,
			bytecodeVersion = metadata.bytecodeVersion,
			data1 = metadata.data1,
			data2 = metadata.data2,
			extraInt = metadata.extraInt,
			extraString = metadata.extraString,
			packageName = metadata.extraString
		))
	}


private fun <R> withExceptionWrapping(action: () -> R) =
	try {
		action()
	}
	catch (e: InconsistentKotlinMetadataException) {
		throw MetaException(e.message, e)
	}
