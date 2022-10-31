package it.unive.michelsonlisa.symblic.value.operator.binary;

import it.unive.lisa.caches.Caches;
import it.unive.lisa.symbolic.value.BinaryExpression;
import it.unive.lisa.symbolic.value.operator.ComparisonOperator;
import it.unive.lisa.symbolic.value.operator.NegatableOperator;
import it.unive.lisa.symbolic.value.operator.binary.BinaryOperator;
import it.unive.lisa.type.Type;
import it.unive.lisa.util.collections.externalSet.ExternalSet;
import it.unive.michelsonlisa.cfg.type.MichelsonIntType;

/**
 * Given two expressions, a {@link BinaryExpression} using this operator checks
 * if the values those expressions compute to are different and compute a value.
 * First argument expression type: any {@link Type}<br>
 * Second argument expression type: any {@link Type}<br>
 * Computed expression type: {@link MichelsonIntType}
 * 
 * @author <a href="mailto:luca.olivieri@univr.it">Luca Olivieri</a>
 */
public class MichelsonComparison implements ComparisonOperator, BinaryOperator {

	/**
	 * The singleton instance of this class.
	 */
	public static final MichelsonComparison INSTANCE = new MichelsonComparison();

	private MichelsonComparison() {
	}

	@Override
	public String toString() {
		return "COMPARE";
	}

	@Override
	public ExternalSet<Type> typeInference(ExternalSet<Type> left, ExternalSet<Type> right) {
		return Caches.types().mkSingletonSet(MichelsonIntType.INSTANCE);
	}

	@Override
	public NegatableOperator opposite() {
		return null;
	}
}
