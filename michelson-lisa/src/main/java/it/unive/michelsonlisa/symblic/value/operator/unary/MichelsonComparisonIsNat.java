package it.unive.michelsonlisa.symblic.value.operator.unary;

import java.util.Set;

import it.unive.lisa.symbolic.value.operator.ComparisonOperator;
import it.unive.lisa.symbolic.value.operator.unary.UnaryOperator;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.TypeSystem;

/**
 */
public class MichelsonComparisonIsNat implements ComparisonOperator, UnaryOperator {

	/**
	 * The singleton instance of this class.
	 */
	public static final MichelsonComparisonIsNat INSTANCE = new MichelsonComparisonIsNat();

	private MichelsonComparisonIsNat() {
	}

	@Override
	public String toString() {
		return "ISNAT";
	}

	@Override
	public ComparisonOperator opposite() {
		return MichelsonComparisonGt.INSTANCE;
	}

	@Override
	public Set<Type> typeInference(TypeSystem types, Set<Type> argument) {
		return Set.of(types.getBooleanType());
	}
}
