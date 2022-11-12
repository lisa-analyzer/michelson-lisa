package it.unive.michelsonlisa.cfg.statement.instrumentation;

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
import it.unive.lisa.symbolic.value.BinaryExpression;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.Untyped;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackConsumer;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackProducer;
import it.unive.michelsonlisa.symblic.value.operator.binary.PhiOperator;

public class Phi extends it.unive.lisa.program.cfg.statement.BinaryExpression implements StackProducer, StackConsumer {

	public Phi(CFG cfg, SourceCodeLocation srcLoc, VariableRef left, VariableRef right) {
		super(cfg, srcLoc, "Phi", computeType(left.getStaticType(), right.getStaticType()), left, right);
	}

	private static Type computeType(Type t1, Type t2) {
		if(t1.equals(t2))
			return t1;
		return Untyped.INSTANCE;
	}

	@Override
	public <A extends AbstractState<A, H, V, T>, H extends HeapDomain<H>, V extends ValueDomain<V>, T extends TypeDomain<T>> AnalysisState<A, H, V, T> binarySemantics(
			InterproceduralAnalysis<A, H, V, T> interprocedural, AnalysisState<A, H, V, T> state,
			SymbolicExpression left, SymbolicExpression right, StatementStore<A, H, V, T> expressions)
			throws SemanticException {

		return state.smallStepSemantics(
				new BinaryExpression(
						getStaticType(),
						left,
						right,
						PhiOperator.INSTANCE,
						getLocation()),	this);
	}

}