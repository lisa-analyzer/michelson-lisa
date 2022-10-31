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
import it.unive.michelsonlisa.cfg.type.MichelsonNatType;

public class MichelsonLsl extends BinaryExpression implements StackConsumer, StackProducer {

	public MichelsonLsl(CFG cfg, String sourceFile, int line, int col, Expression e1, Expression e2) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "LSL", MichelsonNatType.INSTANCE, e1, e2);
		checkParameterType(e1.getStaticType(), e2.getStaticType());	
	}

	private void checkParameterType(Type type1, Type type2) {
		if(!(type1 instanceof MichelsonNatType || type1.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The value of type "+type1+" cannot used in LSL");
		
		if(!(type2 instanceof MichelsonNatType || type2.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The value of type "+type2+" cannot used in LSL");
	}

	@Override
	protected <A extends AbstractState<A, H, V, T>, H extends HeapDomain<H>, V extends ValueDomain<V>, T extends TypeDomain<T>> AnalysisState<A, H, V, T> binarySemantics(
			InterproceduralAnalysis<A, H, V, T> interprocedural, AnalysisState<A, H, V, T> state,
			SymbolicExpression left, SymbolicExpression right, StatementStore<A, H, V, T> expressions)
			throws SemanticException {
		return state;
	}


}
