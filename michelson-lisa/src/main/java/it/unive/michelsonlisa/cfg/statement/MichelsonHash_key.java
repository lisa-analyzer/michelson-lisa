package it.unive.michelsonlisa.cfg.statement;

import java.util.Set;

import it.unive.lisa.analysis.AbstractState;
import it.unive.lisa.analysis.AnalysisState;
import it.unive.lisa.analysis.SemanticException;
import it.unive.lisa.analysis.StatementStore;
import it.unive.lisa.interprocedural.InterproceduralAnalysis;
import it.unive.lisa.program.SourceCodeLocation;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.symbolic.value.UnaryExpression;
import it.unive.lisa.symbolic.value.operator.unary.UnaryOperator;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.TypeSystem;
import it.unive.lisa.type.Untyped;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackConsumer;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackProducer;
import it.unive.michelsonlisa.cfg.type.MichelsonKeyHashType;
import it.unive.michelsonlisa.cfg.type.MichelsonKeyType;

public class MichelsonHash_key extends it.unive.lisa.program.cfg.statement.UnaryExpression implements StackConsumer, StackProducer {

	public MichelsonHash_key(CFG cfg, String sourceFile, int line, int col, Expression expression) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "HASH_KEY", MichelsonKeyHashType.INSTANCE, expression);
		checkParameterType(expression.getStaticType());	
	}

	private void checkParameterType(Type type) {
		if(!(type instanceof MichelsonKeyType || type.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The value of type "+type+" cannot used in HASH_KEY");
	}

	@Override
	public <A extends AbstractState<A>> AnalysisState<A> fwdUnarySemantics(InterproceduralAnalysis<A> interprocedural,
			AnalysisState<A> state, SymbolicExpression expr, StatementStore<A> expressions) throws SemanticException {
		UnaryOperator opt = new UnaryOperator() {

			@Override
			public Set<Type> typeInference(TypeSystem types, Set<Type> argument) {
				return Set.of(MichelsonKeyHashType.INSTANCE);
			}
		};
		return state.smallStepSemantics(new UnaryExpression(MichelsonKeyHashType.INSTANCE, expr, opt, getLocation()), this);
	
	}
}
