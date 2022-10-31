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
import it.unive.lisa.type.Type;
import it.unive.lisa.type.Untyped;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackConsumer;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackProducer;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonLambdaType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonPairType;

public class MichelsonApply extends BinaryExpression implements StackConsumer, StackProducer{

	public MichelsonApply(CFG cfg, String sourceFile, int line, int col, Expression left, Expression right) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "APPLY", computeType(left.getStaticType(), right.getStaticType()), left, right);
	}

	private static Type computeType(Type left, Type right) {

		if(!(left instanceof MichelsonLambdaType || left.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The left value of type "+left+" cannot used in APPLY");
		
		Type parType = Untyped.INSTANCE;
		if(left instanceof MichelsonLambdaType) {
			MichelsonLambdaType lambdaType = ((MichelsonLambdaType) left);
			if(lambdaType.getParameterType() instanceof MichelsonPairType)
				parType = ((MichelsonPairType) lambdaType.getParameterType()).getRightType();
			else if(!lambdaType.getParameterType().equals(Untyped.INSTANCE))
				throw new IllegalArgumentException("The value of type "+left+" cannot used in the lambda of APPLY");
		}
		return new MichelsonLambdaType(parType, right);
	}
	
	@Override
	protected <A extends AbstractState<A, H, V, T>, H extends HeapDomain<H>, V extends ValueDomain<V>, T extends TypeDomain<T>> AnalysisState<A, H, V, T> binarySemantics(
			InterproceduralAnalysis<A, H, V, T> interprocedural, AnalysisState<A, H, V, T> state,
			SymbolicExpression left, SymbolicExpression right, StatementStore<A, H, V, T> expressions)
			throws SemanticException {
		// TODO Auto-generated method stub
		return state;
	}
}
