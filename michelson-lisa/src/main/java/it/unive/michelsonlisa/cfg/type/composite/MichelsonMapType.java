package it.unive.michelsonlisa.cfg.type.composite;

import java.util.Collection;
import java.util.HashSet;
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
 * MAP_TYPE type of Michelson language. This is the only map type available for Michelson. It
 * implements the singleton design pattern, that is the instances of this type
 * are unique. The unique instance of this type can be retrieved by
 * {@link MichelsonMapType#INSTANCE}.
 * 
 * @author <a href="mailto:luca.olivieri@univr.it">Luca Olivieri</a>
 */
public class MichelsonMapType implements InMemoryType, MichelsonType, MichelsonPassableType, MichelsonStorableType, MichelsonPushableType, MichelsonPackableType, MichelsonBigMapValueType, MichelsonDuplicableType {
	
	private Type keyType;
	private Type elementType;

	private static final Set<MichelsonType> mapTypes = new HashSet<>();

	public static MichelsonType lookup(MichelsonType type) {
		if (!mapTypes.contains(type))
			mapTypes.add(type);
		return mapTypes.stream().filter(x -> x.equals(type)).findFirst().get();
	}

	public MichelsonMapType(MichelsonComparableType keyType, Type elementType) {
		this.keyType = keyType;
		this.elementType = elementType;
	}
	
	public MichelsonMapType(Untyped keyType, Type elementType) {
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
			return keyType.canBeAssignedTo(((MichelsonMapType) other).keyType)
					&& elementType.canBeAssignedTo(((MichelsonMapType) other).elementType);
		return other.isUntyped();
	}

	@Override
	public Type commonSupertype(Type other) {
		if (other instanceof MichelsonMapType)
			if (keyType.canBeAssignedTo(((MichelsonMapType) other).keyType)
					&& elementType.canBeAssignedTo(((MichelsonMapType) other).elementType))
				return other;
		return Untyped.INSTANCE;
	}

	@Override
	public String toString() {
		return "MAP[" + keyType + "]" + elementType;
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
		MichelsonMapType other = (MichelsonMapType) obj;
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
	public Set<Type> allInstances(TypeSystem types) {
		Set<Type> instances = new HashSet<>();
		for (MichelsonType in : mapTypes)
			instances.add(in);
		return instances;
	}

	public static void clearAll() {
		mapTypes.clear();
	}
}