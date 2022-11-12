package it.unive.michelsonlisa.cfg.type.composite;

import java.util.Collection;
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
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonPassableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonStorableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonType;

/**
 * TICKET_TYPE type of Michelson language. This is the only ticket type available for Michelson. It
 * implements the singleton design pattern, that is the instances of this type
 * are unique. The unique instance of this type can be retrieved by
 * {@link MichelsonTicketType#INSTANCE}.
 * 
 * @author <a href="mailto:luca.olivieri@univr.it">Luca Olivieri</a>
 */
public class MichelsonTicketType implements InMemoryType, MichelsonType, MichelsonPassableType, MichelsonStorableType, MichelsonBigMapValueType {

	private Type informationType;

	public MichelsonTicketType(MichelsonComparableType informationType) {
		this.informationType = informationType;
	}
	
	public MichelsonTicketType(Untyped informationType) {
		this.informationType = informationType;
	}

	public Type getInformationType() {
		return informationType;
	}

	@Override
	public boolean canBeAssignedTo(Type other) {
		if (other instanceof MichelsonTicketType)
			return informationType.canBeAssignedTo(((MichelsonTicketType) other).informationType);
		return other.isUntyped();
	}

	@Override
	public Type commonSupertype(Type other) {
		if (other instanceof MichelsonTicketType)
			if (informationType.canBeAssignedTo(((MichelsonTicketType) other).informationType))
				return other;
		return Untyped.INSTANCE;
	}

	@Override
	public String toString() {
		return "TICKET " + informationType;
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
		result = prime * result + ((informationType == null) ? 0 : informationType.hashCode());
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
		MichelsonTicketType other = (MichelsonTicketType) obj;
		if (informationType == null) {
			if (other.informationType != null)
				return false;
		} else if (!informationType.equals(other.informationType))
			return false;
		return true;
	}
	
	
}
