package it.unive.michelsonlisa.utils;

import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.type.Type;

public class MichelsonTypeUtils {

	public static Type[] getTypes(Expression[] exprs) {
		Type[] types = new Type[exprs.length];
		
		for(int i=0; i < exprs.length; i++)
			types[i] = exprs[i].getStaticType();
			
		return types;
	}

}
