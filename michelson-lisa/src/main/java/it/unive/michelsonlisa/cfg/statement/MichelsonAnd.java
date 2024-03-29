package it.unive.michelsonlisa.cfg.statement;

import it.unive.lisa.analysis.AbstractState;
import it.unive.lisa.analysis.AnalysisState;
import it.unive.lisa.analysis.SemanticException;
import it.unive.lisa.analysis.StatementStore;
import it.unive.lisa.interprocedural.InterproceduralAnalysis;
import it.unive.lisa.program.SourceCodeLocation;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.statement.BinaryExpression;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.symbolic.value.PushAny;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.Untyped;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackConsumer;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackProducer;
import it.unive.michelsonlisa.cfg.type.MichelsonBoolType;
import it.unive.michelsonlisa.cfg.type.MichelsonIntType;
import it.unive.michelsonlisa.cfg.type.MichelsonNatType;

public class MichelsonAnd extends BinaryExpression
		implements StackConsumer, StackProducer, MichelsonBinaryNumericalOperation {

	public MichelsonAnd(CFG cfg, String sourceFile, int line, int col, Expression left, Expression right) {
		super(cfg, new SourceCodeLocation(sourceFile, line, col), "AND",
				computeType(left.getStaticType(), right.getStaticType()), left, right);
	}

	private static Type computeType(Type t1, Type t2) {

		if (!(t1 instanceof MichelsonNatType || t1 instanceof MichelsonIntType || t1 instanceof MichelsonBoolType
				|| t1.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The left value of type " + t1 + " cannot used in AND");

		if (!(t2 instanceof MichelsonNatType || t2 instanceof MichelsonIntType || t2 instanceof MichelsonBoolType
				|| t2.equals(Untyped.INSTANCE)))
			throw new IllegalArgumentException("The left value of type " + t1 + " cannot used in AND");

		if (t1.equals(t2))
			return t1;

		if (t1 instanceof MichelsonIntType && t2 instanceof MichelsonNatType)
			return MichelsonNatType.INSTANCE;

		if (t1.equals(Untyped.INSTANCE) || t2.equals(Untyped.INSTANCE))
			return Untyped.INSTANCE;
		else
			throw new IllegalArgumentException("The operation AND cannot used with the types  " + t1 + " : " + t2);
	}

	@Override
	public <A extends AbstractState<A>> AnalysisState<A> fwdBinarySemantics(InterproceduralAnalysis<A> interprocedural,
			AnalysisState<A> state, SymbolicExpression left, SymbolicExpression right, StatementStore<A> expressions)
			throws SemanticException {
		AnalysisState<A> result = state.top();
		Type leftType = left.getStaticType();
		Type rightType = right.getStaticType();

		if ((leftType.isUntyped() || (leftType.isNumericType() && leftType.asNumericType().isIntegral()))
				&& (rightType.isUntyped() || (rightType.isNumericType() && rightType.asNumericType().isIntegral()))) {
			// TODO: LiSA has not symbolic expression handling bitwise,
			// return top at the moment
			AnalysisState<A> tmp = state
					.smallStepSemantics(new PushAny(resultType(leftType, rightType), getLocation()), this);
			result = result.lub(tmp);
		}
		return result;
	}
}
