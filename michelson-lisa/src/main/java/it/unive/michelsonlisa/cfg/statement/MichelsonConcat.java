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
import it.unive.michelsonlisa.cfg.type.MichelsonBytesType;
import it.unive.michelsonlisa.cfg.type.MichelsonStringType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonListType;

public class MichelsonConcat extends NaryExpression implements StackConsumer, StackProducer{

	public MichelsonConcat(CFG cfg, String sourceFile, int line, int col, Expression left, Expression right) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "CONCAT", computeType(left.getStaticType(), right.getStaticType()), left, right);
	}
	
	private static Type computeType(Type t1, Type t2) {
		
		if(!(t1 instanceof MichelsonStringType
				|| t1 instanceof MichelsonBytesType
				|| t1.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The left value of type "+t1+" cannot used in ADD");
		
		if(!(t2 instanceof MichelsonStringType
				|| t2 instanceof MichelsonBytesType
				|| t2.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The right value of type "+t2+" cannot used in ADD");
		
		if(t1.equals(t2))
			return t1;
		
		return Untyped.INSTANCE;
	}
	
	public MichelsonConcat(CFG cfg, String sourceFile, int line, int col, Expression list) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "CONCAT", computeListType(list.getStaticType()), list);
	}
	
	private static Type computeListType(Type t1) {
		if(t1 instanceof MichelsonListType) {
			Type lType = ((MichelsonListType) t1 ).getContentType();
			if(!(lType instanceof MichelsonStringType
					|| lType instanceof MichelsonBytesType
					|| lType.equals(Untyped.INSTANCE)))
				throw new IllegalArgumentException("The type "+t1+" cannot used in CONCAT");
			else
				return lType;
		}
		if(t1.equals(Untyped.INSTANCE))
			return Untyped.INSTANCE;
		else
			throw new IllegalArgumentException("The type "+t1+" cannot used in CONCAT");
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
