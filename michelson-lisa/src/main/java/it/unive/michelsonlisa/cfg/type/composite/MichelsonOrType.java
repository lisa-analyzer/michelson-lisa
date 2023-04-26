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
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonBigMapValueType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonComparableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonDuplicableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonPackableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonPassableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonPushableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonStorableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonType;

/**
 * OR_TYPE type of Michelson language. This is the only or type available for Michelson. It
 * implements the singleton design pattern, that is the instances of this type
 * are unique. The unique instance of this type can be retrieved by
 * {@link MichelsonOrType#INSTANCE}.
 * 
 * @author <a href="mailto:luca.olivieri@univr.it">Luca Olivieri</a>
 */
public class MichelsonOrType implements InMemoryType, MichelsonType, MichelsonComparableType, MichelsonPassableType, MichelsonStorableType, MichelsonPushableType, MichelsonPackableType, MichelsonBigMapValueType, MichelsonDuplicableType {


	private Type leftType;
	private Type rightType;
	
	public MichelsonOrType(Type leftType, Type rightType) {
		this.leftType = leftType;
		this.rightType = rightType;
	}
	
	public MichelsonOrType(Type[] types) {
		if(types.length != 2)
			throw new IllegalArgumentException("the parameter rightType must contain 2 element");
		this.leftType = types[0];
		this.rightType = types[1];
	}

	public Type getRightType() {
		return rightType;
	}
	
	public Type getLeftType() {
		return leftType;
	}
	
	@Override
	public boolean canBeAssignedTo(Type other) {
		if (other instanceof MichelsonOrType)
			return rightType.canBeAssignedTo(((MichelsonOrType) other).rightType)
					&& leftType.canBeAssignedTo(((MichelsonOrType) other).leftType);
		return other.isUntyped();
	}

	@Override
	public Type commonSupertype(Type other) {
		if (other instanceof MichelsonOrType)
			if (rightType.canBeAssignedTo(((MichelsonOrType) other).rightType)
					&& leftType.canBeAssignedTo(((MichelsonOrType) other).leftType))
				return other;
		return Untyped.INSTANCE;
	}

	@Override
	public String toString() {
		return "OR " + leftType + " " + rightType;
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
		result = prime * result + ((leftType == null) ? 0 : leftType.hashCode());
		result = prime * result + ((rightType == null) ? 0 : rightType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj.getClass().isAssignableFrom(getClass()) || getClass().isAssignableFrom(obj.getClass())))
			return false;
		MichelsonOrType other = (MichelsonOrType) obj;
		if (leftType == null) {
			if (other.leftType != null)
				return false;
		} else if (!leftType.equals(other.leftType))
			return false;
		if (rightType == null) {
			if (other.rightType != null)
				return false;
		} else if (!rightType.equals(other.rightType))
			return false;
		return true;
	}
	
	
}
