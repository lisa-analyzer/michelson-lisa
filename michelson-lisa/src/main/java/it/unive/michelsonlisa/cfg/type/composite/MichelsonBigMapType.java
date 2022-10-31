package it.unive.michelsonlisa.cfg.type.composite;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import it.unive.lisa.program.SourceCodeLocation;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.type.InMemoryType;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.Untyped;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonComparableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonDuplicableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonPassableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonStorableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonType;

/**
 * BIG_MAP_TYPE type of Michelson language. This is the only big map type available for Michelson. It
 * implements the singleton design pattern, that is the instances of this type
 * are unique. The unique instance of this type can be retrieved by
 * {@link MichelsonBigMapType#INSTANCE}.
 * 
 * @author <a href="mailto:luca.olivieri@univr.it">Luca Olivieri</a>
 */
public class MichelsonBigMapType implements InMemoryType, MichelsonType, MichelsonPassableType, MichelsonStorableType, MichelsonDuplicableType {
	
	private Type keyType;
	private Type elementType;

	private static final Set<MichelsonType> mapTypes = new HashSet<>();

	public static MichelsonType lookup(MichelsonType type) {
		if (!mapTypes.contains(type))
			mapTypes.add(type);
		return mapTypes.stream().filter(x -> x.equals(type)).findFirst().get();
	}

	public MichelsonBigMapType(MichelsonComparableType keyType, Type elementType) {
		this.keyType = keyType;
		this.elementType = elementType;
	}
	
	public MichelsonBigMapType(Untyped keyType, Type elementType) {
		this.keyType = keyType;
		this.elementType = elementType;
	}

	public Type getKeyType() {
		return keyType;
	}

	public Type getElementType() {
		return elementType;
	}

	@Override
	public boolean canBeAssignedTo(Type other) {
		if (other instanceof MichelsonMapType)
			return keyType.canBeAssignedTo(((MichelsonBigMapType) other).keyType)
					&& elementType.canBeAssignedTo(((MichelsonBigMapType) other).elementType);
		return other.isUntyped();
	}

	@Override
	public Type commonSupertype(Type other) {
		if (other instanceof MichelsonMapType)
			if (keyType.canBeAssignedTo(((MichelsonBigMapType) other).keyType)
					&& elementType.canBeAssignedTo(((MichelsonBigMapType) other).elementType))
				return other;
		return Untyped.INSTANCE;
	}

	@Override
	public String toString() {
		return "BIG_MAP_TYPE[" + keyType + "]" + elementType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((elementType == null) ? 0 : elementType.hashCode());
		result = prime * result + ((keyType == null) ? 0 : keyType.hashCode());
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
		MichelsonBigMapType other = (MichelsonBigMapType) obj;
		if (elementType == null) {
			if (other.elementType != null)
				return false;
		} else if (!elementType.equals(other.elementType))
			return false;
		if (keyType == null) {
			if (other.keyType != null)
				return false;
		} else if (!keyType.equals(other.keyType))
			return false;
		return true;
	}

	@Override
	public boolean isPointerType() {
		return false;
	}

	@Override
	public Expression defaultValue(CFG cfg, SourceCodeLocation location) {
		return null;
	}

	public static Collection<Type> all() {
		Collection<Type> instances = new HashSet<>();
		for (MichelsonType in : mapTypes)
			instances.add(in);
		return instances;
	}

	@Override
	public Collection<Type> allInstances() {
		Collection<Type> instances = new HashSet<>();
		for (MichelsonType in : mapTypes)
			instances.add(in);
		return instances;
	}

	public static void clearAll() {
		mapTypes.clear();
	}
}