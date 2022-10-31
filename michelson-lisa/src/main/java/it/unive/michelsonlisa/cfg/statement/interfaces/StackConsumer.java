package it.unive.michelsonlisa.cfg.statement.interfaces;

public interface StackConsumer {

	default int getNumberOfStackElementsConsumed(){
		return 1;
	}
}
