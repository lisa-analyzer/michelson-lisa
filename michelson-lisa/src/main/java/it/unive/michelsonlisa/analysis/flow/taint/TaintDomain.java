package it.unive.michelsonlisa.analysis.flow.taint;

import it.unive.lisa.analysis.Lattice;
import it.unive.lisa.analysis.SemanticException;
import it.unive.lisa.analysis.nonrelational.value.BaseNonRelationalValueDomain;
import it.unive.lisa.analysis.representation.DomainRepresentation;
import it.unive.lisa.analysis.representation.StringRepresentation;
import it.unive.lisa.program.annotations.Annotation;
import it.unive.lisa.program.annotations.matcher.AnnotationMatcher;
import it.unive.lisa.program.annotations.matcher.BasicAnnotationMatcher;
import it.unive.lisa.program.cfg.ProgramPoint;
import it.unive.lisa.program.cfg.statement.Assignment;
import it.unive.lisa.symbolic.SymbolicExpression;
import it.unive.lisa.symbolic.value.Constant;
import it.unive.lisa.symbolic.value.Identifier;
import it.unive.lisa.symbolic.value.PushAny;
import it.unive.lisa.symbolic.value.operator.binary.BinaryOperator;
import it.unive.lisa.symbolic.value.operator.ternary.TernaryOperator;
import it.unive.lisa.symbolic.value.operator.unary.UnaryOperator;
import it.unive.michelsonlisa.cfg.statement.instrumentation.MichelsonParameterAndStore;

/**
 * The taint domain, used for the taint analysis.
 */
public class TaintDomain extends BaseNonRelationalValueDomain<TaintDomain> {

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
	private static final TaintDomain TOP = new TaintDomain((byte) 3);

	/**
	 * The top state.
	 */
	private static final TaintDomain TAINTED = new TaintDomain((byte) 2);

	/**
	 * The clean state.
	 */
	private static final TaintDomain CLEAN = new TaintDomain((byte) 1);

	/**
	 * The bottom state.
	 */
	private static final TaintDomain BOTTOM = new TaintDomain((byte) 0);

	private final byte v;

	/**
	 * Builds a new instance of taint, referring to the top element of the lattice.
	 */
	public TaintDomain() {
		this((byte) 3);
	}

	private TaintDomain(byte v) {
		this.v = v;
	}
		
	@Override
	public TaintDomain variable(Identifier id, ProgramPoint pp) throws SemanticException {

		if(pp instanceof Assignment && ((Assignment) pp).getRight() instanceof MichelsonParameterAndStore) {
			return TAINTED;
		}
		return super.variable(id, pp);
	}

	@Override
	public DomainRepresentation representation() {
		return this == BOTTOM ? Lattice.bottomRepresentation()
				: this == CLEAN ? new StringRepresentation("_")
						: this == TAINTED ? new StringRepresentation("#") : Lattice.topRepresentation();
	}

	@Override
	public TaintDomain top() {
		return TOP;
	}

	@Override
	public TaintDomain bottom() {
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
	protected TaintDomain evalNullConstant(ProgramPoint pp) throws SemanticException {
		return CLEAN;
	}

	@Override
	protected TaintDomain evalNonNullConstant(Constant constant, ProgramPoint pp) throws SemanticException {
		if (constant instanceof Tainted)
			return TAINTED;
		return CLEAN;
	}

	@Override
	protected TaintDomain evalUnaryExpression(UnaryOperator operator, TaintDomain arg, ProgramPoint pp)
			throws SemanticException {
		return arg;
	}

	@Override
	protected TaintDomain evalBinaryExpression(BinaryOperator operator, TaintDomain left, TaintDomain right,
			ProgramPoint pp) throws SemanticException {
		
		if (left == TAINTED || right == TAINTED)
			return TAINTED;

		if (left == TOP || right == TOP)
			return TOP;

		return CLEAN;
	}

	@Override
	protected TaintDomain evalTernaryExpression(TernaryOperator operator, TaintDomain left, TaintDomain middle,
			TaintDomain right, ProgramPoint pp) throws SemanticException {
		if (left == TAINTED || right == TAINTED || middle == TAINTED)
			return TAINTED;

		if (left == TOP || right == TOP || middle == TOP)
			return TOP;

		return CLEAN;
	}

	@Override
	protected TaintDomain evalPushAny(PushAny pushAny, ProgramPoint pp) throws SemanticException {
 		return TAINTED;
	}

	@Override
	public boolean tracksIdentifiers(Identifier id) {
		return true;
	}

	@Override
	public boolean canProcess(SymbolicExpression expression) {
		return true;
	}

	@Override
	protected TaintDomain lubAux(TaintDomain other) throws SemanticException {
		return TOP; // should never happen
	}

	@Override
	protected TaintDomain wideningAux(TaintDomain other) throws SemanticException {
		return TOP; // should never happen
	}

	@Override
	protected boolean lessOrEqualAux(TaintDomain other) throws SemanticException {
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
		TaintDomain other = (TaintDomain) obj;
		if (v != other.v)
			return false;
		return true;
	}
}