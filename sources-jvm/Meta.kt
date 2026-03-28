@file:OptIn(UnstableMetadataApi::class)

package io.fluidsonic.meta

import kotlin.metadata.*
import kotlin.metadata.jvm.*
import java.io.*
import java.util.jar.*
import javax.lang.model.element.*
import kotlin.reflect.*


/** Entry point for reading Kotlin metadata from compiled classes, modules, and JAR files. */
// FIXME improve API
public interface Meta {

	public companion object {

		private const val classFileSuffix = ".class"
		private const val kotlinModuleFileSuffix = ".kotlin_module"
		private val kotlinModuleJarEntryNamePattern = Regex("^META-INF/[^/]*" + Regex.escape(kotlinModuleFileSuffix))


		/** Returns all Kotlin modules found in the same source (JAR or directory) as the given [javaClass]. */
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
					.map { (name, data) -> name to KotlinModuleMetadata.read(data) }
					.map { (name, data) -> of(data, name = name) }
			}
		}


		/** Returns all Kotlin modules found in the same source (JAR or directory) as the given [kotlinClass]. */
		public fun allModulesFromSameSourceAs(kotlinClass: KClass<*>): Sequence<MModule> =
			allModulesFromSameSourceAs(kotlinClass.java)


		/** Returns all metadata elements (types and modules) found in the same source (JAR or directory) as the given [javaClass]. */
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


		/** Returns all metadata elements (types and modules) found in the same source (JAR or directory) as the given [kotlinClass]. */
		public fun everythingFromSameSourceAs(kotlinClass: KClass<*>): Sequence<Meta> =
			everythingFromSameSourceAs(kotlinClass.java)


		/** Reads Kotlin metadata from a Java class and returns the corresponding [MType], or `null` if no metadata is found. */
		public fun of(javaClass: Class<*>): MType? =
			javaClass.getAnnotation(Metadata::class.java)?.let { of(it, className = MQualifiedTypeName.of(javaClass)) }


		/** Reads Kotlin metadata from an annotation processing [Element] and returns the corresponding [MType], or `null` if no metadata is found. */
		public fun of(element: Element): MType? =
			element.getAnnotation(Metadata::class.java)?.let { of(it, className = MQualifiedTypeName.of(element)) }


		/** Reads Kotlin metadata from a Kotlin class and returns the corresponding [MType], or `null` if no metadata is found. */
		public fun of(kotlinClass: KClass<*>): MType? =
			of(kotlinClass.java)


		/** Parses the given [Metadata] annotation and returns the corresponding [MType], or `null` if not supported. */
		public fun of(metadata: Metadata, className: MQualifiedTypeName? = null): MType? =
			withExceptionWrapping {
				val classMetadata = KotlinClassMetadata.readLenient(metadata)
				of(classMetadata, className = className, packageName = metadata.packageName)
			}


		/** Converts [KotlinClassMetadata] into the corresponding [MType], or `null` if not supported. */
		public fun of(
			metadata: KotlinClassMetadata,
			className: MQualifiedTypeName? = null,
			packageName: String = "",
		): MType? =
			when (metadata) {
				is KotlinClassMetadata.Class -> of(metadata)
				is KotlinClassMetadata.FileFacade -> of(
					metadata,
					className = className ?: throw MetaException("a class name must be provided when reading metadata of a file facade"),
					packageName = packageName,
				)

				is KotlinClassMetadata.SyntheticClass -> of(metadata)
				is KotlinClassMetadata.MultiFileClassFacade -> of(
					metadata,
					className = className ?: throw MetaException("a class name must be provided when reading metadata of a file facade"),
				)

				is KotlinClassMetadata.MultiFileClassPart -> of(metadata, packageName = packageName)
				is KotlinClassMetadata.Unknown -> MUnknown
			}


		/** Converts class metadata into the corresponding [MType]. */
		public fun of(metadata: KotlinClassMetadata.Class): MType =
			withExceptionWrapping { metadata.kmClass.toMeta() }


		/** Converts file facade metadata into an [MFile]. */
		public fun of(
			metadata: KotlinClassMetadata.FileFacade,
			className: MQualifiedTypeName,
			packageName: String = "",
		): MFile =
			withExceptionWrapping {
				metadata.kmPackage.toMeta(
					facadeClassName = className,
					kotlinPackageName = packageName.ifEmpty { null }?.let { MPackageName.fromKotlinInternal(it) },
				)
			}


		/** Converts module metadata into an [MModule]. */
		public fun of(metadata: KotlinModuleMetadata, name: MModuleName): MModule =
			withExceptionWrapping { metadata.kmModule.toMeta(name) }


		/** Converts multi-file class facade metadata into an [MMultiFileClass]. */
		public fun of(metadata: KotlinClassMetadata.MultiFileClassFacade, className: MQualifiedTypeName): MMultiFileClass =
			MMultiFileClass(
				className = className,
				partClassNames = metadata.partClassNames.map { MQualifiedTypeName.fromKotlinInternal(it) },
			)


		/** Converts multi-file class part metadata into an [MMultiFileClassPart]. */
		public fun of(metadata: KotlinClassMetadata.MultiFileClassPart, packageName: String = ""): MMultiFileClassPart =
			withExceptionWrapping {
				val facadeClassName = MQualifiedTypeName.fromKotlinInternal(metadata.facadeClassName)

				MMultiFileClassPart(
					className = facadeClassName,
					file = metadata.kmPackage.toMeta(
						facadeClassName = facadeClassName,
						kotlinPackageName = packageName.ifEmpty { null }?.let { MPackageName.fromKotlinInternal(it) },
					),
				)
			}


		/** Converts synthetic class metadata into an [MLambda], or `null` if the synthetic class is not a lambda. */
		public fun of(metadata: KotlinClassMetadata.SyntheticClass): MLambda? =
			if (metadata.isLambda)
				withExceptionWrapping { metadata.kmLambda?.toMeta() }
			else
				null


		/** Prints all metadata elements from the same source as [javaClass] to standard output. */
		public fun printAllElementsFromSameSourceAs(javaClass: Class<*>) {
			printAllElementsFromSameSourceAs(javaClass, destination = PrintWriter(System.out))
		}


		/** Prints all metadata elements from the same source as [kotlinClass] to standard output. */
		public fun printAllElementsFromSameSourceAs(kotlinClass: KClass<*>) {
			printAllElementsFromSameSourceAs(kotlinClass.java)
		}


		/** Prints all metadata elements from the same source as [javaClass] to the given [destination]. */
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


		/** Prints all metadata elements from the same source as [kotlinClass] to the given [destination]. */
		public fun printAllElementsFromSameSourceAs(kotlinClass: KClass<*>, destination: Writer) {
			printAllElementsFromSameSourceAs(kotlinClass.java, destination = destination)
		}
	}
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
