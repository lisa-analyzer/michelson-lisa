package it.unive.michelsonlisa.cfg.expression.literal;

import it.unive.lisa.program.SourceCodeLocation;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.statement.literal.Literal;
import it.unive.michelsonlisa.cfg.type.MichelsonBoolType;

/**
 * A Michelson boolean value. The static type of a Michelson boolean value is
 * {@link MichelsonBoolType}.
 * 
 * @author <a href="mailto:luca.olivieri@univr.it">Luca Olivieri</a>
 */
public class MichelsonBooleanData extends Literal<Boolean> {

	/**
	 * Builds a Michelson boolean value. The location where this value
	 * appears is unknown (i.e. no source file/line/column is available).
	 * 
	 * @param cfg   the cfg that this Michelson boolean belongs to
	 * @param value the Boolean value
	 */
	public MichelsonBooleanData(CFG cfg, SourceCodeLocation location, Boolean value) {
		super(cfg, location, value, MichelsonBoolType.INSTANCE);
	}
}
