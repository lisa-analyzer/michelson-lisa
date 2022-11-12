package it.unive.michelsonlisa.cfg.statement.instrumentation;

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
import it.unive.lisa.program.cfg.statement.UnaryExpression;
import it.unive.lisa.program.cfg.statement.VariableRef;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.Untyped;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackConsumer;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackProducer;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonOrType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonComparableType;

public class GetLeft extends UnaryExpression implements StackProducer, StackConsumer {

	public GetLeft(CFG cfg, SourceCodeLocation srcLoc, VariableRef expr) {
		super(cfg, srcLoc, "GetLeft", computeType(expr.getStaticType()), expr);
	}


	private static Type computeType(Type type) {
		if(!(type instanceof MichelsonOrType || type instanceof MichelsonComparableType || type.equals(Untyped.INSTANCE) ))
			throw new IllegalArgumentException("The type" +type+ " is not supported by GetLeft");
		if(type instanceof MichelsonOrType)
			return ((MichelsonOrType) type).getLeftType();
		
		return Untyped.INSTANCE;
	}
	
	@Override
	public <A extends AbstractState<A, H, V, T>, H extends HeapDomain<H>, V extends ValueDomain<V>, T extends TypeDomain<T>> AnalysisState<A, H, V, T> unarySemantics(
			InterproceduralAnalysis<A, H, V, T> interprocedural, AnalysisState<A, H, V, T> state,
			SymbolicExpression expr, StatementStore<A, H, V, T> expressions) throws SemanticException {

		return state.smallStepSemantics(expr, this);
	}

}