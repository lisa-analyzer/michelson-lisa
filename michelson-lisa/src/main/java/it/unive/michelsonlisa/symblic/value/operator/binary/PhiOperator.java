package it.unive.michelsonlisa.symblic.value.operator.binary;

import it.unive.lisa.caches.Caches;
import it.unive.lisa.symbolic.value.operator.AdditionOperator;
import it.unive.lisa.symbolic.value.operator.binary.BinaryOperator;
import it.unive.lisa.type.NumericType;
import it.unive.lisa.type.Type;
import it.unive.lisa.util.collections.externalSet.ExternalSet;

public class PhiOperator implements AdditionOperator, BinaryOperator {

	/**
	 * The singleton instance of this class.
	 */
	public static final PhiOperator INSTANCE = new PhiOperator();

	private PhiOperator() {
	}

	@Override
	public String toString() {
		return "\u03D5";
	}

	@Override
	public ExternalSet<Type> typeInference(ExternalSet<Type> left, ExternalSet<Type> right) {
		if (left.noneMatch(Type::isNumericType) || right.noneMatch(Type::isNumericType))
			return Caches.types().mkEmptySet();
		ExternalSet<Type> set = NumericType.commonNumericalType(left, right);
		if (set.isEmpty())
			return Caches.types().mkEmptySet();
		return set;
	}
}