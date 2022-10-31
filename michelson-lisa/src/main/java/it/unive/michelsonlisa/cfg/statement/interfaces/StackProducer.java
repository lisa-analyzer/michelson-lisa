package it.unive.michelsonlisa.cfg.statement.interfaces;

public interface StackProducer {

	default int getNumberOfStackElementsProduced(){
		return 1;
	}
}
