package it.unive.michelsonlisa.symblic.value.operator.unary;

import it.unive.lisa.caches.Caches;
import it.unive.lisa.symbolic.value.operator.ComparisonOperator;
import it.unive.lisa.symbolic.value.operator.unary.UnaryOperator;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.common.BoolType;
import it.unive.lisa.util.collections.externalSet.ExternalSet;

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
	public ExternalSet<Type> typeInference(ExternalSet<Type> argument) {
		if (argument.noneMatch(Type::isNumericType))
			return Caches.types().mkEmptySet();
		return Caches.types().mkSingletonSet(BoolType.INSTANCE);
	}
}
