package it.unive.michelsonlisa.symblic.value.operator.unary;

import it.unive.lisa.caches.Caches;
import it.unive.lisa.symbolic.value.operator.ComparisonOperator;
import it.unive.lisa.symbolic.value.operator.unary.UnaryOperator;
import it.unive.lisa.type.BooleanType;
import it.unive.lisa.type.NumericType;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.common.BoolType;
import it.unive.lisa.util.collections.externalSet.ExternalSet;

/**
 * Given an expression that both evaluate to numeric values, a
 * {@link UnaryExpression} using this operator checks if the result 
 * is greater or equal.<br>
 * <br>
 * Argument expression type: {@link NumericType}<br>
 * Computed expression type: {@link BooleanType}
 * 
 * @author <a href="mailto:luca.negrini@unive.it">Luca Olivieri</a>
 */
public class MichelsonComparisonGe implements ComparisonOperator, UnaryOperator {

	/**
	 * The singleton instance of this class.
	 */
	public static final MichelsonComparisonGe INSTANCE = new MichelsonComparisonGe();

	private MichelsonComparisonGe() {
	}

	@Override
	public String toString() {
		return ">=";
	}

	@Override
	public ComparisonOperator opposite() {
		return MichelsonComparisonLt.INSTANCE;
	}

	@Override
	public ExternalSet<Type> typeInference(ExternalSet<Type> argument) {
		if (argument.noneMatch(Type::isNumericType))
			return Caches.types().mkEmptySet();
		return Caches.types().mkSingletonSet(BoolType.INSTANCE);
	}
}
