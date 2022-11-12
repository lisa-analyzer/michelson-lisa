package it.unive.michelsonlisa.cfg.statement;

import java.util.Set;

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
import it.unive.lisa.symbolic.value.UnaryExpression;
import it.unive.lisa.symbolic.value.operator.unary.UnaryOperator;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.TypeSystem;
import it.unive.lisa.type.Untyped;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackConsumer;
import it.unive.michelsonlisa.cfg.type.MichelsonBLS12_381_G1Type;
import it.unive.michelsonlisa.cfg.type.MichelsonBLS12_381_G2Type;
import it.unive.michelsonlisa.cfg.type.MichelsonBoolType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonListType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonPairType;

public class MichelsonPairing_check extends it.unive.lisa.program.cfg.statement.UnaryExpression implements StackConsumer {

	public MichelsonPairing_check(CFG cfg, String sourceFile, int line, int col, Expression expression) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "PAIRING_CHECK", MichelsonBoolType.INSTANCE, expression);
		
		checkParameterType(expression.getStaticType());	
	}

	private void checkParameterType(Type type) {
		
		if(type.equals(Untyped.INSTANCE))
			return;
		if(!(type instanceof MichelsonListType))
			throw new IllegalArgumentException("The value of type "+type+" cannot used in PAIRING_CHECK");
		
		Type  lType = ((MichelsonListType) type).getContentType();
		
		if(lType.equals(Untyped.INSTANCE))
			return;
		else if(!(lType instanceof MichelsonPairType))
			throw new IllegalArgumentException("The value of type "+lType+" cannot used in PAIRING_CHECK");
		
		MichelsonPairType pType = (MichelsonPairType) lType;
		
		if(!(pType.getLeftType() instanceof MichelsonBLS12_381_G1Type || pType.getLeftType().equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The value of type "+pType.getLeftType()+" cannot used in PAIRING_CHECK");
		
		if(!(pType.getRightType() instanceof MichelsonBLS12_381_G2Type || pType.getRightType().equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The value of type "+pType.getRightType()+" cannot used in PAIRING_CHECK");
	}

	@Override
	public <A extends AbstractState<A, H, V, T>, H extends HeapDomain<H>, V extends ValueDomain<V>, T extends TypeDomain<T>> AnalysisState<A, H, V, T> unarySemantics(
			InterproceduralAnalysis<A, H, V, T> interprocedural, AnalysisState<A, H, V, T> state,
			SymbolicExpression expr, StatementStore<A, H, V, T> expressions) throws SemanticException {
		UnaryOperator opt = new UnaryOperator() {

			@Override
			public Set<Type> typeInference(TypeSystem types, Set<Type> argument) {
				return Set.of(MichelsonBoolType.INSTANCE);
			}
		};
		return state.smallStepSemantics(new UnaryExpression(MichelsonBoolType.INSTANCE, expr, opt, getLocation()), this);
	}
}
