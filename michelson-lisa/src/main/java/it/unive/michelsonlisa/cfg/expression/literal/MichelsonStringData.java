package it.unive.michelsonlisa.cfg.expression.literal;

import it.unive.lisa.program.SourceCodeLocation;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.statement.literal.Literal;
import it.unive.michelsonlisa.cfg.type.MichelsonStringType;

/**
 * A Michelson string value. The static type of a Michelson string value is
 * {@link MichelsonStringType}.
 * 
 * @author <a href="mailto:luca.olivieri@univr.it">Luca Olivieri</a>
 */
public class MichelsonStringData extends Literal<String> {

	/**
	 * Builds a Michelson string value. The location where this Michelson string value appears
	 * is unknown (i.e. no source file/line/column is available). The static
	 * type of a Michelson string value is {@link MichelsonStringType}.
	 * 
	 * @param cfg   the cfg that this Michelson string belongs to
	 * @param value the string value
	 */
	public MichelsonStringData(CFG cfg, SourceCodeLocation location, String value) {
		super(cfg, location, value, MichelsonStringType.INSTANCE);
	}

	@Override
	public String toString() {
		return "\"" + getValue() + "\"";
	}
}
