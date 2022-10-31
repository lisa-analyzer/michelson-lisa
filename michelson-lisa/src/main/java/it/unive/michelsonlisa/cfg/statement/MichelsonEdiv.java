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
import it.unive.lisa.program.cfg.statement.BinaryExpression;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.Untyped;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackConsumer;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackProducer;
import it.unive.michelsonlisa.cfg.type.MichelsonIntType;
import it.unive.michelsonlisa.cfg.type.MichelsonMutezType;
import it.unive.michelsonlisa.cfg.type.MichelsonNatType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonOptionType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonPairType;

public class MichelsonEdiv extends BinaryExpression implements StackConsumer, StackProducer, MichelsonBinaryNumericalOperation{

	public MichelsonEdiv(CFG cfg, String sourceFile, int line, int col, Expression left, Expression right) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "EDIV", computeType(left.getStaticType(), right.getStaticType()), left, right);
	}

	private static Type computeType(Type t1, Type t2) {
		
		if(!(t1 instanceof MichelsonNatType 
				|| t1 instanceof MichelsonIntType
				|| t1 instanceof MichelsonMutezType
				|| t1.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The left value of type "+t1+" cannot used in EDIV");
		
		if(!(t2 instanceof MichelsonNatType 
				|| t2 instanceof MichelsonIntType
				|| t2 instanceof MichelsonMutezType 
				|| t2.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The right value of type "+t2+" cannot used in EDIV");
		
		if(t1 instanceof MichelsonIntType) {
			if(t2 instanceof MichelsonNatType )
				return new MichelsonOptionType(new MichelsonPairType(MichelsonIntType.INSTANCE, MichelsonNatType.INSTANCE));
			else if(t2 instanceof MichelsonIntType)
				return new MichelsonOptionType(new MichelsonPairType(MichelsonIntType.INSTANCE, MichelsonNatType.INSTANCE));
		}

		if(t1 instanceof MichelsonNatType) {
			if(t2 instanceof MichelsonNatType )
				return new MichelsonOptionType(new MichelsonPairType(MichelsonNatType.INSTANCE, MichelsonNatType.INSTANCE));
			else if(t2 instanceof MichelsonIntType)
				return new MichelsonOptionType(new MichelsonPairType(MichelsonIntType.INSTANCE, MichelsonNatType.INSTANCE));
		}

		if(t1 instanceof MichelsonMutezType) {
			if(t2 instanceof MichelsonNatType )
				return new MichelsonOptionType(new MichelsonPairType(MichelsonMutezType.INSTANCE, MichelsonMutezType.INSTANCE));
			else if(t2 instanceof MichelsonMutezType)
				return new MichelsonOptionType(new MichelsonPairType(MichelsonNatType.INSTANCE, MichelsonMutezType.INSTANCE));
		}
		
		if(t1.equals(Untyped.INSTANCE) || t2.equals(Untyped.INSTANCE))
			return Untyped.INSTANCE;
		else
			throw new IllegalArgumentException("The operation EDIV cannot used with the types  "+t1+" : "+t2);

	}

	@Override
	protected <A extends AbstractState<A, H, V, T>, H extends HeapDomain<H>, V extends ValueDomain<V>, T extends TypeDomain<T>> AnalysisState<A, H, V, T> binarySemantics(
			InterproceduralAnalysis<A, H, V, T> interprocedural, AnalysisState<A, H, V, T> state,
			SymbolicExpression left, SymbolicExpression right, StatementStore<A, H, V, T> expressions)
			throws SemanticException {
		return state;
	}



}
