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
import it.unive.michelsonlisa.cfg.expression.literal.MichelsonNaturalData;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackConsumer;

public class MichelsonDrop extends NaryExpression implements StackConsumer {

	int n;
	
	public MichelsonDrop(CFG cfg, String sourceFile, int line, int col) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "DROP");
		n = 1;
	}

	public MichelsonDrop(CFG cfg, String sourceFile, int line, int col, MichelsonNaturalData nat) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "DROP", nat);
		n = nat.getValue().intValueExact();
	}


	@Override
	public <A extends AbstractState<A, H, V, T>, H extends HeapDomain<H>, V extends ValueDomain<V>, T extends TypeDomain<T>> AnalysisState<A, H, V, T> expressionSemantics(
			InterproceduralAnalysis<A, H, V, T> interprocedural, AnalysisState<A, H, V, T> state,
			ExpressionSet<SymbolicExpression>[] params, StatementStore<A, H, V, T> expressions)
			throws SemanticException {
		return state;
	}
	
	@Override
	public int getNumberOfStackElementsConsumed(){
		return n;
	}
}
