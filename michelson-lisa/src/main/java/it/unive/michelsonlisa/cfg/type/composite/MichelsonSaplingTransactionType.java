package it.unive.michelsonlisa.cfg.type.composite;

import java.util.Collection;
import java.util.Collections;

import it.unive.lisa.program.SourceCodeLocation;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.type.InMemoryType;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.Untyped;
import it.unive.michelsonlisa.cfg.expression.literal.MichelsonNaturalData;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonBigMapValueType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonDuplicableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonPackableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonPassableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonPushableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonStorableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonType;

/**
 * SAPLING_TRANSACTION_TYPE type of Michelson language. This is the only SAPLING_TRANSACTION_TYPE type available for Michelson. It
 * implements the singleton design pattern, that is the instances of this type
 * are unique. The unique instance of this type can be retrieved by
 * {@link MichelsonSaplingTransactionType#INSTANCE}.
 * 
 * @author <a href="mailto:luca.olivieri@univr.it">Luca Olivieri</a>
 */
public class MichelsonSaplingTransactionType implements InMemoryType, MichelsonType, MichelsonPassableType, MichelsonStorableType, MichelsonPushableType, MichelsonPackableType, MichelsonBigMapValueType, MichelsonDuplicableType {
	
	private MichelsonNaturalData n;
	
	public MichelsonSaplingTransactionType(MichelsonNaturalData n) {
		this.n = n;
	}

	public MichelsonNaturalData getNatural() {
		return n;
	}
	
	@Override
	public boolean canBeAssignedTo(Type other) {
		//if (other instanceof MichelsonSaplingTransactionType)
		//	return n.canBeAssignedTo(((MichelsonSaplingTransactionType) other).n);
		return other.isUntyped();
	}

	@Override
	public Type commonSupertype(Type other) {
		//if (other instanceof MichelsonSaplingTransactionType)
		//	if (n.canBeAssignedTo(((MichelsonSaplingTransactionType) other).n))
		//		return other;
		return Untyped.INSTANCE;
	}

	@Override
	public String toString() {
		return "SAMPLING_TRANSACTION_TYPE " + n;
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
		MichelsonSaplingTransactionType other = (MichelsonSaplingTransactionType) obj;
		if (n == null) {
			if (other.n != null)
				return false;
		} else if (!n.equals(other.n))
			return false;
		return true;
	}
	
}
