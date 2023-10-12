package it.unive.michelsonlisa.cfg.statement.instrumentation;

import it.unive.lisa.analysis.AbstractState;
import it.unive.lisa.analysis.AnalysisState;
import it.unive.lisa.analysis.SemanticException;
import it.unive.lisa.analysis.StatementStore;
import it.unive.lisa.analysis.lattices.ExpressionSet;
import it.unive.lisa.interprocedural.InterproceduralAnalysis;
import it.unive.lisa.program.SourceCodeLocation;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.statement.NaryExpression;
import it.unive.lisa.symbolic.value.Constant;
import it.unive.michelsonlisa.cfg.type.MichelsonMutezType;

/*
 * Instrumented Empty Structure of List/Map
 */
public class EmptyStructure extends NaryExpression {

	public EmptyStructure(CFG cfg, SourceCodeLocation srcLoc) {
		super(cfg, srcLoc, "{}");
	}

	
	@Override
	public <A extends AbstractState<A>> AnalysisState<A> forwardSemanticsAux(InterproceduralAnalysis<A> interprocedural,
			AnalysisState<A> state, ExpressionSet[] params, StatementStore<A> expressions) throws SemanticException {
		return state.smallStepSemantics(new Constant(MichelsonMutezType.INSTANCE, "{}", getLocation()), this);
	}

}