package it.unive.michelsonlisa.frontend;

import it.unive.lisa.type.BooleanType;
import it.unive.lisa.type.NumericType;
import it.unive.lisa.type.StringType;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.TypeSystem;
import it.unive.michelsonlisa.cfg.type.MichelsonBoolType;
import it.unive.michelsonlisa.cfg.type.MichelsonIntType;
import it.unive.michelsonlisa.cfg.type.MichelsonStringType;

public class MichelsonTypeSystem extends TypeSystem {

	@Override
	public BooleanType getBooleanType() {
		return MichelsonBoolType.INSTANCE;
	}

	@Override
	public StringType getStringType() {
		return MichelsonStringType.INSTANCE;
	}

	@Override
	public NumericType getIntegerType() {
		return MichelsonIntType.INSTANCE;
	}

	@Override
	public boolean canBeReferenced(Type type) {
		return type.isInMemoryType();
	}

}
