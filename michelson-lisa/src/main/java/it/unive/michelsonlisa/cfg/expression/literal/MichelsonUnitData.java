package it.unive.michelsonlisa.cfg.expression.literal;

import it.unive.lisa.program.SourceCodeLocation;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.statement.literal.Literal;
import it.unive.michelsonlisa.cfg.type.MichelsonUnitType;

/**
 * A Michelson unit value. The static type of a Michelson unit value is
 * {@link MichelsonUnitType}.
 * 
 * @author <a href="mailto:luca.olivieri@univr.it">Luca Olivieri</a>
 */
public class MichelsonUnitData extends Literal<Object>{

	/**
	 * Builds a Michelson unit value. The location where this Michelson unit value appears
	 * is unknown (i.e. no source file/line/column is available). The static
	 * type of a Michelson unit value is {@link MichelsonUnitType}.
	 * 
	 * @param cfg   the cfg that this Michelson unit belongs to
	 * @param value the unit value
	 */
	public MichelsonUnitData(CFG cfg, String sourceFile, int line, int col) {
		super(cfg,  new SourceCodeLocation(sourceFile, line, col), "Unit", MichelsonUnitType.INSTANCE);
	}

	@Override
	public String toString() {
		return "\"" + getValue() + "\"";
	}
}
