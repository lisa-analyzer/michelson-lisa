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
import it.unive.michelsonlisa.cfg.type.composite.MichelsonOptionType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonPairType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonSaplingStateType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonSaplingTransactionType;

public class MichelsonSapling_verify_update extends BinaryExpression implements StackConsumer, StackProducer {

	public MichelsonSapling_verify_update(CFG cfg, String sourceFile, int line, int col, Expression left, Expression right) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "SAPLING_VERIFY_UPDATE", computeType(left.getStaticType(), right.getStaticType()), left, right);
	}

	private static Type computeType(Type t1, Type t2) {
		
		if(!(t1 instanceof MichelsonSaplingTransactionType || t1.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The value of type "+t1+" cannot used in SAPLING_VERIFY_UPDATE");
		
		if(!(t2 instanceof MichelsonSaplingStateType || t2.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The value of type "+t2+" cannot used in SAPLING_VERIFY_UPDATE");
		
		return new MichelsonOptionType(new MichelsonPairType( MichelsonIntType.INSTANCE, t2));
	}

	@Override
	protected <A extends AbstractState<A, H, V, T>, H extends HeapDomain<H>, V extends ValueDomain<V>, T extends TypeDomain<T>> AnalysisState<A, H, V, T> binarySemantics(
			InterproceduralAnalysis<A, H, V, T> interprocedural, AnalysisState<A, H, V, T> state,
			SymbolicExpression left, SymbolicExpression right, StatementStore<A, H, V, T> expressions)
			throws SemanticException {
		// TODO Auto-generated method stub
		return state;
	}
}
