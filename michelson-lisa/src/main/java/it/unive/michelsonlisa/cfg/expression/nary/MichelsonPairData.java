package it.unive.michelsonlisa.cfg.expression.nary;

import it.unive.lisa.analysis.AbstractState;
import it.unive.lisa.analysis.AnalysisState;
import it.unive.lisa.analysis.SemanticException;
import it.unive.lisa.analysis.StatementStore;
import it.unive.lisa.analysis.lattices.ExpressionSet;
import it.unive.lisa.interprocedural.InterproceduralAnalysis;
import it.unive.lisa.program.SourceCodeLocation;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonPairType;
import it.unive.michelsonlisa.utils.MichelsonTypeUtils;

/**
 * A PAIR data of Michelson language 
 * 
 * @author <a href="mailto:luca.olivieri@univr.it">Luca Olivieri</a>
 */
public class MichelsonPairData extends it.unive.lisa.program.cfg.statement.NaryExpression {

	/**
	 * exprs must have length > 1
	 */
	public MichelsonPairData(CFG cfg, SourceCodeLocation location, Expression[] exprs) {
		super(cfg, location, "PAIR", new MichelsonPairType(MichelsonTypeUtils.getTypes(exprs)), exprs);
	}

	@Override
	public <A extends AbstractState<A>> AnalysisState<A> forwardSemanticsAux(InterproceduralAnalysis<A> interprocedural,
			AnalysisState<A> state, ExpressionSet[] params, StatementStore<A> expressions) throws SemanticException {
		return state;
	}

	
}
