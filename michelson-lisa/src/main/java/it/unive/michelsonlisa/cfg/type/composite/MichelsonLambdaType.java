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
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonDuplicableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonPackableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonPassableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonPushableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonStorableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonType;

/**
 * LAMBDA_TYPE type of Michelson language. This is the only lambda type available for Michelson. It
 * implements the singleton design pattern, that is the instances of this type
 * are unique. The unique instance of this type can be retrieved by
 * {@link MichelsonLambdaType#INSTANCE}.
 * 
 * @author <a href="mailto:luca.olivieri@univr.it">Luca Olivieri</a>
 */
public class MichelsonLambdaType implements InMemoryType, MichelsonType, MichelsonPassableType, MichelsonStorableType, MichelsonPushableType, MichelsonPackableType, MichelsonBigMapValueType, MichelsonDuplicableType {

	private Type parameterType;
	private Type returnType;
	
	public MichelsonLambdaType(Type parameterType, Type returnType) {
		this.parameterType = parameterType;
		this.returnType = returnType;
	}

	public Type getParameterType() {
		return parameterType;
	}
	
	public Type getReturnType() {
		return returnType;
	}
	
	@Override
	public boolean canBeAssignedTo(Type other) {
		if (other instanceof MichelsonLambdaType)
			return parameterType.canBeAssignedTo(((MichelsonLambdaType) other).parameterType)
					&& returnType.canBeAssignedTo(((MichelsonLambdaType) other).returnType);
		return other.isUntyped();
	}

	@Override
	public Type commonSupertype(Type other) {
		if (other instanceof MichelsonLambdaType)
			if (parameterType.canBeAssignedTo(((MichelsonLambdaType) other).parameterType)
					&& returnType.canBeAssignedTo(((MichelsonLambdaType) other).returnType))
				return other;
		return Untyped.INSTANCE;
	}

	@Override
	public String toString() {
		return "LAMBDA " + parameterType + " " + returnType;
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
		result = prime * result + ((returnType == null) ? 0 : returnType.hashCode());
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
		MichelsonLambdaType other = (MichelsonLambdaType) obj;
		if (parameterType == null) {
			if (other.parameterType != null)
				return false;
		} else if (!parameterType.equals(other.parameterType))
			return false;
		if (returnType == null) {
			if (other.returnType != null)
				return false;
		} else if (!returnType.equals(other.returnType))
			return false;
		return true;
	}
}
