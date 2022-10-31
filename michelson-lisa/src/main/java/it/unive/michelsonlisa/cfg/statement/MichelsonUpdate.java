package it.unive.michelsonlisa.cfg.statement;

import java.util.ArrayList;
import java.util.List;

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
import it.unive.michelsonlisa.cfg.expression.literal.MichelsonNaturalData;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackConsumer;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackProducer;
import it.unive.michelsonlisa.cfg.type.MichelsonBoolType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonBigMapType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonMapType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonOptionType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonPairType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonSetType;

public class MichelsonUpdate extends NaryExpression implements StackConsumer, StackProducer {

	public MichelsonUpdate(CFG cfg, String sourceFile, int line, int col, Expression p0, Expression p1, Expression p2) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "UPDATE", p2.getStaticType() , p0, p1, p2);
		
		checkParameterTypes(p0.getStaticType(), p1.getStaticType(), p2.getStaticType());
	}


	private void checkParameterTypes(Type t1, Type t2, Type t3) {
		if(!(t2 instanceof MichelsonBoolType 
				|| t2 instanceof MichelsonOptionType
				|| t2.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The value of type "+t2+" cannot used in UPDATE");
		
		if(!(t3 instanceof MichelsonSetType 
				|| t3 instanceof MichelsonMapType
				|| t3 instanceof MichelsonBigMapType
				|| t3.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The value of type "+t3+" cannot used in UPDATE");
		
		if(t2.equals(Untyped.INSTANCE) || t3.equals(Untyped.INSTANCE))
			return;
		
		if(t2 instanceof MichelsonBoolType &&  t3 instanceof MichelsonSetType)
			return;
		
		if(t2 instanceof MichelsonOptionType && (t3 instanceof MichelsonMapType || t3 instanceof MichelsonBigMapType))
			return;
		
		throw new IllegalArgumentException("The operation UPDATE cannot used with the types  "+t2+" : "+t3);
		
	}


	public MichelsonUpdate(CFG cfg, String sourceFile, int line, int col, MichelsonNaturalData nat, Expression p1, Expression p2) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "UPDATE", computeType(nat, p1.getStaticType(), p2.getStaticType()), nat, p1, p2);
	}

	private static Type computeType(MichelsonNaturalData nat, Type t1, Type t2) {
		
		if(!(t2 instanceof MichelsonPairType || t2.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The value of type "+t2+" cannot used in UPDATE");
		
		int n = nat.getValue().intValueExact();
		if(n == 0)
			return t2;
		else {
			if(t2 instanceof MichelsonPairType) {
				MichelsonPairType tmp = (MichelsonPairType) t2;
				List<Type> types = new ArrayList<>();
				int count =1;
				while(tmp instanceof MichelsonPairType) {
					if(count == n)
						types.add(t1);
					else if(count % 2 == 1)
						types.add(tmp.getLeftType());
					else if(tmp.getRightType() instanceof MichelsonPairType)
						tmp = (MichelsonPairType) tmp.getRightType();
					else
						types.add(tmp.getRightType());
				}
			}
		}
		
		return Untyped.INSTANCE;
	}


	@Override
	public <A extends AbstractState<A, H, V, T>, H extends HeapDomain<H>, V extends ValueDomain<V>, T extends TypeDomain<T>> AnalysisState<A, H, V, T> expressionSemantics(
			InterproceduralAnalysis<A, H, V, T> interprocedural, AnalysisState<A, H, V, T> state,
			ExpressionSet<SymbolicExpression>[] params, StatementStore<A, H, V, T> expressions)
			throws SemanticException {
		// TODO Auto-generated method stub
		return state;
	}
}
