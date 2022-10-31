package it.unive.michelsonlisa.cfg.statement;

import it.unive.lisa.analysis.AbstractState;
import it.unive.lisa.analysis.AnalysisState;
import it.unive.lisa.analysis.SemanticException;
import it.unive.lisa.analysis.StatementStore;
import it.unive.lisa.analysis.heap.HeapDomain;
import it.unive.lisa.analysis.value.TypeDomain;
import it.unive.lisa.analysis.value.ValueDomain;
import it.unive.lisa.interprocedural.InterproceduralAnalysis;
import it.unive.lisa.program.SourceCodeLocation;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.program.cfg.statement.UnaryExpression;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.Untyped;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackConsumer;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackProducer;
import it.unive.michelsonlisa.cfg.type.MichelsonKeyHashType;
import it.unive.michelsonlisa.cfg.type.MichelsonOperationType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonOptionType;

public class MichelsonSet_delegate extends UnaryExpression implements StackConsumer, StackProducer {

	public MichelsonSet_delegate(CFG cfg, String sourceFile, int line, int col, Expression expression) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "SET_DELEGATE", MichelsonOperationType.INSTANCE, expression);
		checkParameterType(expression.getStaticType());	
	}

	private void checkParameterType(Type type) {
		
		if(type instanceof MichelsonOptionType) {
			MichelsonOptionType oType = (MichelsonOptionType) type;
			if(!(oType.getContentType() instanceof MichelsonKeyHashType 
					|| oType.getContentType().equals(Untyped.INSTANCE)))
				throw new IllegalArgumentException("The value of type "+type+" cannot used in SET_DELEGATE");
			return;
		}
			
			
		if(!type.equals(Untyped.INSTANCE))
			throw new IllegalArgumentException("The value of type "+type+" cannot used in SET_DELEGATE");

	}

	@Override
	protected <A extends AbstractState<A, H, V, T>, H extends HeapDomain<H>, V extends ValueDomain<V>, T extends TypeDomain<T>> AnalysisState<A, H, V, T> unarySemantics(
			InterproceduralAnalysis<A, H, V, T> interprocedural, AnalysisState<A, H, V, T> state,
			SymbolicExpression expr, StatementStore<A, H, V, T> expressions) throws SemanticException {
		// TODO Auto-generated method stub
		return state;
	}


}
