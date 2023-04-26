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
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonDuplicableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonPackableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonPassableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonType;

/**
 * CONTRACT_TYPE type of Michelson language. This is the only contract type available for Michelson. It
 * implements the singleton design pattern, that is the instances of this type
 * are unique. The unique instance of this type can be retrieved by
 * {@link MichelsonContractType#INSTANCE}.
 * 
 * @author <a href="mailto:luca.olivieri@univr.it">Luca Olivieri</a>
 */
public class MichelsonContractType implements InMemoryType, MichelsonType, MichelsonPassableType, MichelsonPackableType, MichelsonDuplicableType {
	
	private Type parameterType;

	public MichelsonContractType(Type parameterType) {
		this.parameterType = parameterType;
	}

	public Type getParameterType() {
		return parameterType;
	}

	@Override
	public boolean canBeAssignedTo(Type other) {
		if (other instanceof MichelsonContractType)
			return parameterType.canBeAssignedTo(((MichelsonContractType) other).parameterType);
		return other.isUntyped();
	}

	@Override
	public Type commonSupertype(Type other) {
		if (other instanceof MichelsonContractType)
			if (parameterType.canBeAssignedTo(((MichelsonContractType) other).parameterType))
				return other;
		return Untyped.INSTANCE;
	}

	@Override
	public String toString() {
		return "CONTRACT " + parameterType;
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
		result = prime * result + ((parameterType == null) ? 0 : parameterType.hashCode());
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
		MichelsonContractType other = (MichelsonContractType) obj;
		if (parameterType == null) {
			if (other.parameterType != null)
				return false;
		} else if (!parameterType.equals(other.parameterType))
			return false;
		return true;
	}

}
