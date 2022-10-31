package it.unive.michelsonlisa.cfg.statement.instrumentation;

import it.unive.lisa.program.SourceCodeLocation;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.michelsonlisa.cfg.expression.nary.MichelsonPairData;

public class MichelsonParameterAndStore extends MichelsonPairData {
	
	public MichelsonParameterAndStore(CFG cfg, SourceCodeLocation location, Expression[] exprs) {
		super(cfg, location, exprs);
	}

}
