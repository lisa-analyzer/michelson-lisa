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
import it.unive.lisa.program.cfg.statement.VariableRef;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.Untyped;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackConsumer;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackProducer;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonLambdaType;

public class MichelsonExec extends BinaryExpression implements StackConsumer, StackProducer{

	public MichelsonExec(CFG cfg, String sourceFile, int line, int col, Expression expression, VariableRef expr2) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "EXEC", computeType(expr2.getStaticType()), expression, expr2);
	}

	private static Type computeType(Type type) {
		if(!(type instanceof MichelsonLambdaType || type.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The value of type "+type+" cannot used in EXEC");
		if(type instanceof MichelsonLambdaType)
			return ((MichelsonLambdaType) type).getReturnType();
		return Untyped.INSTANCE;
	}

	@Override
	public <A extends AbstractState<A>> AnalysisState<A> fwdBinarySemantics(InterproceduralAnalysis<A> interprocedural,
			AnalysisState<A> state, SymbolicExpression left, SymbolicExpression right, StatementStore<A> expressions)
			throws SemanticException {
		return state;
	}
}
