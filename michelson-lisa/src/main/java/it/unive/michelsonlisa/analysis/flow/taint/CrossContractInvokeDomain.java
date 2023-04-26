package it.unive.michelsonlisa.analysis.flow.taint;

import it.unive.lisa.analysis.Lattice;
import it.unive.lisa.analysis.SemanticException;
import it.unive.lisa.analysis.nonrelational.value.BaseNonRelationalValueDomain;
import it.unive.lisa.analysis.representation.DomainRepresentation;
import it.unive.lisa.analysis.representation.StringRepresentation;
import it.unive.lisa.program.annotations.Annotation;
import it.unive.lisa.program.annotations.Annotations;
import it.unive.lisa.program.annotations.matcher.AnnotationMatcher;
import it.unive.lisa.program.annotations.matcher.BasicAnnotationMatcher;
import it.unive.lisa.program.cfg.ProgramPoint;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.symbolic.value.Constant;
import it.unive.lisa.symbolic.value.Identifier;
import it.unive.lisa.symbolic.value.PushAny;
import it.unive.lisa.symbolic.value.operator.binary.BinaryOperator;
import it.unive.lisa.symbolic.value.operator.ternary.TernaryOperator;
import it.unive.lisa.symbolic.value.operator.unary.UnaryOperator;
import it.unive.lisa.type.Type;

/**
 * The taint domain, used for the taint analysis.
 */
public class CrossContractInvokeDomain implements BaseNonRelationalValueDomain<CrossContractInvokeDomain> {

	/**
	 * The annotation Tainted.
	 */
	public static final Annotation TAINTED_ANNOTATION = new Annotation("lisa.taint.Tainted");

	/**
	 * The matcher for the Tainted annotation.
	 */
	private static final AnnotationMatcher TAINTED_MATCHER = new BasicAnnotationMatcher(TAINTED_ANNOTATION);

	/**
	 * The annotation Clean.
	 */
	public static final Annotation CLEAN_ANNOTATION = new Annotation("lisa.taint.Clean");

	/**
	 * The matcher for the Clean annotation.
	 */
	private static final AnnotationMatcher CLEAN_MATCHER = new BasicAnnotationMatcher(CLEAN_ANNOTATION);

	/**
	 * The top state.
	 */
	private static final CrossContractInvokeDomain TOP = new CrossContractInvokeDomain((byte) 3);

	/**
	 * The top state.
	 */
	private static final CrossContractInvokeDomain TAINTED = new CrossContractInvokeDomain((byte) 2);

	/**
	 * The clean state.
	 */
	private static final CrossContractInvokeDomain CLEAN = new CrossContractInvokeDomain((byte) 1);

	/**
	 * The bottom state.
	 */
	private static final CrossContractInvokeDomain BOTTOM = new CrossContractInvokeDomain((byte) 0);

	private final byte v;

	/**
	 * Builds a new instance of taint, referring to the top element of the
	 * lattice.
	 */
	public CrossContractInvokeDomain() {
		this((byte) 3);
	}

	private CrossContractInvokeDomain(byte v) {
		this.v = v;
	}

	@Override
	public CrossContractInvokeDomain variable(Identifier id, ProgramPoint pp) throws SemanticException {

		Annotations annots = id.getAnnotations();
		if (annots.isEmpty())
			return BaseNonRelationalValueDomain.super.variable(id, pp);

		if (annots.contains(TAINTED_MATCHER))
			return TAINTED;

		if (annots.contains(CLEAN_MATCHER))
			return CLEAN;

		return BaseNonRelationalValueDomain.super.variable(id, pp);
	}

	@Override
	public DomainRepresentation representation() {
		return this == BOTTOM ? Lattice.bottomRepresentation()
				: this == CLEAN ? new StringRepresentation("_")
						: this == TAINTED ? new StringRepresentation("#") : Lattice.topRepresentation();
	}

	@Override
	public CrossContractInvokeDomain top() {
		return TOP;
	}

	@Override
	public CrossContractInvokeDomain bottom() {
		return BOTTOM;
	}

	/**
	 * Yields if the state is tainted.
	 * 
	 * @return {@code true} if is tainted, otherwise {@code false}
	 */
	public boolean isTainted() {
		return this == TAINTED;
	}

	public boolean isClean() {
		return this == CLEAN;
	}

	@Override
	public CrossContractInvokeDomain evalNullConstant(ProgramPoint pp) throws SemanticException {
		return CLEAN;
	}

	@Override
	public CrossContractInvokeDomain evalNonNullConstant(Constant constant, ProgramPoint pp) throws SemanticException {
		if (constant instanceof Tainted)
			return TAINTED;
		return CLEAN;
	}

	@Override
	public CrossContractInvokeDomain evalUnaryExpression(UnaryOperator operator, CrossContractInvokeDomain arg,
			ProgramPoint pp)
			throws SemanticException {
		return arg;
	}

	@Override
	public CrossContractInvokeDomain evalBinaryExpression(BinaryOperator operator, CrossContractInvokeDomain left,
			CrossContractInvokeDomain right,
			ProgramPoint pp) throws SemanticException {

		if (left == TAINTED || right == TAINTED)
			return TAINTED;

		if (left == TOP || right == TOP)
			return TOP;

		return CLEAN;
	}

	@Override
	public CrossContractInvokeDomain evalTernaryExpression(TernaryOperator operator, CrossContractInvokeDomain left,
			CrossContractInvokeDomain middle,
			CrossContractInvokeDomain right, ProgramPoint pp) throws SemanticException {
		if (left == TAINTED || right == TAINTED || middle == TAINTED)
			return TAINTED;

		if (left == TOP || right == TOP || middle == TOP)
			return TOP;

		return CLEAN;
	}

	@Override
	public CrossContractInvokeDomain evalPushAny(PushAny pushAny, ProgramPoint pp) throws SemanticException {
		return TAINTED;
	}

	@Override
	public boolean tracksIdentifiers(Identifier id) {
		for (Type t : id.getRuntimeTypes(null))
			if (!(t.isInMemoryType()))
				return true;
		return false;
	}

	@Override
	public boolean canProcess(SymbolicExpression expression) {
		return true;
	}

	@Override
	public CrossContractInvokeDomain lubAux(CrossContractInvokeDomain other) throws SemanticException {
		return TOP; // should never happen
	}

	@Override
	public CrossContractInvokeDomain wideningAux(CrossContractInvokeDomain other) throws SemanticException {
		return TOP; // should never happen
	}

	@Override
	public boolean lessOrEqualAux(CrossContractInvokeDomain other) throws SemanticException {
		return false; // should never happen
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + v;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CrossContractInvokeDomain other = (CrossContractInvokeDomain) obj;
		if (v != other.v)
			return false;
		return true;
	}
}