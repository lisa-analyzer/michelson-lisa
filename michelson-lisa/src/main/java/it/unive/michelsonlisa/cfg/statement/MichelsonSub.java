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
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.symbolic.value.BinaryExpression;
import it.unive.lisa.symbolic.value.operator.binary.NumericNonOverflowingSub;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.Untyped;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackConsumer;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackProducer;
import it.unive.michelsonlisa.cfg.type.MichelsonIntType;
import it.unive.michelsonlisa.cfg.type.MichelsonMutezType;
import it.unive.michelsonlisa.cfg.type.MichelsonNatType;
import it.unive.michelsonlisa.cfg.type.MichelsonTimestampType;

public class MichelsonSub extends it.unive.lisa.program.cfg.statement.BinaryExpression implements StackConsumer, StackProducer {

	public MichelsonSub(CFG cfg, String sourceFile, int line, int col, Expression left, Expression right) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "SUB", computeType(left.getStaticType(),right.getStaticType()), left, right);
		
		
	}
	
	private static Type computeType(Type t1, Type t2) {
		
		if(!(t1 instanceof MichelsonNatType 
				|| t1 instanceof MichelsonIntType
				|| t1 instanceof MichelsonTimestampType
				|| t1 instanceof MichelsonMutezType 
				|| t1.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The left value of type "+t1+" cannot used in ADD");
		
		if(!(t2 instanceof MichelsonNatType 
				|| t2 instanceof MichelsonIntType
				|| t2 instanceof MichelsonTimestampType
				|| t2 instanceof MichelsonMutezType 
				|| t2.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The right value of type "+t2+" cannot used in ADD");

		if(t1 instanceof MichelsonIntType) {
			if(t2 instanceof MichelsonNatType)
				return MichelsonIntType.INSTANCE;
		}

		if(t1 instanceof MichelsonNatType) {
			if(t2 instanceof MichelsonIntType)
				return MichelsonIntType.INSTANCE;
		}

		if(t1 instanceof MichelsonTimestampType) {
			if(t2 instanceof MichelsonIntType)
				return MichelsonTimestampType.INSTANCE;
		}
		
		if(t1.equals(t2) && (t1 instanceof MichelsonNatType 
				|| t1 instanceof MichelsonIntType 
				|| t1 instanceof MichelsonMutezType 
				|| t1 instanceof MichelsonTimestampType) )
			return t1;

		if(t1.equals(Untyped.INSTANCE) || t2.equals(Untyped.INSTANCE))
			return Untyped.INSTANCE;
		else
			throw new IllegalArgumentException("The operation SUB cannot used with the types  "+t1+" : "+t2);
	}

	
	@Override
	protected <A extends AbstractState<A, H, V, T>, H extends HeapDomain<H>, V extends ValueDomain<V>, T extends TypeDomain<T>> AnalysisState<A, H, V, T> binarySemantics(
			InterproceduralAnalysis<A, H, V, T> interprocedural, AnalysisState<A, H, V, T> state,
			SymbolicExpression left, SymbolicExpression right, StatementStore<A, H, V, T> expressions)
			throws SemanticException {
		return state.smallStepSemantics(
				new BinaryExpression(
						getStaticType(),
						left,
						right,
						NumericNonOverflowingSub.INSTANCE,
						getLocation()),
				this);
	}

}
