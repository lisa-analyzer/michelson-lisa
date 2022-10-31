package it.unive.michelsonlisa.frontend.visitors;



import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import it.unive.lisa.type.Type;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonPairType;

public class InputTracker {

	private final Pair<Object,Object> pair;
	private final List<InputAnnotation> annotationList;
	enum AnnotationType {
		InputParameter,
		InputStore
	}

	public InputTracker(MichelsonPairType type) {
		Type parameterType = type.getLeftType();
		Type storeType = type.getRightType();
		annotationList = new ArrayList<>();
		pair = Pair.of(computePairs(parameterType, AnnotationType.InputParameter), computePairs(storeType, AnnotationType.InputStore));
	}
	
	private int p=0;
	private int s=0;

	private Object computePairs(Type type, AnnotationType annotationType) {
		if(type instanceof MichelsonPairType)
			return Pair.of(computePairs(((MichelsonPairType) type).getLeftType(), annotationType), computePairs(((MichelsonPairType) type).getRightType(), annotationType));
		else {
			InputAnnotation a = null;
			switch(annotationType) {
				case InputParameter:
					a = new InputParameterAnnotation(p);
					p++;
					break;
				case InputStore: 
					a = new InputParameterAnnotation(s);
					s++;
					break;			
			}
			annotationList.add(a);
			return a;
		}
	}

	public Object getLeft() {
		return pair.getLeft();
	}

	public Object getRight() {
		return pair.getRight();
	}
	
	public List<Object> getUnpair() {
		return List.of(pair.getLeft(), pair.getRight());
	}
	
	public List<Object> getUnpair(int n) {
		//TODO:
		return null;
	}
	
	public List<InputAnnotation> getInputAnnotations(){
		
		return annotationList;
	}
	
}
