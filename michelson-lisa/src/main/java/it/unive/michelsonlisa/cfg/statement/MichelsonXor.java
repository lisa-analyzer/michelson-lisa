package it.unive.michelsonlisa.cfg.statement;

import it.unive.lisa.analysis.AbstractState;
import it.unive.lisa.analysis.AnalysisState;
import it.unive.lisa.analysis.SemanticException;
import it.unive.lisa.analysis.StatementStore;
import it.unive.lisa.analysis.heap.HeapDomain;
import it.unive.lisa.analysis.value.TypeDomain;
import it.unive.lisa.analysis.value.ValueDomain;
import it.unive.lisa.interprocedural.InterproceduralAnalysis;
import it.unive.lisa.program.SourceCodeLocation;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.statement.BinaryExpression;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.symbolic.value.PushAny;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.Untyped;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackConsumer;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackProducer;
import it.unive.michelsonlisa.cfg.type.MichelsonBoolType;
import it.unive.michelsonlisa.cfg.type.MichelsonNatType;

public class MichelsonXor extends BinaryExpression implements StackConsumer, StackProducer, MichelsonBinaryNumericalOperation {

	public MichelsonXor(CFG cfg, String sourceFile, int line, int col, Expression left, Expression right) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "XOR", computeType(left.getStaticType(), right.getStaticType()), left ,right);
	}

	private static Type computeType(Type type1, Type type2) {
		
		if(!(type1 instanceof MichelsonNatType || type1 instanceof MichelsonBoolType || type1.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The value of type "+type1+" cannot used in XOR");
		
		if(!(type2 instanceof MichelsonNatType || type2 instanceof MichelsonBoolType || type2.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The value of type "+type1+" cannot used in XOR");
		
		if(type1.equals(Untyped.INSTANCE))
			return type2;

		return type1;
	}
	
	@Override
	protected <A extends AbstractState<A, H, V, T>, H extends HeapDomain<H>, V extends ValueDomain<V>, T extends TypeDomain<T>> AnalysisState<A, H, V, T> binarySemantics(
			InterproceduralAnalysis<A, H, V, T> interprocedural, AnalysisState<A, H, V, T> state,
			SymbolicExpression left, SymbolicExpression right, StatementStore<A, H, V, T> expressions)
			throws SemanticException {
		AnalysisState<A, H, V, T> result = state.top();
		for (Type leftType : left.getRuntimeTypes())
			for (Type rightType : right.getRuntimeTypes())
				if ((leftType.isUntyped() || (leftType.isNumericType() && leftType.asNumericType().isIntegral())) &&
						(rightType.isUntyped()
								|| (rightType.isNumericType() && rightType.asNumericType().isIntegral()))) {
					// TODO: LiSA has not symbolic expression handling bitwise,
					// return top at the moment
					AnalysisState<A, H, V, T> tmp = state
							.smallStepSemantics(new PushAny(resultType(leftType, rightType), getLocation()), this);
					result = result.lub(tmp);
				}
		return result;
	}
}
