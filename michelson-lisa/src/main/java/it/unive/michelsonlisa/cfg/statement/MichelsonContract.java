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
import it.unive.lisa.program.cfg.statement.VariableRef;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.symbolic.value.UnaryExpression;
import it.unive.lisa.symbolic.value.operator.unary.UnaryOperator;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.TypeSystem;
import it.unive.lisa.type.Untyped;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackConsumer;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackProducer;
import it.unive.michelsonlisa.cfg.type.MichelsonAddressType;

public class MichelsonContract extends it.unive.lisa.program.cfg.statement.UnaryExpression implements StackProducer, StackConsumer{

	
	public MichelsonContract(CFG cfg, String sourceFile, int line, int col, Type type, VariableRef address) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "CONTRACT", type, address);
		checkParameterType(address.getStaticType());	
	}

	private void checkParameterType(Type type) {
		if(!(type instanceof MichelsonAddressType || type.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The value of type "+type+" cannot used in CONTRACT");
	}
	public MichelsonContract(CFG cfg, SourceCodeLocation location, Type type,
			VariableRef address) {
		super(cfg, location, "CONTRACT", type, address);
	}
	
	@Override
	public <A extends AbstractState<A, H, V, T>, H extends HeapDomain<H>, V extends ValueDomain<V>, T extends TypeDomain<T>> AnalysisState<A, H, V, T> unarySemantics(
			InterproceduralAnalysis<A, H, V, T> interprocedural, AnalysisState<A, H, V, T> state,
			SymbolicExpression expr, StatementStore<A, H, V, T> expressions) throws SemanticException {

		UnaryOperator opt = new UnaryOperator() {

			@Override
			public Set<Type> typeInference(TypeSystem types, Set<Type> argument) {
				return Set.of(getStaticType());
			}
		};

		return state.smallStepSemantics(new UnaryExpression(getStaticType(), expr, opt, getLocation()), this);
	}

}
