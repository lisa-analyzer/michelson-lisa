package it.unive.michelsonlisa.annotations;

import it.unive.lisa.program.annotations.Annotation;
import it.unive.lisa.program.cfg.statement.Statement;

/**
 * The class represents a method annotation.
 *
 * @author <a href="mailto:luca.olivieri@univr.it">Luca Olivieri</a>
 */
public class MethodAnnotation extends CodeAnnotation {

	private final Class<? extends Statement> statementClass;

	/**
	 * Builds an instance of method annotation.
	 * 
	 * @param annotation the annotation
	 * @param statementClass the statement class
	 */
	public MethodAnnotation(Annotation annotation, Class<? extends Statement> statementClass) {
		super(annotation);
		this.statementClass = statementClass;
	}

	/**
	 * Yields the statement class to annotate
	 * 
	 * @return the statement class
	 */
	public Class<? extends Statement> getStatementClass() {
		return statementClass;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((statementClass == null) ? 0 : statementClass.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		MethodAnnotation other = (MethodAnnotation) obj;
		if (statementClass == null) {
			if (other.statementClass != null)
				return false;
		} else if (!statementClass.equals(other.statementClass))
			return false;
		return true;
	}

	
	



	
}
