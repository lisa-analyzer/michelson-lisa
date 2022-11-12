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
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.program.cfg.statement.NaryExpression;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackConsumer;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackProducer;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonPairType;
import it.unive.michelsonlisa.utils.MichelsonTypeUtils;

public class MichelsonPair extends NaryExpression implements StackProducer, StackConsumer {

	public MichelsonPair(CFG cfg, String sourceFile, int line, int col, Expression left, Expression right) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "PAIR", new MichelsonPairType(MichelsonTypeUtils.getTypes(new Expression[]{left, right})), left, right);
	}

	public MichelsonPair(CFG cfg, String sourceFile, int line, int col, Expression ...exprs) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "PAIR", new MichelsonPairType(MichelsonTypeUtils.getTypes(exprs)), exprs);
	}

	@Override
	public <A extends AbstractState<A, H, V, T>, H extends HeapDomain<H>, V extends ValueDomain<V>, T extends TypeDomain<T>> AnalysisState<A, H, V, T> expressionSemantics(
			InterproceduralAnalysis<A, H, V, T> interprocedural, AnalysisState<A, H, V, T> state,
			ExpressionSet<SymbolicExpression>[] params, StatementStore<A, H, V, T> expressions)
			throws SemanticException {
		AnalysisState<A, H, V, T> result = state.bottom();
		for (ExpressionSet<SymbolicExpression> exprs : params)
			for(SymbolicExpression e :exprs) {
				try {
					result = result.lub(state.smallStepSemantics(e, this));
				} catch (SemanticException e1) {
					e1.printStackTrace();
				}
			}	
		
		return result;
	}
}
