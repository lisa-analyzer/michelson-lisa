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
import it.unive.lisa.type.Type;
import it.unive.lisa.type.Untyped;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackConsumer;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackProducer;
import it.unive.michelsonlisa.cfg.type.MichelsonBoolType;
import it.unive.michelsonlisa.cfg.type.MichelsonBytesType;
import it.unive.michelsonlisa.cfg.type.MichelsonKeyType;
import it.unive.michelsonlisa.cfg.type.MichelsonSignatureType;

public class MichelsonCheck_signature extends NaryExpression implements StackConsumer, StackProducer {

	public MichelsonCheck_signature(CFG cfg, String sourceFile, int line, int col, Expression expression, Expression expression2, Expression expression3) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "CHECK_SIGNATURE", MichelsonBoolType.INSTANCE, expression, expression2, expression3);
		checkParameterTypes(expression.getStaticType(), expression2.getStaticType(), expression3.getStaticType());	
	}


	private void checkParameterTypes(Type t1, Type t2, Type t3) {
		if(!(t1 instanceof MichelsonKeyType 
				|| t1.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The left value of type "+t1+" cannot used in CHECK_SIGNATURE");
		
		if(!(t2 instanceof MichelsonSignatureType
				|| t2.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The middle value of type "+t2+" cannot used in ADD");
		
		if(!(t3 instanceof MichelsonBytesType 
				|| t3.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The middle value of type "+t2+" cannot used in ADD");
		
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
