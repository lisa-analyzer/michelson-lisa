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
import it.unive.lisa.symbolic.value.operator.binary.NumericNonOverflowingMul;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.Untyped;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackConsumer;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackProducer;
import it.unive.michelsonlisa.cfg.type.MichelsonBLS12_381_FRType;
import it.unive.michelsonlisa.cfg.type.MichelsonBLS12_381_G1Type;
import it.unive.michelsonlisa.cfg.type.MichelsonBLS12_381_G2Type;
import it.unive.michelsonlisa.cfg.type.MichelsonIntType;
import it.unive.michelsonlisa.cfg.type.MichelsonMutezType;
import it.unive.michelsonlisa.cfg.type.MichelsonNatType;

public class MichelsonMul extends it.unive.lisa.program.cfg.statement.BinaryExpression implements StackConsumer, StackProducer {

	public MichelsonMul(CFG cfg, String sourceFile, int line, int col, Expression left, Expression right) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "MUL", computeType(left.getStaticType(), right.getStaticType()), left, right);
	}
	
	private static Type computeType(Type t1, Type t2) {
		
		if(!(t1 instanceof MichelsonNatType 
				|| t1 instanceof MichelsonIntType
				|| t1 instanceof MichelsonMutezType 
				|| t1 instanceof MichelsonBLS12_381_G1Type 
				|| t1 instanceof MichelsonBLS12_381_G2Type 
				|| t1 instanceof MichelsonBLS12_381_FRType
				|| t1.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The left value of type "+t1+" cannot used in MUL");
		
		if(!(t2 instanceof MichelsonNatType 
				|| t2 instanceof MichelsonIntType
				|| t2 instanceof MichelsonMutezType 
				|| t2 instanceof MichelsonBLS12_381_G1Type 
				|| t2 instanceof MichelsonBLS12_381_G2Type 
				|| t2 instanceof MichelsonBLS12_381_FRType
				|| t2.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The right value of type "+t2+" cannot used in MUL");
		
		if(t1.equals(t2))
			return t1;
		
		if(t1 instanceof MichelsonIntType) {
			if(t2 instanceof MichelsonNatType)
				return MichelsonIntType.INSTANCE;
			if(t2 instanceof MichelsonBLS12_381_FRType)
				return MichelsonBLS12_381_FRType.INSTANCE;
		}

		if(t1 instanceof MichelsonNatType) {
			if(t2 instanceof MichelsonIntType)
				return MichelsonIntType.INSTANCE;
			else if(t2 instanceof MichelsonMutezType)
				return MichelsonMutezType.INSTANCE;
			else if(t2 instanceof MichelsonBLS12_381_FRType)
				return MichelsonBLS12_381_FRType.INSTANCE;
		}

		if(t1 instanceof MichelsonMutezType) {
			if(t2 instanceof MichelsonNatType)
				return MichelsonMutezType.INSTANCE;
		}

		if (t1 instanceof MichelsonBLS12_381_FRType) {
			if(t2 instanceof MichelsonIntType)
				return MichelsonBLS12_381_FRType.INSTANCE;
			if(t2 instanceof MichelsonNatType)
				return MichelsonBLS12_381_FRType.INSTANCE;
		}

		if (t1 instanceof MichelsonBLS12_381_G1Type 
						|| t1 instanceof MichelsonBLS12_381_G2Type ) {
			if(t2 instanceof MichelsonBLS12_381_FRType)
				return t1;
		} 
		
		if(t1.equals(Untyped.INSTANCE) || t2.equals(Untyped.INSTANCE))
			return Untyped.INSTANCE;
		else
			throw new IllegalArgumentException("The operation MUL cannot used with the types  "+t1+" : "+t2);
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
						NumericNonOverflowingMul.INSTANCE,
						getLocation()),
				this);
	}


}
