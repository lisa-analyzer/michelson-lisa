package it.unive.michelsonlisa.cfg.expression.literal;

import it.unive.lisa.program.SourceCodeLocation;
import it.unive.lisa.program.cfg.CFG;
import it.unive.michelsonlisa.cfg.type.MichelsonIntType;
import it.unive.michelsonlisa.cfg.type.MichelsonNatType;

/**
 * Michelson natural value class. The static type of a Michelson natural value is
 * {@link MichelsonNatType}.
 * 
 * @author <a href="mailto:luca.olivieri@univr.it">Luca Olivieri</a>
 */
public class MichelsonIntegerData extends MichelsonNaturalData {

	/**
	 * Builds a Michelson natural value. The location where this Michelson natural value
	 * appears is unknown (i.e. no source file/line/column is available).
	 * 
	 * @param cfg   the cfg that this Michelson natural belongs to
	 * @param value the natural value
	 */
	public MichelsonIntegerData(CFG cfg, SourceCodeLocation location, Object value) {
		super(cfg, location, value, MichelsonIntType.INSTANCE);
	}
}
