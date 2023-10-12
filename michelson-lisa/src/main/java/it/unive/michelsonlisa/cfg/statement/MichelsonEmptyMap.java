package it.unive.michelsonlisa.cfg.statement;

import it.unive.lisa.analysis.AbstractState;
import it.unive.lisa.analysis.AnalysisState;
import it.unive.lisa.analysis.SemanticException;
import it.unive.lisa.analysis.StatementStore;
import it.unive.lisa.analysis.lattices.ExpressionSet;
import it.unive.lisa.interprocedural.InterproceduralAnalysis;
import it.unive.lisa.program.SourceCodeLocation;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.statement.NaryExpression;
import it.unive.lisa.symbolic.value.Constant;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.Untyped;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackProducer;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonMapType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonComparableType;

public class MichelsonEmptyMap extends NaryExpression implements StackProducer{

	public MichelsonEmptyMap(CFG cfg, String sourceFile, int line, int col, Type keyType, Type valueType) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "EMPTY_MAP", computeType(keyType, valueType));
	}

	private static Type computeType(Type keyType, Type valueType) {
		
		if(keyType instanceof MichelsonComparableType)
			return new MichelsonMapType((MichelsonComparableType) keyType, valueType);
		
		return new MichelsonMapType(Untyped.INSTANCE, valueType);
	}

	@Override
	public <A extends AbstractState<A>> AnalysisState<A> forwardSemanticsAux(InterproceduralAnalysis<A> interprocedural,
			AnalysisState<A> state, ExpressionSet[] params, StatementStore<A> expressions) throws SemanticException {
		return state.smallStepSemantics(new Constant(getStaticType(), "EMPTY_MAP_VALUE", getLocation()), this);

	}



}
