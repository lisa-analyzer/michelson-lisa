package it.unive.michelsonlisa.cfg.type;

import java.util.Collection;
import java.util.Collections;

import it.unive.lisa.program.SourceCodeLocation;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.Untyped;
import it.unive.michelsonlisa.cfg.expression.literal.MichelsonBooleanData;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonBigMapValueType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonDuplicableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonPackableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonPassableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonPushableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonStorableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonType;

/**
 * BLS12_381_FR type of Michelson language. This is the only BLS12_381_FR type available for Michelson. It
 * implements the singleton design pattern, that is the instances of this type
 * are unique. The unique instance of this type can be retrieved by
 * {@link MichelsonBLS12_381_FRType#INSTANCE}.
 * 
 * @author <a href="mailto:luca.olivieri@univr.it">Luca Olivieri</a>
 */
public class MichelsonBLS12_381_FRType implements  MichelsonType, MichelsonPassableType, MichelsonStorableType, MichelsonPushableType, MichelsonPackableType, MichelsonBigMapValueType, MichelsonDuplicableType {

	/**
	 * Unique instance of MichelsonBLS12_381_FRType.
	 */
	public static final MichelsonBLS12_381_FRType INSTANCE = new MichelsonBLS12_381_FRType();

	private MichelsonBLS12_381_FRType() {
	}

	@Override
	public String toString() {
		return "BLS12_381_FR_TYPE";
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
		return other instanceof MichelsonBLS12_381_FRType || other.isUntyped();
	}

	@Override
	public Type commonSupertype(Type other) {
		return other instanceof MichelsonBLS12_381_FRType ? this : Untyped.INSTANCE;
	}

	@Override
	public Expression defaultValue(CFG cfg, SourceCodeLocation location) {
		return new MichelsonBooleanData(cfg, location, false);
	}

	@Override
	public Collection<Type> allInstances() {
		return Collections.singleton(this);
	}
}
