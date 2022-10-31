package it.unive.michelsonlisa.cfg.type.composite;

import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonComparableType;

/**
 * Boolean type of Michelson language. This is the only pair type available for Michelson. It
 * implements the singleton design pattern, that is the instances of this type
 * are unique. The unique instance of this type can be retrieved by
 * {@link MichelsonPairCompType#INSTANCE}.
 * 
 * @author <a href="mailto:luca.olivieri@univr.it">Luca Olivieri</a>
 */
public class MichelsonPairCompType extends MichelsonPairType {


	public MichelsonPairCompType(MichelsonComparableType ... elementTypes) {
		super(elementTypes);
	}
}
