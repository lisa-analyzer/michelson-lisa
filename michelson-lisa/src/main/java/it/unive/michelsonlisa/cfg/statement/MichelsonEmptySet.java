package it.unive.michelsonlisa.cfg.statement;

import it.unive.lisa.analysis.AbstractState;
import it.unive.lisa.analysis.AnalysisState;
import it.unive.lisa.analysis.SemanticException;
import it.unive.lisa.analysis.StatementStore;
import it.unive.lisa.analysis.heap.HeapDomain;
import it.unive.lisa.analysis.lattices.ExpressionSet;
import it.unive.lisa.analysis.value.TypeDomain;
import it.unive.lisa.analysis.value.ValueDomain;
import it.unive.lisa.interprocedural.InterproceduralAnalysis;
import it.unive.lisa.program.SourceCodeLocation;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.statement.NaryExpression;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.symbolic.value.Constant;
import it.unive.lisa.type.Type;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackProducer;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonSetType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonComparableType;

public class MichelsonEmptySet extends NaryExpression implements StackProducer{

	public MichelsonEmptySet(CFG cfg, String sourceFile, int line, int col, Type type) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "EMPTY_SET", new MichelsonSetType(computeType(type)));
	}

	private static MichelsonComparableType computeType(Type type) {
		if(type instanceof MichelsonComparableType)
			return (MichelsonComparableType) type;
		else
			throw new IllegalArgumentException("The type "+type+" cannot used in the set because it must be comparable");
	}

	@Override
	public <A extends AbstractState<A, H, V, T>, H extends HeapDomain<H>, V extends ValueDomain<V>, T extends TypeDomain<T>> AnalysisState<A, H, V, T> expressionSemantics(
			InterproceduralAnalysis<A, H, V, T> interprocedural, AnalysisState<A, H, V, T> state,
			ExpressionSet<SymbolicExpression>[] params, StatementStore<A, H, V, T> expressions)
			throws SemanticException {
		return state.smallStepSemantics(new Constant(getStaticType(), "EMPTY_SET_VALUE", getLocation()), this);
	}


}
