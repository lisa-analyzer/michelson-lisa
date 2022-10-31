package it.unive.michelsonlisa.cfg.expression.literal;

import it.unive.lisa.program.SourceCodeLocation;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.statement.literal.Literal;
import it.unive.michelsonlisa.cfg.type.MichelsonBytesType;

/**
 * A Michelson byte sequence value. The static type of a Michelson byte sequence value is
 * {@link MichelsonBytesType}.
 * 
 * @author <a href="mailto:luca.olivieri@univr.it">Luca Olivieri</a>
 */
public class MichelsonByteSequenceData extends Literal<Object> {

	/**
	 * Builds a Michelson byte sequence value. The location where this Michelson byte sequence value appears
	 * is unknown (i.e. no source file/line/column is available). The static
	 * type of a Michelson byte sequence value is {@link MichelsonBytesType}.
	 * 
	 * @param cfg   the cfg that this Michelson byte sequence belongs to
	 * @param value the byte sequence value
	 */
	public MichelsonByteSequenceData(CFG cfg, SourceCodeLocation location, String value) {
		super(cfg, location, value, MichelsonBytesType.INSTANCE);
	}

	@Override
	public String toString() {
		return "\"" + getValue() + "\"";
	}
}
