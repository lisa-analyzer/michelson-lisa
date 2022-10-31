package it.unive.michelsonlisa.cfg.expression.literal;

import it.unive.lisa.program.SourceCodeLocation;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.statement.literal.Literal;
import it.unive.lisa.type.Type;

/**
 * Instrumented Placeholder
 * 
 * @author <a href="mailto:luca.olivieri@univr.it">Luca Olivieri</a>
 */
public class MichelsonUnknownData extends Literal<Object> {

	public MichelsonUnknownData(CFG cfg, SourceCodeLocation location, String label, Type type) {
		super(cfg, location, label, type);
	}
	
	
}
