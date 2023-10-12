package it.unive.michelsonlisa.checkers;

import it.unive.lisa.analysis.AnalyzedCFG;
import it.unive.lisa.analysis.SimpleAbstractState;
import it.unive.lisa.analysis.heap.MonolithicHeap;
import it.unive.lisa.analysis.nonrelational.value.TypeEnvironment;
import it.unive.lisa.analysis.nonrelational.value.ValueEnvironment;
import it.unive.lisa.analysis.taint.Taint;
import it.unive.lisa.analysis.types.InferredTypes;
import it.unive.lisa.checks.semantic.CheckToolWithAnalysisResults;
import it.unive.lisa.checks.semantic.SemanticCheck;
import it.unive.lisa.program.Global;
import it.unive.lisa.program.Unit;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.edge.Edge;
import it.unive.lisa.program.cfg.statement.Statement;
import it.unive.lisa.program.cfg.statement.VariableRef;
import it.unive.michelsonlisa.cfg.statement.MichelsonTransfer_tokens;

/**
 * A taint checker.
 */
public class TaintChecker implements
SemanticCheck<SimpleAbstractState<MonolithicHeap, ValueEnvironment<Taint>, TypeEnvironment<InferredTypes>>> {

	@Override
	public void beforeExecution(
			CheckToolWithAnalysisResults<SimpleAbstractState<MonolithicHeap, ValueEnvironment<Taint>, TypeEnvironment<InferredTypes>>> tool) {
	}

	@Override
	public void afterExecution(
			CheckToolWithAnalysisResults<SimpleAbstractState<MonolithicHeap, ValueEnvironment<Taint>, TypeEnvironment<InferredTypes>>> tool) {
	}

	@Override
	public void visitGlobal(
			CheckToolWithAnalysisResults<SimpleAbstractState<MonolithicHeap, ValueEnvironment<Taint>, TypeEnvironment<InferredTypes>>> tool,
			Unit unit, Global global, boolean instance) {
	}
	
	
	@Override
	public boolean visit(
			CheckToolWithAnalysisResults<SimpleAbstractState<MonolithicHeap, ValueEnvironment<Taint>, TypeEnvironment<InferredTypes>>> tool,
			CFG graph) {
		return true;
	}
	

	@Override
	public boolean visitUnit(
			CheckToolWithAnalysisResults<SimpleAbstractState<MonolithicHeap, ValueEnvironment<Taint>, TypeEnvironment<InferredTypes>>> tool,
			Unit unit) {
		return true;
	}
	
	@Override
	public boolean visit(
			CheckToolWithAnalysisResults<SimpleAbstractState<MonolithicHeap, ValueEnvironment<Taint>, TypeEnvironment<InferredTypes>>> tool,
			CFG graph, Edge edge) {
		return  true;
	}

	@Override
	public boolean visit(
			CheckToolWithAnalysisResults<SimpleAbstractState<MonolithicHeap, ValueEnvironment<Taint>, TypeEnvironment<InferredTypes>>> tool,
			CFG graph, Statement node) {
		if (node instanceof MichelsonTransfer_tokens) {
			MichelsonTransfer_tokens transferToken = (MichelsonTransfer_tokens) node;
			for (AnalyzedCFG<SimpleAbstractState<MonolithicHeap, 
						ValueEnvironment<Taint>, 
							TypeEnvironment<InferredTypes>>> result : tool.getResultOf(transferToken.getCFG())) {
				VariableRef ref = (VariableRef) transferToken.getSubExpressions()[2];
				Taint state = result.getAnalysisStateAfter(transferToken).getState().getValueState()
						.getState(ref.getVariable());
				if (state.isAlwaysTainted())
					tool.warnOn(node, "The value passed for the "
							+ " parameter of this transfer token is tainted");
				else if (state.isPossiblyTainted())
					tool.warnOn(node, "The value passed for the "
							+ " parameter of this transfer token might be tainted");
			}

		}

		return true;
	}



	
	

}
