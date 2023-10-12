package it.unive.michelsonlisa.cfg.statement;

import it.unive.lisa.analysis.AbstractState;
import it.unive.lisa.analysis.AnalysisState;
import it.unive.lisa.analysis.SemanticException;
import it.unive.lisa.analysis.StatementStore;
import it.unive.lisa.analysis.lattices.ExpressionSet;
import it.unive.lisa.interprocedural.InterproceduralAnalysis;
import it.unive.lisa.program.SourceCodeLocation;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.program.cfg.statement.NaryExpression;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.Untyped;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackConsumer;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackProducer;
import it.unive.michelsonlisa.cfg.type.MichelsonNatType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonTicketType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonComparableType;

public class MichelsonTicket extends NaryExpression implements StackConsumer, StackProducer {

	public MichelsonTicket(CFG cfg, String sourceFile, int line, int col, Expression content, Expression amount) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "TICKET", computeType(content.getStaticType()), content, amount);
		checkParameterType(amount.getStaticType());	
	}

	private void checkParameterType(Type type) {
		if(!(type instanceof MichelsonNatType || type.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The value of type "+type+" cannot used in TICKET");
	}

	private static Type computeType(Type staticType) {
		if(staticType instanceof MichelsonComparableType)
			return new MichelsonTicketType((MichelsonComparableType) staticType);
		
		return new MichelsonTicketType(Untyped.INSTANCE);
	}


	@Override
	public <A extends AbstractState<A>> AnalysisState<A> forwardSemanticsAux(InterproceduralAnalysis<A> interprocedural,
			AnalysisState<A> state, ExpressionSet[] params, StatementStore<A> expressions) throws SemanticException {
		AnalysisState<A> result = state.bottom();
		for (ExpressionSet exprs : params)
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
