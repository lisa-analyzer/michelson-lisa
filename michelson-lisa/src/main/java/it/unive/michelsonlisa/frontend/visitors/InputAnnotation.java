package it.unive.michelsonlisa.frontend.visitors;

import it.unive.lisa.program.annotations.Annotation;

public abstract class InputAnnotation extends Annotation {

	public int n;
	public InputAnnotation(String annotationName, int n) {
		super(annotationName);
		this.n=n;
	}

}
