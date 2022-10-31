package it.unive.michelsonlisa.annotations.sets;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import it.unive.lisa.program.cfg.statement.Statement;
import it.unive.michelsonlisa.cfg.statement.MichelsonTransfer_tokens;
import it.unive.michelsonlisa.cfg.statement.instrumentation.MichelsonParameterAndStore;

/**
 * The class represents the set of annotations for the untrusted cross-contract invoking analysis
 * 
 * @author <a href="mailto:luca.olivieri@univr.it">Luca Olivieri</a>
 */
public class CrossContractInvokingAnnotationSet extends FlowAnalysisAnnotationSet {

	/**
	 * Builds an instance of an annotation set  for the untrusted cross-contract invoking analysis
	 */
	public CrossContractInvokingAnnotationSet() {

		
	}
	

	static {
		
		Set<Class<? extends Statement>> sources = new HashSet<>();

		sources.add(MichelsonParameterAndStore.class);
		SOURCE_CODE_MEMBER_ANNOTATIONS.put(Kind.METHOD, sources);
		
		Set<Pair<Class<? extends Statement>, Integer>> sinks = new HashSet<>();

		sinks.add(Pair.of(MichelsonTransfer_tokens.class, 1));
		SINK_CONSTRUCTOR_PARAMETER_ANNOTATIONS.put(Kind.PARAM, sinks);
		
		
	}

	
}
