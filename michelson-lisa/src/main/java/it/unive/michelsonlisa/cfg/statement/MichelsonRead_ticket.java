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
import it.unive.michelsonlisa.cfg.type.MichelsonAddressType;
import it.unive.michelsonlisa.cfg.type.MichelsonNatType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonPairType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonTicketType;

public class MichelsonRead_ticket extends UnaryExpression implements StackConsumer, StackProducer {

	public MichelsonRead_ticket(CFG cfg, String sourceFile, int line, int col, Expression expr) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "READ_TICKET", computeType(expr.getStaticType()), expr);
	}

	private static Type computeType(Type type) {
		if(!(type instanceof MichelsonTicketType || type.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The value of type "+type+" cannot used in READ_TICKET");
		
		if(type instanceof MichelsonTicketType)
			return new MichelsonPairType(MichelsonAddressType.INSTANCE, MichelsonNatType.INSTANCE, type);
		
		return new MichelsonPairType(MichelsonAddressType.INSTANCE, MichelsonNatType.INSTANCE, new MichelsonTicketType(Untyped.INSTANCE));
	}

	@Override
	public <A extends AbstractState<A>> AnalysisState<A> fwdUnarySemantics(InterproceduralAnalysis<A> interprocedural,
			AnalysisState<A> state, SymbolicExpression expr, StatementStore<A> expressions) throws SemanticException {
		return state.smallStepSemantics(expr, this);
	}
}
