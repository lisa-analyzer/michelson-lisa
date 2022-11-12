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
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.symbolic.value.UnaryExpression;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.Untyped;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackConsumer;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackProducer;
import it.unive.michelsonlisa.cfg.type.MichelsonBoolType;
import it.unive.michelsonlisa.cfg.type.MichelsonIntType;
import it.unive.michelsonlisa.symblic.value.operator.unary.MichelsonComparisonGt;

public class MichelsonGt extends it.unive.lisa.program.cfg.statement.UnaryExpression implements StackConsumer, StackProducer{

	public MichelsonGt(CFG cfg, String sourceFile, int line, int col, Expression expr) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "GT", MichelsonBoolType.INSTANCE, expr);
		checkParameterType(expr.getStaticType());	
	}

	private void checkParameterType(Type type) {
		if(!(type instanceof MichelsonIntType || type.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The value of type "+type+" cannot used in GT");
	}
	
	@Override
	public <A extends AbstractState<A, H, V, T>, H extends HeapDomain<H>, V extends ValueDomain<V>, T extends TypeDomain<T>> AnalysisState<A, H, V, T> unarySemantics(
			InterproceduralAnalysis<A, H, V, T> interprocedural, AnalysisState<A, H, V, T> state,
			SymbolicExpression expr, StatementStore<A, H, V, T> expressions) throws SemanticException {

		return state.smallStepSemantics(new UnaryExpression(MichelsonBoolType.INSTANCE,
						expr, MichelsonComparisonGt.INSTANCE, getLocation()), this);
		
	}
}
