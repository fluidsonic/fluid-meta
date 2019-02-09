package com.github.fluidsonic.fluid.meta


val MClass.isData
	get() = specialization == MClass.Specialization.Data


val MClass.isInner
	get() = specialization == MClass.Specialization.Inner


val MClass.isSealed
	get() = specialization is MClass.Specialization.Sealed
