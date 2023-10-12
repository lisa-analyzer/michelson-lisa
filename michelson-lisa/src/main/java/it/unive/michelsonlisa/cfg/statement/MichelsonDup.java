package it.unive.michelsonlisa.cfg.statement;

import it.unive.lisa.analysis.AbstractState;
import it.unive.lisa.analysis.AnalysisState;
import it.unive.lisa.analysis.SemanticException;
import it.unive.lisa.analysis.StatementStore;
import it.unive.lisa.interprocedural.InterproceduralAnalysis;
import it.unive.lisa.program.SourceCodeLocation;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.michelsonlisa.cfg.expression.literal.MichelsonNaturalData;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackProducer;

public class MichelsonDup extends it.unive.lisa.program.cfg.statement.UnaryExpression implements StackProducer{

	MichelsonNaturalData nat;
	
	public MichelsonDup(CFG cfg, String sourceFile, int line, int col, Expression expression) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "DUP", expression.getStaticType(), expression);
	}

	public MichelsonDup(CFG cfg, String sourceFile, int line, int col, MichelsonNaturalData nat, Expression expression) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "DUP", expression.getStaticType(), expression);
		this.nat = nat;
	}
	
	public int getNumberOfStackElementsProduced(){
		return  nat == null ? 1 : nat.getValue().intValueExact();
	}

	@Override
	public <A extends AbstractState<A>> AnalysisState<A> fwdUnarySemantics(InterproceduralAnalysis<A> interprocedural,
			AnalysisState<A> state, SymbolicExpression expr, StatementStore<A> expressions) throws SemanticException {
		return state.smallStepSemantics(expr, this);
	}
	
}
