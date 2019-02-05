package com.github.fluidsonic.fluid.meta

import kotlinx.metadata.Flags
import kotlinx.metadata.KmEffectExpressionVisitor


internal class MEffectExpressionBuilder : KmEffectExpressionVisitor() {

	private var andArguments: MutableList<MEffectExpressionBuilder>? = null
	private var constantValue: Any? = null
	private var flags: Flags = 0
	private var instanceType: MTypeReferenceBuilder? = null
	private var orArguments: MutableList<MEffectExpressionBuilder>? = null
	private var parameterIndex: MValueParameterIndex? = null


	fun build(): MEffectExpression = MEffectExpression(
		andArguments = andArguments.mapOrEmpty { it.build() },
		constantValue = constantValue,
		flags = flags,
		instanceType = instanceType?.build(),
		orArguments = orArguments.mapOrEmpty { it.build() },
		parameterIndex = parameterIndex
	)


	override fun visit(flags: Flags, parameterIndex: Int?) {
		this.flags = flags
		this.parameterIndex = parameterIndex?.let(::MValueParameterIndex)
	}


	override fun visitAndArgument() =
		MEffectExpressionBuilder().also {
			andArguments?.apply { add(it) }
				?: { andArguments = mutableListOf(it) }()
		}


	override fun visitConstantValue(value: Any?) {
		this.constantValue = value
	}


	override fun visitIsInstanceType(flags: Flags) =
		MTypeReferenceBuilder(flags = flags)
			.also { instanceType = it }


	override fun visitOrArgument() =
		MEffectExpressionBuilder().also {
			orArguments?.apply { add(it) }
				?: { orArguments = mutableListOf(it) }()
		}
}
