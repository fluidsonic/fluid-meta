package io.fluidsonic.meta


public interface MConstructable {

	public val constructors: List<MConstructor>


	public companion object
}


public val MConstructable.primaryConstructor: MConstructor?
	get() = constructors.firstOrNull { !it.isSecondary }


public val MConstructable.secondaryConstructors: List<MConstructor>
	get() = constructors.filter { it.isSecondary }
