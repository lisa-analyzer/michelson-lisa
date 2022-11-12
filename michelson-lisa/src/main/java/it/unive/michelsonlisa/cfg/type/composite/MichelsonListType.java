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
import it.unive.lisa.type.TypeSystem;
import it.unive.lisa.type.Untyped;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonBigMapValueType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonDuplicableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonPackableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonPassableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonPushableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonStorableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonType;

/**
 * LIST_TYPE type of Michelson language. This is the only list type available for Michelson. It
 * implements the singleton design pattern, that is the instances of this type
 * are unique. The unique instance of this type can be retrieved by
 * {@link MichelsonListType#INSTANCE}.
 * 
 * @author <a href="mailto:luca.olivieri@univr.it">Luca Olivieri</a>
 */
public class MichelsonListType implements InMemoryType, MichelsonType, MichelsonPassableType, MichelsonStorableType, MichelsonPushableType, MichelsonPackableType, MichelsonBigMapValueType, MichelsonDuplicableType {

	private Type contentType;

	private static final Set<MichelsonListType> listTypes = new HashSet<>();

	public static MichelsonListType lookup(MichelsonListType type) {
		if (!listTypes.contains(type))
			listTypes.add(type);

		return listTypes.stream().filter(x -> x.equals(type)).findFirst().get();
	}

	public MichelsonListType(Type contentType) {
		this.contentType = contentType;
	}

	public Type getContentType() {
		return contentType;
	}
	
	@Override
	public boolean canBeAssignedTo(Type other) {
		if (other instanceof MichelsonListType)
			return contentType.canBeAssignedTo(((MichelsonListType) other).contentType);
		return false;
	}

	@Override
	public Type commonSupertype(Type other) {
		if (other instanceof MichelsonListType)
			if (contentType.canBeAssignedTo(((MichelsonListType) other).contentType))
				return other;
		return Untyped.INSTANCE;
	}

	@Override
	public String toString() {
		return "LIST " + contentType.toString();
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
		MichelsonListType other = (MichelsonListType) obj;
		if (contentType == null) {
			if (other.contentType != null)
				return false;
		} else if (!contentType.equals(other.contentType))
			return false;
		return true;
	}

	@Override
	public Expression defaultValue(CFG cfg, SourceCodeLocation location) {
	/*	List<Expression> result = new ArrayList<Expression>();
		for (int i = 0; i < result.size(); i++)
			if (contentType instanceof MichelsonType)
				result.set(i, ((MichelsonType) contentType).defaultValue(cfg, location));
			else
				result.set(i,  new MichelsonUnknown(cfg, location));

		
		*/
		return null;
	}

	public static Collection<Type> all() {
		Collection<Type> instances = new HashSet<>();
		for (MichelsonListType in : listTypes)
			instances.add(in);
		return instances;
	}

	@Override
	public Set<Type> allInstances(TypeSystem types) {
		return Collections.singleton(this);
	}

	public static void clearAll() {
		listTypes.clear();
	}

}
