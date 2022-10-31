package it.unive.michelsonlisa.cfg.type.interfaces;

import it.unive.lisa.program.SourceCodeLocation;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.type.Type;


public interface MichelsonType extends Type {

	public Expression defaultValue(CFG cfg, SourceCodeLocation location);

	public default boolean isMichelsonUntyped() {
		return false;
	}

	public default boolean isMichelsonUnsignedInteger() {
		return isNumericType() && asNumericType().isIntegral() && asNumericType().isUnsigned();
	}

	public default boolean isMichelsonSignedInteger() {
		return isNumericType() && asNumericType().isIntegral() && asNumericType().isSigned();
	}
}
