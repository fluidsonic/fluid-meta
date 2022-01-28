package io.fluidsonic.meta

import kotlinx.metadata.*
import kotlinx.metadata.jvm.*
import java.io.*
import java.util.jar.*
import javax.lang.model.element.*
import kotlin.reflect.*


// FIXME improve API
public interface Meta {

	public companion object {

		private const val classFileSuffix = ".class"
		private const val kotlinModuleFileSuffix = ".kotlin_module"
		private val kotlinModuleJarEntryNamePattern = Regex("^META-INF/[^/]*" + Regex.escape(kotlinModuleFileSuffix))


		public fun allModulesFromSameSourceAs(javaClass: Class<*>): Sequence<MModule> {
			withExceptionWrapping {
				val file = javaClass.protectionDomain.codeSource?.location?.toURI()?.let(::File)
					?: return emptySequence()

				val dataSequences = when {
					file.isFile -> JarFile(file)
						.entries()
						.asSequence()
						.filter { kotlinModuleJarEntryNamePattern.matches(it.name) }
						.mapNotNull { entry ->
							javaClass.getResourceAsStream("/${entry.name}")?.readBytes()?.let { data ->
								val moduleName = MModuleName(entry.name.substringAfterLast('/').removeSuffix(kotlinModuleFileSuffix))
								moduleName to data
							}
						}

					file.isDirectory ->
						file.resolve("META-INF").listFiles().orEmpty()
							.filter { it.name.endsWith(kotlinModuleFileSuffix) }
							.asSequence()
							.map {
								val moduleName = MModuleName(it.name.removeSuffix(kotlinModuleFileSuffix))
								moduleName to it.readBytes()
							}

					else ->
						return emptySequence()
				}

				return dataSequences
					.mapNotNull { (name, data) -> KotlinModuleMetadata.read(data)?.let { name to it } }
					.map { (name, data) -> of(data, name = name) }
			}
		}


		public fun allModulesFromSameSourceAs(kotlinClass: KClass<*>): Sequence<MModule> =
			allModulesFromSameSourceAs(kotlinClass.java)


		public fun everythingFromSameSourceAs(javaClass: Class<*>): Sequence<Meta> =
			withExceptionWrapping {
				val baseDirectory = javaClass.protectionDomain.codeSource?.location?.toURI()?.let(::File)
					?: return emptySequence()

				when {
					baseDirectory.isFile -> JarFile(baseDirectory)
						.entries()
						.asSequence()
						.mapNotNull { entry ->
							when {
								entry.name.endsWith(classFileSuffix) ->
									runCatching { Class.forName(entry.name.removeSuffix(classFileSuffix).replace('/', '.')) }
										.getOrNull()
										?.let { of(it) }

								kotlinModuleJarEntryNamePattern.matches(entry.name) ->
									javaClass.getResourceAsStream("/${entry.name}")
										?.readBytes()
										?.let { KotlinModuleMetadata.read(it) }
										?.let { metadata ->
											val moduleName = MModuleName(entry.name.substringAfterLast('/').removeSuffix(kotlinModuleFileSuffix))
											of(metadata, name = moduleName)
										}

								else -> null
							}
						}

					baseDirectory.isDirectory ->
						baseDirectory.walkTopDown()
							.mapNotNull { file ->
								when {
									file.name.endsWith(classFileSuffix) ->
										runCatching {
											Class.forName(
												file.toRelativeString(baseDirectory)
													.removeSuffix(classFileSuffix)
													.replace('/', '.')
											)
										}
											.getOrNull()
											?.let { of(it) }

									file.name.endsWith(kotlinModuleFileSuffix) ->
										javaClass.getResourceAsStream("/${file.toRelativeString(baseDirectory)}")
											?.readBytes()
											?.let { KotlinModuleMetadata.read(it) }
											?.let { metadata ->
												val moduleName = MModuleName(file.name.removeSuffix(kotlinModuleFileSuffix))
												of(metadata, name = moduleName)
											}

									else -> null
								}
							}

					else ->
						return emptySequence()
				}
			}


		public fun everythingFromSameSourceAs(kotlinClass: KClass<*>): Sequence<Meta> =
			everythingFromSameSourceAs(kotlinClass.java)


		public fun of(javaClass: Class<*>): MType? =
			javaClass.getAnnotation(Metadata::class.java)?.let { of(it, className = MQualifiedTypeName.of(javaClass)) }


		public fun of(element: Element): MType? =
			element.getAnnotation(Metadata::class.java)?.let { of(it, className = MQualifiedTypeName.of(element)) }


		public fun of(kotlinClass: KClass<*>): MType? =
			of(kotlinClass.java)


		public fun of(metadata: Metadata, className: MQualifiedTypeName? = null): MType? =
			KotlinClassMetadata(metadata)?.let { of(it, className = className) }


		public fun of(metadata: KotlinClassMetadata, className: MQualifiedTypeName? = null): MType? =
			when (metadata) {
				is KotlinClassMetadata.Class -> of(metadata)
				is KotlinClassMetadata.FileFacade -> of(
					metadata,
					className = className ?: throw MetaException("a class name must be provided when reading metadata of a file facade")
				)
				is KotlinClassMetadata.SyntheticClass -> of(metadata)
				is KotlinClassMetadata.MultiFileClassFacade -> of(
					metadata,
					className = className ?: throw MetaException("a class name must be provided when reading metadata of a file facade")
				)
				is KotlinClassMetadata.MultiFileClassPart -> of(metadata)
				is KotlinClassMetadata.Unknown -> MUnknown
			}


		public fun of(metadata: KotlinClassMetadata.Class): MType =
			withExceptionWrapping {
				MClassBuilder()
					.also { metadata.accept(it) }
					.build()
			}


		public fun of(metadata: KotlinClassMetadata.FileFacade, className: MQualifiedTypeName): MFile =
			withExceptionWrapping {
				MFileBuilder(
					facadeClassName = className,
					kotlinPackageName = metadata.header.packageName.ifEmpty { null }?.let { MPackageName.fromKotlinInternal(it) }
				)
					.also { metadata.accept(it) }
					.build()
			}


		public fun of(metadata: KotlinModuleMetadata, name: MModuleName): MModule =
			withExceptionWrapping {
				MModuleBuilder(name = name)
					.also { metadata.accept(it) }
					.build()
			}


		public fun of(metadata: KotlinClassMetadata.MultiFileClassFacade, className: MQualifiedTypeName): MMultiFileClass =
			MMultiFileClass(
				className = className,
				partClassNames = metadata.partClassNames.map { MQualifiedTypeName.fromKotlinInternal(it) }
			)


		public fun of(metadata: KotlinClassMetadata.MultiFileClassPart): MMultiFileClassPart =
			withExceptionWrapping {
				val facadeClassName = MQualifiedTypeName.fromKotlinInternal(metadata.facadeClassName)

				MMultiFileClassPart(
					className = facadeClassName,
					file = MFileBuilder(
						facadeClassName = facadeClassName,
						kotlinPackageName = metadata.header.packageName.ifEmpty { null }?.let { MPackageName.fromKotlinInternal(it) }
					)
						.also { metadata.accept(it) }
						.build()
				)
			}


		public fun of(metadata: KotlinClassMetadata.SyntheticClass): MLambda? =
			if (metadata.isLambda)
				withExceptionWrapping {
					try {
						MLambdaBuilder()
							.also { metadata.accept(it) }
							.build()
					}
					catch (e: InconsistentKotlinMetadataException) {
						// TODO https://youtrack.jetbrains.com/issue/KT-29790
						if (e.message == "No VersionRequirement with the given id in the table")
							return null

						throw e
					}
				}
			else
				null


		public fun printAllElementsFromSameSourceAs(javaClass: Class<*>) {
			printAllElementsFromSameSourceAs(javaClass, destination = PrintWriter(System.out))
		}


		public fun printAllElementsFromSameSourceAs(kotlinClass: KClass<*>) {
			printAllElementsFromSameSourceAs(kotlinClass.java)
		}


		public fun printAllElementsFromSameSourceAs(javaClass: Class<*>, destination: Writer) {
			everythingFromSameSourceAs(javaClass)
				.sortedBy { meta ->
					when (meta) {
						is MModule -> 0
						is MClass -> 1
						is MObject -> 2
						is MInterface -> 3
						is MEnumClass -> 4
						is MEnumEntryClass -> 5
						is MAnnotationClass -> 6
						is MLambda -> 7
						is MFile -> 8
						is MMultiFileClass -> 9
						is MMultiFileClassPart -> 10
						else -> 11
					}
				}
				.forEach { meta ->
					destination.append(meta.toString())
					destination.append("\n// ------------------------------------------------------------------------------------------------------\n\n")
				}

			destination.flush()
		}


		public fun printAllElementsFromSameSourceAs(kotlinClass: KClass<*>, destination: Writer) {
			printAllElementsFromSameSourceAs(kotlinClass.java, destination = destination)
		}
	}
}


@Suppress("FunctionName")
private fun KotlinClassMetadata(metadata: Metadata) =
	withExceptionWrapping {
		KotlinClassMetadata.read(KotlinClassHeader(kind = metadata.kind,
			metadataVersion = metadata.metadataVersion,
			data1 = metadata.data1,
			data2 = metadata.data2,
			extraString = metadata.extraString,
			packageName = metadata.packageName,
			extraInt = metadata.extraInt))
	}


private inline fun <R> withExceptionWrapping(action: () -> R) =
	try {
		action()
	}
	catch (e: MetaException) {
		throw e
	}
	catch (e: Exception) {
		throw MetaException(e.message, e)
	}
