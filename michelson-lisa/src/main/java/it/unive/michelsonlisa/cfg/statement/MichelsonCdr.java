package it.unive.michelsonlisa.cfg.statement;

import it.unive.lisa.analysis.AbstractState;
import it.unive.lisa.analysis.AnalysisState;
import it.unive.lisa.analysis.SemanticException;
import it.unive.lisa.analysis.StatementStore;
import it.unive.lisa.analysis.heap.HeapDomain;
import it.unive.lisa.analysis.value.TypeDomain;
import it.unive.lisa.analysis.value.ValueDomain;
import it.unive.lisa.caches.Caches;
import it.unive.lisa.interprocedural.InterproceduralAnalysis;
import it.unive.lisa.program.SourceCodeLocation;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.symbolic.value.UnaryExpression;
import it.unive.lisa.symbolic.value.operator.unary.UnaryOperator;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.Untyped;
import it.unive.lisa.util.collections.externalSet.ExternalSet;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackConsumer;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackProducer;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonPairCompType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonPairType;

public class MichelsonCdr extends it.unive.lisa.program.cfg.statement.UnaryExpression implements StackConsumer, StackProducer {

	final Type returnType; 
	public MichelsonCdr(CFG cfg, String sourceFile, int line, int col, Expression expression) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "CDR", computeType(expression.getStaticType()), expression);
		returnType = computeType(expression.getStaticType());
	}

	private static Type computeType(Type type) {

		if(type instanceof MichelsonPairCompType)
			return ((MichelsonPairCompType) type).getRightType();
		else if(type instanceof MichelsonPairType)
			return ((MichelsonPairType) type).getRightType();
		else if(type.equals(Untyped.INSTANCE))
			return Untyped.INSTANCE;
		else
			throw new IllegalArgumentException("The value of type "+type+" cannot used in CDR");
	}

	@Override
	protected <A extends AbstractState<A, H, V, T>, H extends HeapDomain<H>, V extends ValueDomain<V>, T extends TypeDomain<T>> AnalysisState<A, H, V, T> unarySemantics(
			InterproceduralAnalysis<A, H, V, T> interprocedural, AnalysisState<A, H, V, T> state,
			SymbolicExpression expr, StatementStore<A, H, V, T> expressions) throws SemanticException {
		
		UnaryOperator opt = new UnaryOperator() {

			@Override
			public ExternalSet<Type> typeInference(ExternalSet<Type> argument) {

				return Caches.types().mkSingletonSet(returnType);
			}
		};

		return state.smallStepSemantics(new UnaryExpression(returnType, expr, opt, getLocation()), this);
	}
}
