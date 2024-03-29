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
import it.unive.michelsonlisa.cfg.type.MichelsonAddressType;
import it.unive.michelsonlisa.cfg.type.MichelsonMutezType;
import it.unive.michelsonlisa.cfg.type.MichelsonOperationType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonContractType;

public class MichelsonTransfer_tokens extends NaryExpression implements StackConsumer, StackProducer {

	public MichelsonTransfer_tokens(CFG cfg, String sourceFile, int line, int col, Expression expression, Expression expression2, Expression expression3) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "TRANSFER_TOKENS", MichelsonOperationType.INSTANCE, expression, expression2, expression3);
		checkParameterTypes(expression2.getStaticType(), expression3.getStaticType());
	}


	private void checkParameterTypes(Type t2, Type t3) {
		if(!(t2 instanceof MichelsonMutezType || t2.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The value of type "+t2+" cannot used in TRANSFER_TOKENS");
		if(!(t3 instanceof MichelsonContractType || t3 instanceof MichelsonAddressType|| t3.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The value of type "+t3+" cannot used in TRANSFER_TOKENS");
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
