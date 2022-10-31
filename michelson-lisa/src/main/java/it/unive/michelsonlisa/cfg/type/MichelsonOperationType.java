package it.unive.michelsonlisa.cfg.type;

import java.util.Collection;
import java.util.Collections;

import it.unive.lisa.program.SourceCodeLocation;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.Untyped;
import it.unive.michelsonlisa.cfg.expression.literal.MichelsonBooleanData;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonDuplicableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonType;

/**
 * OPERATION_TYPE type of Michelson language. This is the only operation type available for Michelson. It
 * implements the singleton design pattern, that is the instances of this type
 * are unique. The unique instance of this type can be retrieved by
 * {@link MichelsonOperationType#INSTANCE}.
 * 
 * @author <a href="mailto:luca.olivieri@univr.it">Luca Olivieri</a>
 */
public class MichelsonOperationType implements MichelsonType, MichelsonDuplicableType {

	/**
	 * Unique instance of MichelsonOperationType.
	 */
	public static final MichelsonOperationType INSTANCE = new MichelsonOperationType();

	private MichelsonOperationType() {
	}

	@Override
	public String toString() {
		return "OPERATION_TYPE";
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
		return other instanceof MichelsonOperationType || other.isUntyped();
	}

	@Override
	public Type commonSupertype(Type other) {
		return other instanceof MichelsonOperationType ? this : Untyped.INSTANCE;
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
