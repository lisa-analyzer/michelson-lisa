package it.unive.michelsonlisa.cfg.expression.literal;

import java.math.BigInteger;

import it.unive.lisa.program.SourceCodeLocation;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.statement.literal.Literal;
import it.unive.michelsonlisa.cfg.type.MichelsonIntType;
import it.unive.michelsonlisa.cfg.type.MichelsonNatType;

/**
 * Michelson integer value class. The static type of a Michelson integer value is
 * {@link MichelsonIntType}.
 * 
 * @author <a href="mailto:luca.olivieri@univr.it">Luca Olivieri</a>
 */
public class MichelsonNaturalData extends Literal<Object> {

	/**
	 * Builds a Michelson integer value. The location where this Michelson integer value
	 * appears is unknown (i.e. no source file/line/column is available).
	 * 
	 * @param cfg   the cfg that this Michelson integer belongs to
	 * @param value the integer value
	 */
	public MichelsonNaturalData(CFG cfg, SourceCodeLocation location, BigInteger value) {
		super(cfg, location, value, MichelsonIntType.INSTANCE);
	}
	

	protected MichelsonNaturalData(CFG cfg, SourceCodeLocation location, Object value, MichelsonNatType type) {
		super(cfg, location, value, type);
	}


	@Override
	public BigInteger getValue() {
		return (BigInteger) super.getValue();
	}
	
	
}
