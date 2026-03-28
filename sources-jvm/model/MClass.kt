package io.fluidsonic.meta


/** Whether this class is a data class. */
public val MClass.isData: Boolean
	get() = specialization == MClass.Specialization.Data


/** Whether this class is an inner class. */
public val MClass.isInner: Boolean
	get() = specialization == MClass.Specialization.Inner


/** Whether this class is a sealed class. */
public val MClass.isSealed: Boolean
	get() = specialization is MClass.Specialization.Sealed
