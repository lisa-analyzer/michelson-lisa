package it.unive.michelsonlisa.cfg.type;

import java.util.Collection;
import java.util.Collections;

import it.unive.lisa.program.SourceCodeLocation;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.type.NumericType;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.Untyped;
import it.unive.michelsonlisa.cfg.expression.literal.MichelsonBooleanData;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonBigMapValueType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonComparableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonDuplicableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonPackableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonPassableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonPushableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonStorableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonType;

/**
 * Boolean type of Michelson language. This is the only natural number type available for Michelson. It
 * implements the singleton design pattern, that is the instances of this type
 * are unique. The unique instance of this type can be retrieved by
 * {@link MichelsonNatType#INSTANCE}.
 * 
 * @author <a href="mailto:luca.olivieri@univr.it">Luca Olivieri</a>
 */
public class MichelsonNatType implements NumericType, MichelsonType, MichelsonComparableType, MichelsonPassableType, MichelsonStorableType, MichelsonPushableType, MichelsonPackableType, MichelsonBigMapValueType, MichelsonDuplicableType {

	/**
	 * Unique instance of MichelsonNatType.
	 */
	public static final MichelsonNatType INSTANCE = new MichelsonNatType();

	protected MichelsonNatType() {
	}

	@Override
	public String toString() {
		return "NAT_TYPE";
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
		return other instanceof MichelsonNatType || other.isUntyped();
	}

	@Override
	public Type commonSupertype(Type other) {
		return other instanceof MichelsonNatType ? this : Untyped.INSTANCE;
	}

	@Override
	public Expression defaultValue(CFG cfg, SourceCodeLocation location) {
		return new MichelsonBooleanData(cfg, location, false);
	}

	@Override
	public Collection<Type> allInstances() {
		return Collections.singleton(this);
	}

	@Override
	public boolean is8Bits() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean is16Bits() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean is32Bits() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean is64Bits() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isUnsigned() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isIntegral() {
		// TODO Auto-generated method stub
		return false;
	}
}
