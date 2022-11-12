package it.unive.michelsonlisa.symblic.value.operator.unary;

import java.util.Set;

import it.unive.lisa.symbolic.value.operator.ComparisonOperator;
import it.unive.lisa.symbolic.value.operator.unary.UnaryOperator;
import it.unive.lisa.type.BooleanType;
import it.unive.lisa.type.NumericType;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.TypeSystem;

/**
 * Given an expression that both evaluate to numeric values, a
 * {@link UnaryExpression} using this operator checks if the result 
 * is less or equal.<br>
 * <br>
 * Argument expression type: {@link NumericType}<br>
 * Computed expression type: {@link BooleanType}
 * 
 * @author <a href="mailto:luca.olivieri@univr.it">Luca Olivieri</a>
 */
public class MichelsonComparisonLt implements ComparisonOperator, UnaryOperator {

	/**
	 * The singleton instance of this class.
	 */
	public static final MichelsonComparisonLt INSTANCE = new MichelsonComparisonLt();

	private MichelsonComparisonLt() {
	}

	@Override
	public String toString() {
		return "<";
	}

	@Override
	public ComparisonOperator opposite() {
		return MichelsonComparisonGe.INSTANCE;
	}

	@Override
	public Set<Type> typeInference(TypeSystem types, Set<Type> argument) {
		return Set.of(types.getBooleanType());
	}
}
