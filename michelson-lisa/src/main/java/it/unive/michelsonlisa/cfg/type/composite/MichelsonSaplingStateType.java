package it.unive.michelsonlisa.cfg.type.composite;

import java.util.Collections;
import java.util.Set;

import it.unive.lisa.program.SourceCodeLocation;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.type.InMemoryType;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.TypeSystem;
import it.unive.lisa.type.Untyped;
import it.unive.michelsonlisa.cfg.expression.literal.MichelsonNaturalData;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonDuplicableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonPassableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonStorableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonType;

/**
 * SAPLING_STATE_TYPE type of Michelson language. This is the only SAPLING_STATE_TYPE type available for Michelson. It
 * implements the singleton design pattern, that is the instances of this type
 * are unique. The unique instance of this type can be retrieved by
 * {@link MichelsonSaplingStateType#INSTANCE}.
 * 
 * @author <a href="mailto:luca.olivieri@univr.it">Luca Olivieri</a>
 */
public class MichelsonSaplingStateType implements InMemoryType, MichelsonType, MichelsonPassableType, MichelsonStorableType, MichelsonDuplicableType {

	private MichelsonNaturalData n;
	
	public MichelsonSaplingStateType(MichelsonNaturalData n) {
		this.n = n;
	}

	public MichelsonNaturalData getNat() {
		return n;
	}
	
	@Override
	public boolean canBeAssignedTo(Type other) {
		//if (other instanceof MichelsonSaplingStateType)
		//	return n.canBeAssignedTo(((MichelsonSaplingStateType) other).n);
		return other.isUntyped();
	}

	@Override
	public Type commonSupertype(Type other) {
		//if (other instanceof MichelsonSaplingStateType)
		//	if (n.canBeAssignedTo(((MichelsonSaplingStateType) other).n))
		//		return other;
		return Untyped.INSTANCE;
	}

	@Override
	public String toString() {
		return "SAMPLING_STATE_TYPE " + n;
	}

	@Override
	public Set<Type> allInstances(TypeSystem types) {
		return Collections.singleton(this);
	}

	@Override
	public Expression defaultValue(CFG cfg, SourceCodeLocation location) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((n == null) ? 0 : n.hashCode());
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
		MichelsonSaplingStateType other = (MichelsonSaplingStateType) obj;
		if (n == null) {
			if (other.n != null)
				return false;
		} else if (!n.equals(other.n))
			return false;
		return true;
	}
	
}
