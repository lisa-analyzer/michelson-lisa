package it.unive.michelsonlisa.cfg.statement;

import it.unive.lisa.analysis.AbstractState;
import it.unive.lisa.analysis.AnalysisState;
import it.unive.lisa.analysis.SemanticException;
import it.unive.lisa.analysis.StatementStore;
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
import it.unive.michelsonlisa.cfg.type.composite.MichelsonListType;

public class MichelsonCons extends BinaryExpression implements StackConsumer, StackProducer {

	public MichelsonCons(CFG cfg, String sourceFile, int line, int col, Expression left, Expression right) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "CONS", computeType(left.getStaticType(), right.getStaticType()), left, right);
	}
	
	private static Type computeType(Type t1, Type t2) {
		
		
		if(t2.equals(Untyped.INSTANCE)) {
			if(t1.equals(Untyped.INSTANCE))
				return new MichelsonListType(t1);
			else 
				return Untyped.INSTANCE;
		} if(t2 instanceof MichelsonListType) {
			Type lType = ((MichelsonListType) t2 ).getContentType();
			if(!(lType.equals(t1))){
				if(lType.equals(Untyped.INSTANCE))
					return new MichelsonListType(t1);
				else if(t1.equals(Untyped.INSTANCE))
					return t2;
				else
					throw new IllegalArgumentException("The mismatch between type "+t1+" and the list type "+ lType);
			} else
				return t2;
		} else 
			throw new IllegalArgumentException("The value of type "+t2+" cannot used in CONS");

	}

	@Override
	public <A extends AbstractState<A>> AnalysisState<A> fwdBinarySemantics(InterproceduralAnalysis<A> interprocedural,
			AnalysisState<A> state, SymbolicExpression left, SymbolicExpression right, StatementStore<A> expressions)
			throws SemanticException {
		AnalysisState<A> result = state.bottom();
		result = state.smallStepSemantics(left, this);
		result = result.lub(state.smallStepSemantics(right, this));
		return result;
	}


}
