package io.fluidsonic.meta


public val MClass.isData: Boolean
	get() = specialization == MClass.Specialization.Data


public val MClass.isInner: Boolean
	get() = specialization == MClass.Specialization.Inner


public val MClass.isSealed: Boolean
	get() = specialization is MClass.Specialization.Sealed
