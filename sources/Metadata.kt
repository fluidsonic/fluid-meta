package com.github.fluidsonic.fluid.meta

import kotlinx.metadata.InconsistentKotlinMetadataException
import kotlinx.metadata.jvm.KotlinClassHeader
import kotlinx.metadata.jvm.KotlinClassMetadata
import kotlinx.metadata.jvm.KotlinModuleMetadata
import kotlin.reflect.KClass


object Metadata {

	fun of(java: Class<*>) =
		of(java.getAnnotation(kotlin.Metadata::class.java))


	fun of(kotlin: KClass<*>) =
		of(kotlin.java)


	fun of(metadata: kotlin.Metadata) =
		KotlinClassMetadata(metadata)?.let { of(it) }


	fun of(metadata: KotlinClassMetadata): MType? =
		when (metadata) {
			is KotlinClassMetadata.Class -> ofClass(metadata)
			is KotlinClassMetadata.FileFacade -> ofFileFacade(metadata)
			is KotlinClassMetadata.SyntheticClass -> ofLambda(metadata)
			is KotlinClassMetadata.MultiFileClassFacade -> ofMultiFileClassFacade(metadata)
			is KotlinClassMetadata.MultiFileClassPart -> ofMultiFileClassPart(metadata)
			is KotlinClassMetadata.Unknown -> MUnknown
		}


	fun ofClass(java: Class<*>) =
		ofClass(java.getAnnotation(kotlin.Metadata::class.java))


	fun ofClass(kotlin: KClass<*>) =
		ofClass(kotlin.java)


	fun ofClass(metadata: kotlin.Metadata) =
		(KotlinClassMetadata(metadata) as? KotlinClassMetadata.Class)?.let { ofClass(it) }


	fun ofClass(metadata: KotlinClassMetadata.Class) =
		withExceptionWrapping {
			MClassBuilder()
				.also { metadata.accept(it) }
				.build()
		}


	fun ofFileFacade(java: Class<*>) =
		ofFileFacade(java.getAnnotation(kotlin.Metadata::class.java))


	fun ofFileFacade(kotlin: KClass<*>) =
		ofFileFacade(kotlin.java)


	fun ofFileFacade(metadata: kotlin.Metadata) =
		(KotlinClassMetadata(metadata) as? KotlinClassMetadata.FileFacade)?.let { ofFileFacade(it) }


	fun ofFileFacade(metadata: KotlinClassMetadata.FileFacade) =
		withExceptionWrapping {
			MFileFacadeBuilder()
				.also { metadata.accept(it) }
				.build()
		}


	fun ofLambda(java: Class<*>) =
		ofLambda(java.getAnnotation(kotlin.Metadata::class.java))


	fun ofLambda(kotlin: KClass<*>) =
		ofLambda(kotlin.java)


	fun ofLambda(metadata: kotlin.Metadata) =
		(KotlinClassMetadata(metadata) as? KotlinClassMetadata.SyntheticClass)?.let { ofLambda(it) }


	fun ofLambda(metadata: KotlinClassMetadata.SyntheticClass) =
		if (metadata.isLambda)
			withExceptionWrapping {
				MLambdaBuilder()
					.also { metadata.accept(it) }
					.build()
			}
		else
			null


	fun ofModule(metadata: KotlinModuleMetadata) =
		MModuleBuilder()
			.also { metadata.accept(it) }
			.build()


	fun ofMultiFileClassFacade(java: Class<*>) =
		ofMultiFileClassFacade(java.getAnnotation(kotlin.Metadata::class.java))


	fun ofMultiFileClassFacade(kotlin: KClass<*>) =
		ofMultiFileClassFacade(kotlin.java)


	fun ofMultiFileClassFacade(metadata: kotlin.Metadata) =
		(KotlinClassMetadata(metadata) as? KotlinClassMetadata.MultiFileClassFacade)?.let { ofMultiFileClassFacade(it) }


	fun ofMultiFileClassFacade(metadata: KotlinClassMetadata.MultiFileClassFacade) =
		MMultiFileClassFacade(
			partClassNames = metadata.partClassNames.map(::MTypeName)
		)


	fun ofMultiFileClassPart(java: Class<*>) =
		ofMultiFileClassPart(java.getAnnotation(kotlin.Metadata::class.java))


	fun ofMultiFileClassPart(kotlin: KClass<*>) =
		ofMultiFileClassPart(kotlin.java)


	fun ofMultiFileClassPart(metadata: kotlin.Metadata) =
		(KotlinClassMetadata(metadata) as? KotlinClassMetadata.MultiFileClassPart)?.let { ofMultiFileClassPart(it) }


	fun ofMultiFileClassPart(metadata: KotlinClassMetadata.MultiFileClassPart) =
		withExceptionWrapping {
			MMultiFileClassPart(
				facadeClassName = MTypeName(metadata.facadeClassName),
				fileFacade = MFileFacadeBuilder()
					.also { metadata.accept(it) }
					.build()
			)
		}
}


@Suppress("FunctionName")
private fun KotlinClassMetadata(metadata: kotlin.Metadata) =
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
		throw MetadataException(e.message, e)
	}
