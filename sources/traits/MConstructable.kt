package com.github.fluidsonic.fluid.meta


interface MConstructable {

	val constructors: List<MConstructor>


	companion object
}


val MConstructable.primaryConstructor
	get() = constructors.firstOrNull { it.isPrimary }


val MConstructable.secondaryConstructors
	get() = constructors.filterNot { it.isPrimary }
