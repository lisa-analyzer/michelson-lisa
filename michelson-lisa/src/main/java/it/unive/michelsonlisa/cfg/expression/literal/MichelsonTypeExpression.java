package it.unive.michelsonlisa.cfg.expression.literal;

import it.unive.lisa.program.SourceCodeLocation;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.statement.literal.Literal;
import it.unive.michelsonlisa.cfg.type.MichelsonUnitType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonType;

/**
 * A Michelson none value. The static type of a Michelson none value is
 * {@link MichelsonUnitType}.
 * 
 * @author <a href="mailto:luca.olivieri@univr.it">Luca Olivieri</a>
 */
public class MichelsonTypeExpression extends Literal<Object> {

	/**
	 * Builds a Michelson none value. The location where this Michelson none value appears
	 * is unknown (i.e. no source file/line/column is available). The static
	 * type of a Michelson none value is {@link MichelsonUnitType}.
	 * 
	 * @param cfg   the cfg that this Michelson none belongs to
	 * @param value the none value
	 */
	public MichelsonTypeExpression(CFG cfg, SourceCodeLocation location, MichelsonType type) {
		super(cfg, location, type, type);
	}

	@Override
	public String toString() {
		return "\"" + getValue() + "\"";
	}

	@Override
	public MichelsonType getValue() {
		
		return (MichelsonType) super.getValue();
	}
	
	

}
