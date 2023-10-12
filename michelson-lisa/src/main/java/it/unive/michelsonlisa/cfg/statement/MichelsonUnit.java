package it.unive.michelsonlisa.cfg.statement;

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
import it.unive.michelsonlisa.cfg.statement.interfaces.StackProducer;
import it.unive.michelsonlisa.cfg.type.MichelsonUnitType;

public class MichelsonUnit extends NaryExpression implements StackProducer {

	public MichelsonUnit(CFG cfg, String sourceFile, int line, int col) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "UNIT", MichelsonUnitType.INSTANCE);
	}

	@Override
	public <A extends AbstractState<A>> AnalysisState<A> forwardSemanticsAux(InterproceduralAnalysis<A> interprocedural,
			AnalysisState<A> state, ExpressionSet[] params, StatementStore<A> expressions) throws SemanticException {
		return state.smallStepSemantics(new Constant(MichelsonUnitType.INSTANCE, "UNIT_VALUE" , getLocation()), this);

	}
}
