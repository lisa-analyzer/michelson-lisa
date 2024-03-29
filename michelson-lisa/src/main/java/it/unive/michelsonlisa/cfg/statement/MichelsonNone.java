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
import it.unive.lisa.type.Type;
import it.unive.michelsonlisa.cfg.expression.literal.MichelsonNoneData;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackProducer;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonOptionType;

public class MichelsonNone extends UnaryExpression implements StackProducer {

	public MichelsonNone(CFG cfg, String sourceFile, int line, int col, Type type) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "NONE", new MichelsonOptionType(type),  new MichelsonNoneData(cfg, new SourceCodeLocation(sourceFile, line, col), type));
	}

	@Override
	public <A extends AbstractState<A>> AnalysisState<A> fwdUnarySemantics(InterproceduralAnalysis<A> interprocedural,
			AnalysisState<A> state, SymbolicExpression expr, StatementStore<A> expressions) throws SemanticException {
		return state.smallStepSemantics(new Constant(getStaticType(), "NONE_VALUE", getLocation()), this);

	}

}
