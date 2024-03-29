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
import it.unive.michelsonlisa.cfg.type.composite.MichelsonBigMapType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonListType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonMapType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonPairType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonSetType;

public class GetNextElementFromIterableStructure extends UnaryExpression implements StackProducer, StackConsumer {

	public GetNextElementFromIterableStructure(CFG cfg, SourceCodeLocation srcLoc, VariableRef expr) {
		super(cfg, srcLoc, "ExtractValueFromOption", computeType(expr.getStaticType()), expr);
	}

	private static Type computeType(Type t) {
		if(t instanceof MichelsonListType)
			return ((MichelsonListType) t).getContentType();
		else if(t instanceof MichelsonSetType)
			return ((MichelsonSetType) t).getContentType();
		else if(t instanceof MichelsonMapType)
			return new MichelsonPairType(((MichelsonMapType) t).getKeyType(), ((MichelsonMapType) t).getElementType());
		else if(t instanceof MichelsonBigMapType)
			return new MichelsonPairType(((MichelsonBigMapType) t).getKeyType(), ((MichelsonBigMapType) t).getElementType());
		return Untyped.INSTANCE;
	}

	@Override
	public <A extends AbstractState<A>> AnalysisState<A> fwdUnarySemantics(InterproceduralAnalysis<A> interprocedural,
			AnalysisState<A> state, SymbolicExpression expr, StatementStore<A> expressions) throws SemanticException {
		return state.smallStepSemantics(expr, this);
	}

}