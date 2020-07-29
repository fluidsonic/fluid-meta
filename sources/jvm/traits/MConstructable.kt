package io.fluidsonic.meta


public interface MConstructable {

	public val constructors: List<MConstructor>


	public companion object
}


public val MConstructable.primaryConstructor: MConstructor?
	get() = constructors.firstOrNull { it.isPrimary }


public val MConstructable.secondaryConstructors: List<MConstructor>
	get() = constructors.filterNot { it.isPrimary }
