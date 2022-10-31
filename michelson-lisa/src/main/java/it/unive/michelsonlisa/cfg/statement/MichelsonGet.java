package it.unive.michelsonlisa.cfg.statement;

import java.math.BigInteger;

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
import it.unive.lisa.program.cfg.statement.VariableRef;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.Untyped;
import it.unive.michelsonlisa.cfg.expression.literal.MichelsonNaturalData;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackConsumer;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackProducer;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonBigMapType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonMapType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonOptionType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonPairType;

public class MichelsonGet extends NaryExpression implements StackConsumer, StackProducer{

	public MichelsonGet(CFG cfg, String sourceFile, int line, int col, Expression key, Expression map ) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "GET", computeType(map.getStaticType()),  key, map);
	}

	private static Type computeType(Type type) {
		Type valueType = Untyped.INSTANCE;
		if(type instanceof MichelsonBigMapType)
			valueType = ((MichelsonBigMapType) type).getElementType();
		else if(type instanceof MichelsonMapType)
			valueType = ((MichelsonMapType) type).getElementType();
		
		return new MichelsonOptionType(valueType);
	}

	public MichelsonGet(CFG cfg, String sourceFile, int line, int col, MichelsonNaturalData nat, VariableRef ref) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "GET", computeTypePair(ref.getStaticType(), nat.getValue()), nat, ref);
	}

	private static Type computeTypePair(Type type, BigInteger integer) {
		Type valueType = Untyped.INSTANCE;
		
		if(type instanceof MichelsonPairType) {
			MichelsonPairType tmp = (MichelsonPairType) type;
			valueType = type;
			for(int i = 1; i <= integer.intValueExact(); i++) {
				if(i % 2 == 0 )
					tmp = (MichelsonPairType) tmp.getRightType();
				if(i == integer.intValueExact())
					valueType = i % 2 == 0 ? tmp : tmp.getLeftType();
			}
				
		}
		
		if(!type.equals(Untyped.INSTANCE))
			throw new IllegalArgumentException("The value of type "+type+" cannot used in GET");
		
		return valueType;
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
