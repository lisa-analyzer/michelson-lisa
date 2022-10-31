package it.unive.michelsonlisa.cfg.type.composite;


import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonComparableType;

/**
 * Boolean type of Michelson language. This is the only or type available for Michelson. It
 * implements the singleton design pattern, that is the instances of this type
 * are unique. The unique instance of this type can be retrieved by
 * {@link MichelsonOrCompType#INSTANCE}.
 * 
 * @author <a href="mailto:luca.olivieri@univr.it">Luca Olivieri</a>
 */
public class MichelsonOrCompType extends MichelsonOrType {
	
	public MichelsonOrCompType(MichelsonComparableType leftType, MichelsonComparableType rightType) {
		super(leftType, rightType);
	}

}
