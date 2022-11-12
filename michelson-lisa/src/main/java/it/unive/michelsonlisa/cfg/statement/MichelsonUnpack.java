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
import it.unive.lisa.program.cfg.statement.VariableRef;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.Untyped;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackConsumer;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackProducer;
import it.unive.michelsonlisa.cfg.type.MichelsonBytesType;

public class MichelsonUnpack extends BinaryExpression implements StackConsumer, StackProducer {

	public MichelsonUnpack(CFG cfg, String sourceFile, int line, int col, Expression type, VariableRef bytes) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "UNPACK", type, bytes);
		checkParameterType(bytes.getStaticType());
	}

	private void checkParameterType(Type type) {
		if(!(type instanceof MichelsonBytesType || type.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The value of type "+type+" cannot used in UNPACK");
		
	}

	@Override
	public <A extends AbstractState<A, H, V, T>, H extends HeapDomain<H>, V extends ValueDomain<V>, T extends TypeDomain<T>> AnalysisState<A, H, V, T> binarySemantics(
			InterproceduralAnalysis<A, H, V, T> interprocedural, AnalysisState<A, H, V, T> state,
			SymbolicExpression left, SymbolicExpression right, StatementStore<A, H, V, T> expressions)
			throws SemanticException {
		AnalysisState<A, H, V, T> result = state.bottom();
		result = state.smallStepSemantics(left, this);
		result = result.lub(state.smallStepSemantics(right, this));
		return result;
	}

}
