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
import it.unive.michelsonlisa.cfg.type.composite.MichelsonOptionType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonTicketType;

public class MichelsonJoin_tickets extends BinaryExpression implements StackConsumer, StackProducer {

	public MichelsonJoin_tickets(CFG cfg, String sourceFile, int line, int col, Expression left, Expression right) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "JOIN_TICKETS", new MichelsonTicketType(new MichelsonOptionType(left.getStaticType())), left, right);
		
		checkParameterTypes(left.getStaticType(), right.getStaticType());	
	}

	private void checkParameterTypes(Type type, Type type2) {
		if(!(type instanceof MichelsonTicketType || type.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The value of type "+type+" cannot used in JOIN_TICKETS");
		
		if(!(type2 instanceof MichelsonTicketType || type2.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The value of type "+type2+" cannot used in JOIN_TICKETS");
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
