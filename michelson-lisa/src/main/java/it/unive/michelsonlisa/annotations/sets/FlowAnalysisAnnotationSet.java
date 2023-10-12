package it.unive.michelsonlisa.annotations.sets;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import it.unive.lisa.analysis.nonInterference.NonInterference;
import it.unive.lisa.analysis.taint.Taint;
import it.unive.lisa.program.cfg.statement.Statement;
import it.unive.michelsonlisa.annotations.AnnotationSet;
import it.unive.michelsonlisa.annotations.CodeAnnotation;
import it.unive.michelsonlisa.annotations.MethodAnnotation;

/**
 * The class represents the set of annotations for the flow analysis
 * 
 * @author <a href="mailto:luca.olivieri@univr.it">Luca Olivieri</a>
 */
public abstract class FlowAnalysisAnnotationSet extends AnnotationSet {

	/**
	 * Builds an instance of an annotation set  for the untrusted cross-contract invoking analysis
	 */
	public FlowAnalysisAnnotationSet() {

	}

	/**
	 * The source code member annotations.
	 */
	protected static final Map<Kind, Set<Class<? extends Statement>>> SOURCE_CODE_MEMBER_ANNOTATIONS = new HashMap<>();

	/**
	 * The sink code member annotations.
	 */
	protected static final Map<Kind, Set<Class<? extends Statement>>> SINK_CODE_MEMBER_ANNOTATIONS = new HashMap<>();

	/**
	 * The source code constructor annotations.
	 */
	protected static final Map<Kind, Set<Class<? extends Statement>>> SOURCE_CONSTRUCTORS_ANNOTATIONS = new HashMap<>();

	/**
	 * The sink code constructor annotations.
	 */
	protected static final Map<Kind, Set<Class<? extends Statement>>> SINK_CONSTRUCTORS_ANNOTATIONS = new HashMap<>();

	/**
	 * The source code constructor parameter annotations.
	 */
	protected static final Map<Kind,
	Set<Pair<Class<? extends Statement>, Integer>>> SOURCE_CONSTRUCTOR_PARAMETER_ANNOTATIONS = new HashMap<>();

	/**
	 * The sink code constructor parameter annotations.
	 */
	protected static final Map<Kind,
	Set<Pair<Class<? extends Statement>, Integer>>> SINK_CONSTRUCTOR_PARAMETER_ANNOTATIONS = new HashMap<>();

	@Override
	public Set<? extends CodeAnnotation> getAnnotationsForCodeMembers() {
		Set<CodeAnnotation> set = new HashSet<>();

		// sources
		for (Entry<Kind, Set<Class<? extends Statement>>> entry : SOURCE_CODE_MEMBER_ANNOTATIONS.entrySet())
			if (entry.getKey() == Kind.METHOD)
				entry.getValue().forEach(mtd -> {
						set.add(new MethodAnnotation(Taint.TAINTED_ANNOTATION, mtd));
						set.add(new MethodAnnotation(NonInterference.LOW_CONF_ANNOTATION, mtd));
					});

		// sink
		for (Entry<Kind, Set<Class<? extends Statement>>> entry : SINK_CODE_MEMBER_ANNOTATIONS.entrySet())
			if (entry.getKey() == Kind.METHOD)
				entry.getValue().forEach(mtd -> {
					//	set.add(new MethodAnnotation(TaintChecker.SINK_ANNOTATION, mtd));
						//set.add(new MethodAnnotation(IntegrityNIChecker.SINK_ANNOTATION, target.getKey(), mtd));
					});

		return set;
	}

	@Override
	public Set<? extends CodeAnnotation> getAnnotationsForConstructors() {
		Set<CodeAnnotation> set = new HashSet<>();

		// sources
		for (Entry<Kind, Set<Class<? extends Statement>>> entry : SOURCE_CONSTRUCTORS_ANNOTATIONS.entrySet())
			if (entry.getKey() == Kind.METHOD)
				entry.getValue().forEach(mtd -> {
						set.add(new MethodAnnotation(Taint.TAINTED_ANNOTATION,  mtd));
						set.add(new MethodAnnotation(NonInterference.LOW_CONF_ANNOTATION, mtd));
					});

		// sinks
		for (Entry<Kind, Set<Class<? extends Statement>>> entry : SINK_CONSTRUCTORS_ANNOTATIONS.entrySet())
			if (entry.getKey() == Kind.METHOD)
				entry.getValue().forEach(mtd -> {
						//set.add(new MethodAnnotation(TaintChecker.SINK_ANNOTATION, mtd));
						//set.add(new MethodAnnotation(IntegrityNIChecker.SINK_ANNOTATION, target.getKey(), mtd));
					});

		for (Entry<Kind, Set<Pair<Class<? extends Statement>, Integer>>> entry : SINK_CONSTRUCTOR_PARAMETER_ANNOTATIONS
				.entrySet())
			if (entry.getKey() == Kind.PARAM)
				entry.getValue().forEach(mtd -> {
						//set.add(new MethodParameterAnnotation(TaintChecker.SINK_ANNOTATION,	mtd.getLeft(), mtd.getRight()));
						//set.add(new MethodParameterAnnotation(IntegrityNIChecker.SINK_ANNOTATION, target.getKey(),
						//		mtd.getLeft(), mtd.getRight()));
					});

		return set;
	}

	@Override
	public Set<? extends CodeAnnotation> getAnnotationsForGlobals() {
		return new HashSet<>();
	}

	/**
	 * Yields the annotation set of sources.
	 * 
	 * @return the annotation set of sources
	 */
	public Set<? extends CodeAnnotation> getAnnotationForSources() {
		Set<CodeAnnotation> set = new HashSet<>();

		for (Entry<Kind, Set<Class<? extends Statement>>> entry : SOURCE_CODE_MEMBER_ANNOTATIONS.entrySet())
			if (entry.getKey() == Kind.METHOD)
					entry.getValue().forEach(mtd -> {
						set.add(new MethodAnnotation(Taint.TAINTED_ANNOTATION, mtd));
						set.add(new MethodAnnotation(NonInterference.LOW_CONF_ANNOTATION, mtd));
					});

		for (Entry<Kind, Set<Class<? extends Statement>>> entry : SOURCE_CONSTRUCTORS_ANNOTATIONS.entrySet())
			if (entry.getKey() == Kind.METHOD)
				entry.getValue().forEach(mtd -> {
						set.add(new MethodAnnotation(Taint.TAINTED_ANNOTATION, mtd));
						set.add(new MethodAnnotation(NonInterference.LOW_CONF_ANNOTATION, mtd));
					});

		return set;
	}

	/**
	 * Yields the annotation set of destinations (sinks).
	 * 
	 * @return the annotation set of destinations (sinks).
	 */
	public Set<? extends CodeAnnotation> getAnnotationForDestinations() {
		Set<CodeAnnotation> set = new HashSet<>();

		for (Entry<Kind, Set<Pair<Class<? extends Statement>, Integer>>> entry : SINK_CONSTRUCTOR_PARAMETER_ANNOTATIONS
				.entrySet())
			if (entry.getKey() == Kind.PARAM)
					entry.getValue().forEach(mtd -> {
						//set.add(new MethodParameterAnnotation(TaintChecker.SINK_ANNOTATION,
						//		mtd.getLeft(), mtd.getRight()));
						//set.add(new MethodParameterAnnotation(IntegrityNIChecker.SINK_ANNOTATION, target.getKey(),
						//		mtd.getLeft(), mtd.getRight()));
					});

		return set;
	}

	
}
