package it.unive.michelsonlisa.cfg.type.composite;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import it.unive.lisa.program.SourceCodeLocation;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.type.InMemoryType;
import it.unive.lisa.type.Type;
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
 * PAIR_TYPE type of Michelson language. This is the only pair type available for Michelson. It
 * implements the singleton design pattern, that is the instances of this type
 * are unique. The unique instance of this type can be retrieved by
 * {@link MichelsonPairType#INSTANCE}.
 * 
 * @author <a href="mailto:luca.olivieri@univr.it">Luca Olivieri</a>
 */
public class MichelsonPairType implements InMemoryType, MichelsonType, MichelsonComparableType, MichelsonPassableType, MichelsonStorableType, MichelsonPushableType, MichelsonPackableType, MichelsonBigMapValueType, MichelsonDuplicableType {

	final Type leftType;
	final Type rightType;

	public MichelsonPairType(Type ... elementTypes) {
		if(elementTypes.length < 2)
			throw new IllegalArgumentException("The Pair Type must be at least 2 types");
		this.leftType = elementTypes[0];
		this.rightType = buildType(elementTypes);
	}

	public MichelsonPairType() {
		leftType = Untyped.INSTANCE;
		rightType = Untyped.INSTANCE;
	}
	
	private Type buildType(Type[] elementTypes) {
		if(elementTypes.length == 2 )
			return elementTypes[1];
		else {
			return new MichelsonPairType(Arrays.copyOfRange(elementTypes, 1, elementTypes.length));
		}
	}

	@Override
	public boolean canBeAssignedTo(Type other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Type commonSupertype(Type other) {
		return Untyped.INSTANCE;
	}

	@Override
	public Collection<Type> allInstances() {
		return Collections.singleton(this);
	}

	@Override
	public Expression defaultValue(CFG cfg, SourceCodeLocation location) {
		// TODO Auto-generated method stub
		return null;
	}

	public Type getLeftType() {
		return leftType;
	}

	public Type getRightType() {
		return rightType;
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
		MichelsonPairType other = (MichelsonPairType) obj;
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

	@Override
	public String toString() {
		return "PAIR " + leftType + " " + rightType;
	}
	
	
	
}

	
