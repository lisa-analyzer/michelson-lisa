package it.unive.michelsonlisa.cfg.statement;

import it.unive.lisa.analysis.AbstractState;
import it.unive.lisa.analysis.AnalysisState;
import it.unive.lisa.analysis.SemanticException;
import it.unive.lisa.analysis.StatementStore;
import it.unive.lisa.interprocedural.InterproceduralAnalysis;
import it.unive.lisa.program.SourceCodeLocation;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.statement.UnaryExpression;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.symbolic.value.Constant;
import it.unive.michelsonlisa.cfg.expression.literal.MichelsonNaturalData;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackConsumer;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackProducer;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonSaplingStateType;

public class MichelsonSaplingEmptyState extends UnaryExpression implements StackConsumer, StackProducer {

	MichelsonSaplingStateType type;
	public MichelsonSaplingEmptyState(CFG cfg, String sourceFile, int line, int col, MichelsonNaturalData nat) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "SAPLING_EMPTY_STATE", new MichelsonSaplingStateType(nat), nat);
		type = new MichelsonSaplingStateType(nat);
	}


	@Override
	public <A extends AbstractState<A>> AnalysisState<A> fwdUnarySemantics(InterproceduralAnalysis<A> interprocedural,
			AnalysisState<A> state, SymbolicExpression expr, StatementStore<A> expressions) throws SemanticException {
		return state.smallStepSemantics(new Constant(type, "EMPY_STATE", getLocation()), this);

	}

}
