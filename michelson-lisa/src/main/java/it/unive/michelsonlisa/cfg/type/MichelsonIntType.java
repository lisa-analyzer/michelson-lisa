package it.unive.michelsonlisa.cfg.type;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import it.unive.lisa.program.SourceCodeLocation;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.TypeSystem;
import it.unive.lisa.type.Untyped;
import it.unive.michelsonlisa.cfg.expression.literal.MichelsonBooleanData;

/**
 * Boolean type of Michelson language. This is the only int type available for Michelson. It
 * implements the singleton design pattern, that is the instances of this type
 * are unique. The unique instance of this type can be retrieved by
 * {@link MichelsonIntType#INSTANCE}.
 * 
 * @author <a href="mailto:luca.olivieri@univr.it">Luca Olivieri</a>
 */
public class MichelsonIntType extends MichelsonNatType {

	/**
	 * Unique instance of MichelsonIntType.
	 */
	public static final MichelsonIntType INSTANCE = new MichelsonIntType();

	private MichelsonIntType() {
	}

	@Override
	public String toString() {
		return "INT_TYPE";
	}

	@Override
	public boolean equals(Object other) {
		return this == other;
	}

	@Override
	public int hashCode() {
		return System.identityHashCode(this);
	}

	@Override
	public boolean canBeAssignedTo(Type other) {
		return other instanceof MichelsonIntType || other.isUntyped();
	}

	@Override
	public Type commonSupertype(Type other) {
		return other instanceof MichelsonIntType ? this : Untyped.INSTANCE;
	}

	@Override
	public Expression defaultValue(CFG cfg, SourceCodeLocation location) {
		return new MichelsonBooleanData(cfg, location, false);
	}

	@Override
	public Set<Type> allInstances(TypeSystem types) {
		return Collections.singleton(this);
	}

	@Override
	public boolean is8Bits() {
		return false;
	}

	@Override
	public boolean is16Bits() {
		return false;
	}

	@Override
	public boolean is32Bits() {
		return false;
	}

	@Override
	public boolean is64Bits() {
		return false;
	}

	@Override
	public boolean isUnsigned() {
		return false;
	}

	@Override
	public boolean isIntegral() {
		return true;
	}

}
