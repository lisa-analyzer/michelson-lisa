package it.unive.michelsonlisa.cfg.statement;

import java.util.Set;

import it.unive.lisa.analysis.AbstractState;
import it.unive.lisa.analysis.AnalysisState;
import it.unive.lisa.analysis.SemanticException;
import it.unive.lisa.analysis.StatementStore;
import it.unive.lisa.interprocedural.InterproceduralAnalysis;
import it.unive.lisa.program.SourceCodeLocation;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.symbolic.value.BinaryExpression;
import it.unive.lisa.symbolic.value.operator.binary.BinaryOperator;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.TypeSystem;
import it.unive.lisa.type.Untyped;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackConsumer;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackProducer;
import it.unive.michelsonlisa.cfg.type.MichelsonNatType;

public class MichelsonLsl extends it.unive.lisa.program.cfg.statement.BinaryExpression implements StackConsumer, StackProducer {

	public MichelsonLsl(CFG cfg, String sourceFile, int line, int col, Expression e1, Expression e2) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "LSL", MichelsonNatType.INSTANCE, e1, e2);
		checkParameterType(e1.getStaticType(), e2.getStaticType());	
	}

	private void checkParameterType(Type type1, Type type2) {
		if(!(type1 instanceof MichelsonNatType || type1.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The value of type "+type1+" cannot used in LSL");
		
		if(!(type2 instanceof MichelsonNatType || type2.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The value of type "+type2+" cannot used in LSL");
	}

	@Override
	public <A extends AbstractState<A>> AnalysisState<A> fwdBinarySemantics(InterproceduralAnalysis<A> interprocedural,
			AnalysisState<A> state, SymbolicExpression left, SymbolicExpression right, StatementStore<A> expressions)
			throws SemanticException {
		BinaryOperator opt = new BinaryOperator() {

			@Override
			public Set<Type> typeInference(TypeSystem types, Set<Type> left, Set<Type> right) {
				return Set.of(MichelsonNatType.INSTANCE);
			}
		};
		return state.smallStepSemantics(new BinaryExpression(MichelsonNatType.INSTANCE, left, right, opt, getLocation()), this);
	
	}


}
