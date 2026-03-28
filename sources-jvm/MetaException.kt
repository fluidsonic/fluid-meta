package io.fluidsonic.meta


/** Exception thrown when an error occurs while reading or processing Kotlin metadata. */
public class MetaException(message: String?, cause: Throwable? = null) : Exception(message, cause)
