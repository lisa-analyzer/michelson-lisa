package it.unive.michelsonlisa.cfg.statement;

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
import it.unive.lisa.symbolic.value.operator.binary.NumericNonOverflowingAdd;
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
import it.unive.michelsonlisa.cfg.type.MichelsonTimestampType;


public class MichelsonAdd extends it.unive.lisa.program.cfg.statement.BinaryExpression implements StackConsumer, StackProducer, MichelsonBinaryNumericalOperation {

	public MichelsonAdd(CFG cfg, String sourceFile, int line, int col, Expression left, Expression right) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "ADD", computeType(left.getStaticType(), right.getStaticType()), left, right);
	}

	private static Type computeType(Type t1, Type t2) {
		
		if(!(t1 instanceof MichelsonNatType 
				|| t1 instanceof MichelsonIntType
				|| t1 instanceof MichelsonTimestampType
				|| t1 instanceof MichelsonMutezType 
				|| t1 instanceof MichelsonBLS12_381_G1Type 
				|| t1 instanceof MichelsonBLS12_381_G2Type 
				|| t1 instanceof MichelsonBLS12_381_FRType
				|| t1.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The left value of type "+t1+" cannot used in ADD");
		
		if(!(t2 instanceof MichelsonNatType 
				|| t2 instanceof MichelsonIntType
				|| t2 instanceof MichelsonTimestampType
				|| t2 instanceof MichelsonMutezType 
				|| t2 instanceof MichelsonBLS12_381_G1Type 
				|| t2 instanceof MichelsonBLS12_381_G2Type 
				|| t2 instanceof MichelsonBLS12_381_FRType
				|| t2.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The right value of type "+t2+" cannot used in ADD");
		
		if(t1.equals(t2) && (t1 instanceof MichelsonNatType 
								|| t1 instanceof MichelsonIntType 
								|| t1 instanceof MichelsonMutezType
								|| t1 instanceof MichelsonTimestampType
								|| t1 instanceof MichelsonBLS12_381_G1Type 
								|| t1 instanceof MichelsonBLS12_381_G2Type 
								|| t1 instanceof MichelsonBLS12_381_FRType ) )
			return t1;
		
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
		
		if (t2 instanceof MichelsonTimestampType) {
			if(t1 instanceof MichelsonIntType)
				return MichelsonTimestampType.INSTANCE;
		} 
		
		if(t1.equals(Untyped.INSTANCE) || t2.equals(Untyped.INSTANCE))
			return Untyped.INSTANCE;
		else
			throw new IllegalArgumentException("The operation ADD cannot used with the types  "+t1+" : "+t2);
	}

	@Override
	public <A extends AbstractState<A>> AnalysisState<A> fwdBinarySemantics(InterproceduralAnalysis<A> interprocedural,
			AnalysisState<A> state, SymbolicExpression left, SymbolicExpression right, StatementStore<A> expressions)
			throws SemanticException {
		/*
		 AnalysisState<A, H, V, T> result = state.bottom();
		//getOffset()// univoco per ogni espressione del control flow graph
		Optional<Statement> target = getCFG().getNodes().stream().filter(n -> n instanceof Assignment && ((Assignment) n).getRight().equals(this)).findAny();
		
		if(target.isPresent()) {
			Assignment as = (Assignment) target.get();
			Collection<Statement> preds = getCFG().predecessorsOf(as); // (set di tutti i predecessori)
			
			for(Statement p : preds) {
				AnalysisState<A, H, V, T> s = expressions.getState(p);
			}
			//for each predecessor
				//  small step (della var predecessor)
			// lub di tutte le small step -> stato su cui operare
			//poi invocare la small step (come nella return) sullo stato appena creato 
			
			result = result.smallStepSemantics(
					new BinaryExpression(
							getStaticType(),
							left,
							right,
							NumericNonOverflowingAdd.INSTANCE,
							getLocation()),
					this);
			//poi puoi fare forget identifier e fai la pop degli id 
			//analysisState.forgetIdentifier();
		
		return result;
		}
		*/
		return state.smallStepSemantics(
				new BinaryExpression(
						MichelsonIntType.INSTANCE,
						left,
						right,
						NumericNonOverflowingAdd.INSTANCE,
						getLocation()),
				this);
	}

}
