package it.unive.michelsonlisa.cfg.type.composite;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

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
 * SET_TYPE type of Michelson language. This is the only set type available for Michelson. It
 * implements the singleton design pattern, that is the instances of this type
 * are unique. The unique instance of this type can be retrieved by
 * {@link MichelsonSetType#INSTANCE}.
 * 
 * @author <a href="mailto:luca.olivieri@univr.it">Luca Olivieri</a>
 */
public class MichelsonSetType implements InMemoryType, MichelsonType, MichelsonPassableType, MichelsonStorableType, MichelsonPushableType, MichelsonPackableType, MichelsonBigMapValueType, MichelsonDuplicableType {
	
	private MichelsonComparableType contentType;

	private static final Set<MichelsonSetType> setTypes = new HashSet<>();

	public static MichelsonSetType lookup(MichelsonSetType type) {
		if (!setTypes.contains(type))
			setTypes.add(type);

		return setTypes.stream().filter(x -> x.equals(type)).findFirst().get();
	}

	public MichelsonSetType(MichelsonComparableType contentType) {
		this.contentType = contentType;
	}

	public Type getContentType() {
		return contentType;
	}
	
	@Override
	public boolean canBeAssignedTo(Type other) {
		if (other instanceof MichelsonSetType)
			return contentType.canBeAssignedTo(((MichelsonSetType) other).contentType);
		return false;
	}

	@Override
	public Type commonSupertype(Type other) {
		if (other instanceof MichelsonSetType)
			if (contentType.canBeAssignedTo(((MichelsonSetType) other).contentType))
				return other;
		return Untyped.INSTANCE;
	}

	@Override
	public String toString() {
		return "SET " + contentType.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contentType == null) ? 0 : contentType.hashCode());
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
		MichelsonSetType other = (MichelsonSetType) obj;
		if (contentType == null) {
			if (other.contentType != null)
				return false;
		} else if (!contentType.equals(other.contentType))
			return false;
		return true;
	}

	@Override
	public Expression defaultValue(CFG cfg, SourceCodeLocation location) {
		return null;
	}

	public static Collection<Type> all() {
		Collection<Type> instances = new HashSet<>();
		for (MichelsonSetType in : setTypes)
			instances.add(in);
		return instances;
	}

	@Override
	public Collection<Type> allInstances() {
		return Collections.singleton(this);
	}

	public static void clearAll() {
		setTypes.clear();
	}
	
	
}
