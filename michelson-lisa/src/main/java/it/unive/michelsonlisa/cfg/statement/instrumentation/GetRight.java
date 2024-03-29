package it.unive.michelsonlisa.cfg.statement.instrumentation;

import it.unive.lisa.analysis.AbstractState;
import it.unive.lisa.analysis.AnalysisState;
import it.unive.lisa.analysis.SemanticException;
import it.unive.lisa.analysis.StatementStore;
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

public class GetRight extends UnaryExpression implements StackProducer, StackConsumer {

	public GetRight(CFG cfg, SourceCodeLocation srcLoc, VariableRef expr) {
		super(cfg, srcLoc, "GetRight", computeType(expr.getStaticType()) ,  expr);
	}

	private static Type computeType(Type type) {
		if(!(type instanceof MichelsonOrType || type instanceof MichelsonComparableType || type.equals(Untyped.INSTANCE) ))
			throw new IllegalArgumentException("The type" +type+ " is not supported by GetRight");
		if(type instanceof MichelsonOrType)
			return ((MichelsonOrType) type).getRightType();
		
		return Untyped.INSTANCE;
	}
	
	@Override
	public <A extends AbstractState<A>> AnalysisState<A> fwdUnarySemantics(InterproceduralAnalysis<A> interprocedural,
			AnalysisState<A> state, SymbolicExpression expr, StatementStore<A> expressions) throws SemanticException {
		return state.smallStepSemantics(expr, this);
	}

}