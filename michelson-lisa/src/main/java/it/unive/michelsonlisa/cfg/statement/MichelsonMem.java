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
import it.unive.lisa.symbolic.value.BinaryExpression;
import it.unive.lisa.symbolic.value.operator.binary.BinaryOperator;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.TypeSystem;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackConsumer;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackProducer;
import it.unive.michelsonlisa.cfg.type.MichelsonBoolType;

public class MichelsonMem extends it.unive.lisa.program.cfg.statement.BinaryExpression implements StackConsumer, StackProducer {

	public MichelsonMem(CFG cfg, String sourceFile, int line, int col, Expression left, Expression right) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "MEM", MichelsonBoolType.INSTANCE, left, right);
	}

	@Override
	public <A extends AbstractState<A, H, V, T>, H extends HeapDomain<H>, V extends ValueDomain<V>, T extends TypeDomain<T>> AnalysisState<A, H, V, T> binarySemantics(
			InterproceduralAnalysis<A, H, V, T> interprocedural, AnalysisState<A, H, V, T> state,
			SymbolicExpression left, SymbolicExpression right, StatementStore<A, H, V, T> expressions)
			throws SemanticException {
		BinaryOperator opt = new BinaryOperator() {

			@Override
			public Set<Type> typeInference(TypeSystem types, Set<Type> left, Set<Type> right) {
				return Set.of(MichelsonBoolType.INSTANCE);
			}
		};
		return state.smallStepSemantics(new BinaryExpression(MichelsonBoolType.INSTANCE, left, right, opt, getLocation()), this);
	}
}
