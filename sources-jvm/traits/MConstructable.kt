package io.fluidsonic.meta


/** A declaration that has constructors. */
public interface MConstructable {

	/** The constructors of this declaration. */
	public val constructors: List<MConstructor>


	public companion object
}


/** The primary constructor, or `null` if there is none. */
public val MConstructable.primaryConstructor: MConstructor?
	get() = constructors.firstOrNull { !it.isSecondary }


/** The secondary constructors of this declaration. */
public val MConstructable.secondaryConstructors: List<MConstructor>
	get() = constructors.filter { it.isSecondary }
