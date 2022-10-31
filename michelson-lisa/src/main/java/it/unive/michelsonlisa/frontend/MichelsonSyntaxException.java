package it.unive.michelsonlisa.frontend;

/**
 * An exception thrown due to an inconsistency in an Michelson file.
 */
public class MichelsonSyntaxException extends RuntimeException {

	private static final long serialVersionUID = 4950907533241427847L;

	/**
	 * Builds the exception.
	 */
	public MichelsonSyntaxException() {
		super();
	}

	/**
	 * Builds the exception.
	 * 
	 * @param message the message of this exception
	 * @param cause   the inner cause of this exception
	 */
	public MichelsonSyntaxException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Builds the exception.
	 * 
	 * @param message the message of this exception
	 */
	public MichelsonSyntaxException(String message) {
		super(message);
	}

	/**
	 * Builds the exception.
	 * 
	 * @param cause the inner cause of this exception
	 */
	public MichelsonSyntaxException(Throwable cause) {
		super(cause);
	}
}