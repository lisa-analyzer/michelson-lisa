package it.unive.michelsonlisa.symblic.value.operator.binary;

import java.util.Set;

import it.unive.lisa.symbolic.value.operator.AdditionOperator;
import it.unive.lisa.symbolic.value.operator.binary.BinaryOperator;
import it.unive.lisa.type.NumericType;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.TypeSystem;

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
	public Set<Type> typeInference(TypeSystem types, Set<Type> left, Set<Type> right) {
		if (left.stream().noneMatch(Type::isNumericType) || right.stream().noneMatch(Type::isNumericType))
				return Set.of();
		Set<Type> set = NumericType.commonNumericalType(left, right);
		if (set.isEmpty())
			return Set.of();
		return set;
	}
}