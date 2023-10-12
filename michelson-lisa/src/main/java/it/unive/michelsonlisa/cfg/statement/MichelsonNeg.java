package it.unive.michelsonlisa.cfg.statement;

import it.unive.lisa.analysis.AbstractState;
import it.unive.lisa.analysis.AnalysisState;
import it.unive.lisa.analysis.SemanticException;
import it.unive.lisa.analysis.StatementStore;
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
import it.unive.michelsonlisa.cfg.type.MichelsonBLS12_381_FRType;
import it.unive.michelsonlisa.cfg.type.MichelsonBLS12_381_G1Type;
import it.unive.michelsonlisa.cfg.type.MichelsonBLS12_381_G2Type;
import it.unive.michelsonlisa.cfg.type.MichelsonIntType;
import it.unive.michelsonlisa.cfg.type.MichelsonNatType;

public class MichelsonNeg extends UnaryExpression implements StackConsumer, StackProducer {

	public MichelsonNeg(CFG cfg, String sourceFile, int line, int col, Expression expression) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "NEG", computeType(expression.getStaticType()), expression);
	}

	private static Type computeType(Type type) {
		
		if(!(type instanceof MichelsonNatType 
				|| type instanceof MichelsonIntType
				|| type instanceof MichelsonBLS12_381_G1Type 
				|| type instanceof MichelsonBLS12_381_G2Type 
				|| type instanceof MichelsonBLS12_381_FRType
				|| type.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The value of type "+type+" cannot used in NEG");
		
		if(type instanceof MichelsonNatType)
			return MichelsonIntType.INSTANCE;
		
		return type;
	}

	@Override
	public <A extends AbstractState<A>> AnalysisState<A> fwdUnarySemantics(InterproceduralAnalysis<A> interprocedural,
			AnalysisState<A> state, SymbolicExpression expr, StatementStore<A> expressions) throws SemanticException {
		return state.smallStepSemantics(expr, this);
	}


}
