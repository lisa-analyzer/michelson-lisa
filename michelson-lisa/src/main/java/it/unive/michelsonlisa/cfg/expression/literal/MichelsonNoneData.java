package it.unive.michelsonlisa.cfg.expression.literal;

import it.unive.lisa.program.SourceCodeLocation;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.statement.literal.Literal;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.Untyped;

/**
 * A Michelson none value.
 * 
 * @author <a href="mailto:luca.olivieri@univr.it">Luca Olivieri</a>
 */
public class MichelsonNoneData extends Literal<Object> {

	/**
	 * Builds a Michelson none value. 
	 * @param cfg   the cfg that this Michelson none belongs to
	 */
	public MichelsonNoneData(CFG cfg, SourceCodeLocation location, Type type) {
		super(cfg, location, "None", type);
	}
	
	public MichelsonNoneData(CFG cfg, SourceCodeLocation location) {
		super(cfg, location, "None", Untyped.INSTANCE);
	}

	@Override
	public String toString() {
		return "\"" + getValue() + "\"";
	}
}
