package io.fluidsonic.meta


/** A declaration that contains references to file-level types. */
public interface MFileTypeContainer {

	/** The qualified names of types declared at file level. */
	public val fileTypes: List<MQualifiedTypeName>


	public companion object
}
