package it.unive.michelsonlisa.cfg.statement;

import java.util.Set;

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
import it.unive.michelsonlisa.cfg.type.MichelsonUnitType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonContractType;

public class MichelsonImplicit_account extends it.unive.lisa.program.cfg.statement.UnaryExpression implements StackConsumer, StackProducer {

	public MichelsonImplicit_account(CFG cfg, String sourceFile, int line, int col, Expression expression) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "IMPLICIT_ACCOUNT", new MichelsonContractType(MichelsonUnitType.INSTANCE) ,expression);
		checkParameterType(expression.getStaticType());	
	}

	private void checkParameterType(Type type) {
		if(!(type instanceof MichelsonKeyHashType || type.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The value of type "+type+" cannot used in IMPLICIT_ACCOUNT");
	}
	
	@Override
	public <A extends AbstractState<A, H, V, T>, H extends HeapDomain<H>, V extends ValueDomain<V>, T extends TypeDomain<T>> AnalysisState<A, H, V, T> unarySemantics(
			InterproceduralAnalysis<A, H, V, T> interprocedural, AnalysisState<A, H, V, T> state,
			SymbolicExpression expr, StatementStore<A, H, V, T> expressions) throws SemanticException {
		UnaryOperator opt = new UnaryOperator() {

			@Override
			public Set<Type> typeInference(TypeSystem types, Set<Type> argument) {
				return Set.of(MichelsonUnitType.INSTANCE);
			}
		};
		return state.smallStepSemantics(new UnaryExpression(MichelsonUnitType.INSTANCE, expr, opt, getLocation()), this);
	}

}
