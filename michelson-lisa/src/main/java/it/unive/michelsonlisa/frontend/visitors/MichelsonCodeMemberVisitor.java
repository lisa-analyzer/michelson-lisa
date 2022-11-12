package it.unive.michelsonlisa.frontend.visitors;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import it.unive.lisa.program.SourceCodeLocation;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.CodeLocation;
import it.unive.lisa.program.cfg.CodeMemberDescriptor;
import it.unive.lisa.program.cfg.controlFlow.ControlFlowStructure;
import it.unive.lisa.program.cfg.controlFlow.IfThenElse;
import it.unive.lisa.program.cfg.controlFlow.Loop;
import it.unive.lisa.program.cfg.edge.Edge;
import it.unive.lisa.program.cfg.edge.FalseEdge;
import it.unive.lisa.program.cfg.edge.SequentialEdge;
import it.unive.lisa.program.cfg.edge.TrueEdge;
import it.unive.lisa.program.cfg.statement.Assignment;
import it.unive.lisa.program.cfg.statement.Expression;
import it.unive.lisa.program.cfg.statement.NoOp;
import it.unive.lisa.program.cfg.statement.Ret;
import it.unive.lisa.program.cfg.statement.Statement;
import it.unive.lisa.program.cfg.statement.VariableRef;
import it.unive.lisa.type.Type;
import it.unive.lisa.type.Untyped;
import it.unive.lisa.util.datastructures.graph.code.NodeList;
import it.unive.michelsonlisa.antlr.MichelsonParser.*;
import it.unive.michelsonlisa.antlr.MichelsonParserBaseVisitor;
import it.unive.michelsonlisa.cfg.expression.literal.MichelsonBooleanData;
import it.unive.michelsonlisa.cfg.expression.literal.MichelsonByteSequenceData;
import it.unive.michelsonlisa.cfg.expression.literal.MichelsonIntegerData;
import it.unive.michelsonlisa.cfg.expression.literal.MichelsonNaturalData;
import it.unive.michelsonlisa.cfg.expression.literal.MichelsonNoneData;
import it.unive.michelsonlisa.cfg.expression.literal.MichelsonStringData;
import it.unive.michelsonlisa.cfg.expression.literal.MichelsonTypeExpression;
import it.unive.michelsonlisa.cfg.expression.literal.MichelsonUnitData;
import it.unive.michelsonlisa.cfg.expression.literal.MichelsonUnknownData;
import it.unive.michelsonlisa.cfg.expression.nary.MichelsonEltData;
import it.unive.michelsonlisa.cfg.expression.nary.MichelsonOrData;
import it.unive.michelsonlisa.cfg.expression.nary.MichelsonPairData;
import it.unive.michelsonlisa.cfg.expression.unary.MichelsonLeftData;
import it.unive.michelsonlisa.cfg.expression.unary.MichelsonRightData;
import it.unive.michelsonlisa.cfg.expression.unary.MichelsonSomeData;
import it.unive.michelsonlisa.cfg.statement.MichelsonAbs;
import it.unive.michelsonlisa.cfg.statement.MichelsonAdd;
import it.unive.michelsonlisa.cfg.statement.MichelsonAddress;
import it.unive.michelsonlisa.cfg.statement.MichelsonAmount;
import it.unive.michelsonlisa.cfg.statement.MichelsonAnd;
import it.unive.michelsonlisa.cfg.statement.MichelsonApply;
import it.unive.michelsonlisa.cfg.statement.MichelsonBalance;
import it.unive.michelsonlisa.cfg.statement.MichelsonBlake2b;
import it.unive.michelsonlisa.cfg.statement.MichelsonCar;
import it.unive.michelsonlisa.cfg.statement.MichelsonCast;
import it.unive.michelsonlisa.cfg.statement.MichelsonCdr;
import it.unive.michelsonlisa.cfg.statement.MichelsonChain_id;
import it.unive.michelsonlisa.cfg.statement.MichelsonCheck_signature;
import it.unive.michelsonlisa.cfg.statement.MichelsonCompare;
import it.unive.michelsonlisa.cfg.statement.MichelsonConcat;
import it.unive.michelsonlisa.cfg.statement.MichelsonCons;
import it.unive.michelsonlisa.cfg.statement.MichelsonContract;
import it.unive.michelsonlisa.cfg.statement.MichelsonCreateContract;
import it.unive.michelsonlisa.cfg.statement.MichelsonDig;
import it.unive.michelsonlisa.cfg.statement.MichelsonDip;
import it.unive.michelsonlisa.cfg.statement.MichelsonDrop;
import it.unive.michelsonlisa.cfg.statement.MichelsonDug;
import it.unive.michelsonlisa.cfg.statement.MichelsonDup;
import it.unive.michelsonlisa.cfg.statement.MichelsonEdiv;
import it.unive.michelsonlisa.cfg.statement.MichelsonEmptyBigMap;
import it.unive.michelsonlisa.cfg.statement.MichelsonEmptyMap;
import it.unive.michelsonlisa.cfg.statement.MichelsonEmptySet;
import it.unive.michelsonlisa.cfg.statement.MichelsonEq;
import it.unive.michelsonlisa.cfg.statement.MichelsonExec;
import it.unive.michelsonlisa.cfg.statement.MichelsonFailwith;
import it.unive.michelsonlisa.cfg.statement.MichelsonGe;
import it.unive.michelsonlisa.cfg.statement.MichelsonGet;
import it.unive.michelsonlisa.cfg.statement.MichelsonGt;
import it.unive.michelsonlisa.cfg.statement.MichelsonHash_key;
import it.unive.michelsonlisa.cfg.statement.MichelsonImplicit_account;
import it.unive.michelsonlisa.cfg.statement.MichelsonInt;
import it.unive.michelsonlisa.cfg.statement.MichelsonIsnat;
import it.unive.michelsonlisa.cfg.statement.MichelsonJoin_tickets;
import it.unive.michelsonlisa.cfg.statement.MichelsonKeccak;
import it.unive.michelsonlisa.cfg.statement.MichelsonLambda;
import it.unive.michelsonlisa.cfg.statement.MichelsonLe;
import it.unive.michelsonlisa.cfg.statement.MichelsonLeft;
import it.unive.michelsonlisa.cfg.statement.MichelsonLevel;
import it.unive.michelsonlisa.cfg.statement.MichelsonLsl;
import it.unive.michelsonlisa.cfg.statement.MichelsonLsr;
import it.unive.michelsonlisa.cfg.statement.MichelsonLt;
import it.unive.michelsonlisa.cfg.statement.MichelsonMem;
import it.unive.michelsonlisa.cfg.statement.MichelsonMul;
import it.unive.michelsonlisa.cfg.statement.MichelsonNeg;
import it.unive.michelsonlisa.cfg.statement.MichelsonNeq;
import it.unive.michelsonlisa.cfg.statement.MichelsonNever;
import it.unive.michelsonlisa.cfg.statement.MichelsonNil;
import it.unive.michelsonlisa.cfg.statement.MichelsonNone;
import it.unive.michelsonlisa.cfg.statement.MichelsonNot;
import it.unive.michelsonlisa.cfg.statement.MichelsonNow;
import it.unive.michelsonlisa.cfg.statement.MichelsonOpen_chest;
import it.unive.michelsonlisa.cfg.statement.MichelsonOr;
import it.unive.michelsonlisa.cfg.statement.MichelsonPack;
import it.unive.michelsonlisa.cfg.statement.MichelsonPair;
import it.unive.michelsonlisa.cfg.statement.MichelsonPairing_check;
import it.unive.michelsonlisa.cfg.statement.MichelsonPush;
import it.unive.michelsonlisa.cfg.statement.MichelsonRead_ticket;
import it.unive.michelsonlisa.cfg.statement.MichelsonRename;
import it.unive.michelsonlisa.cfg.statement.MichelsonRight;
import it.unive.michelsonlisa.cfg.statement.MichelsonSaplingEmptyState;
import it.unive.michelsonlisa.cfg.statement.MichelsonSapling_verify_update;
import it.unive.michelsonlisa.cfg.statement.MichelsonSelf;
import it.unive.michelsonlisa.cfg.statement.MichelsonSelf_address;
import it.unive.michelsonlisa.cfg.statement.MichelsonSender;
import it.unive.michelsonlisa.cfg.statement.MichelsonSet_delegate;
import it.unive.michelsonlisa.cfg.statement.MichelsonSha256;
import it.unive.michelsonlisa.cfg.statement.MichelsonSha3;
import it.unive.michelsonlisa.cfg.statement.MichelsonSha512;
import it.unive.michelsonlisa.cfg.statement.MichelsonSize;
import it.unive.michelsonlisa.cfg.statement.MichelsonSlice;
import it.unive.michelsonlisa.cfg.statement.MichelsonSome;
import it.unive.michelsonlisa.cfg.statement.MichelsonSource;
import it.unive.michelsonlisa.cfg.statement.MichelsonSplit_ticket;
import it.unive.michelsonlisa.cfg.statement.MichelsonSub;
import it.unive.michelsonlisa.cfg.statement.MichelsonSwap;
import it.unive.michelsonlisa.cfg.statement.MichelsonTicket;
import it.unive.michelsonlisa.cfg.statement.MichelsonTotal_voting_power;
import it.unive.michelsonlisa.cfg.statement.MichelsonTransfer_tokens;
import it.unive.michelsonlisa.cfg.statement.MichelsonUnit;
import it.unive.michelsonlisa.cfg.statement.MichelsonUnpack;
import it.unive.michelsonlisa.cfg.statement.MichelsonUnpair;
import it.unive.michelsonlisa.cfg.statement.MichelsonUpdate;
import it.unive.michelsonlisa.cfg.statement.MichelsonVoting_power;
import it.unive.michelsonlisa.cfg.statement.MichelsonXor;
import it.unive.michelsonlisa.cfg.statement.instrumentation.ConsumeLEFT_RIGHT_ProduceOr;
import it.unive.michelsonlisa.cfg.statement.instrumentation.EmptyStructure;
import it.unive.michelsonlisa.cfg.statement.instrumentation.ExtractValueFromOption;
import it.unive.michelsonlisa.cfg.statement.instrumentation.GetLeft;
import it.unive.michelsonlisa.cfg.statement.instrumentation.GetNextElementFromIterableStructure;
import it.unive.michelsonlisa.cfg.statement.instrumentation.GetRight;
import it.unive.michelsonlisa.cfg.statement.instrumentation.GetValueFromList;
import it.unive.michelsonlisa.cfg.statement.instrumentation.LeftUNPAIR;
import it.unive.michelsonlisa.cfg.statement.instrumentation.MichelsonParameterAndStore;
import it.unive.michelsonlisa.cfg.statement.instrumentation.Phi;
import it.unive.michelsonlisa.cfg.statement.instrumentation.RightUNPAIR;
import it.unive.michelsonlisa.cfg.statement.interfaces.StackProducer;
import it.unive.michelsonlisa.cfg.type.MichelsonAddressType;
import it.unive.michelsonlisa.cfg.type.MichelsonBLS12_381_FRType;
import it.unive.michelsonlisa.cfg.type.MichelsonBLS12_381_G1Type;
import it.unive.michelsonlisa.cfg.type.MichelsonBLS12_381_G2Type;
import it.unive.michelsonlisa.cfg.type.MichelsonBoolType;
import it.unive.michelsonlisa.cfg.type.MichelsonBytesType;
import it.unive.michelsonlisa.cfg.type.MichelsonChainIDType;
import it.unive.michelsonlisa.cfg.type.MichelsonChestKeyType;
import it.unive.michelsonlisa.cfg.type.MichelsonChestType;
import it.unive.michelsonlisa.cfg.type.MichelsonIntType;
import it.unive.michelsonlisa.cfg.type.MichelsonKeyHashType;
import it.unive.michelsonlisa.cfg.type.MichelsonKeyType;
import it.unive.michelsonlisa.cfg.type.MichelsonMutezType;
import it.unive.michelsonlisa.cfg.type.MichelsonNatType;
import it.unive.michelsonlisa.cfg.type.MichelsonNeverType;
import it.unive.michelsonlisa.cfg.type.MichelsonOperationType;
import it.unive.michelsonlisa.cfg.type.MichelsonSignatureType;
import it.unive.michelsonlisa.cfg.type.MichelsonStringType;
import it.unive.michelsonlisa.cfg.type.MichelsonTimestampType;
import it.unive.michelsonlisa.cfg.type.MichelsonUnitType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonBigMapType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonContractType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonLambdaType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonListType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonMapType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonOptionCompType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonOptionType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonOrCompType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonOrType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonPairCompType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonPairType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonSaplingStateType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonSaplingTransactionType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonSetType;
import it.unive.michelsonlisa.cfg.type.composite.MichelsonTicketType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonComparableType;
import it.unive.michelsonlisa.cfg.type.interfaces.MichelsonType;
import it.unive.michelsonlisa.utils.MichelsonFileUtils;

public class MichelsonCodeMemberVisitor extends MichelsonParserBaseVisitor<Object> {

	protected String filepath;

	protected final NodeList<CFG, Statement, Edge> nodeList;

	protected final Collection<Statement> entrypoints;

	protected final Collection<ControlFlowStructure> cfs;

	protected final CFG cfg;

	public final CodeMemberDescriptor descriptor;
	
	protected final ArrayList<MichelsonStack<VariableRef>> symbolicStacks = new ArrayList<>();
	
	protected final Map<VariableRef, Assignment> mapAssignamentNode = new HashMap<>();
	
	protected final Map<VariableRef, InputTracker> mapInputTracker = new HashMap<>();
	protected final Map<VariableRef, InputAnnotation> mapEntryPoints = new HashMap<>();
	
	public void printMapEntryPoints() {
		for (Entry<VariableRef, InputAnnotation> e : mapEntryPoints.entrySet())
			System.out.println(e.getKey() + " -> " + e.getValue().getAnnotationName());
			}
	protected final List<InputAnnotation> inputList = new ArrayList<>();
	
	
	protected final Map<MichelsonLambda, Triple<Statement, NodeList<CFG, Statement, Edge>, Statement>> mapLambda = new HashMap<>();
	
	protected final Map<MichelsonCreateContract, CFG> mapCreateContract = new HashMap<>();
	
	private int currentStack;
	
	static final SequentialEdge SEQUENTIAL_SINGLETON = new SequentialEdge();
	
	public MichelsonCodeMemberVisitor(String filepath, CodeMemberDescriptor descriptor) {
		this.filepath = filepath;
		cfs = new LinkedList<>();
		this.descriptor = descriptor;
		nodeList = new NodeList<>(SEQUENTIAL_SINGLETON);
		entrypoints = new HashSet<>();
		// side effects on entrypoints and nodeList will affect the cfg
		cfg = new CFG(descriptor, entrypoints, nodeList);
		currentStack = 0;
		symbolicStacks.add(new MichelsonStack<>());
	}
	
	int var_count = 0;

	Type selfType;
	
	public CFG visitCodeMember(ParameterContext parameter, StorageContext storage, CodeContext code)  {
		
		Type paramType = parameter != null && parameter.type() != null ? visitType(parameter.type()).getValue() : Untyped.INSTANCE;
		Type storageType = storage != null && storage.type() != null ? visitType(storage.type()).getValue() : Untyped.INSTANCE;
		
		selfType = paramType;
			
		MichelsonPairData data = buildParameter_StoreData(code, paramType, storageType);
		InputTracker inputTracker = new InputTracker((MichelsonPairType) data.getStaticType());
		inputList.addAll(inputTracker.getInputAnnotations());
		Assignment assign = new Assignment(cfg, MichelsonFileUtils.locationOf(filepath, code), data.getStaticType(), new VariableRef(cfg, MichelsonFileUtils.locationOf(filepath, code), "v"+0, data.getStaticType()), data );

		
		var_count++;
		
		mapAssignamentNode.put((VariableRef) assign.getLeft(), assign);
		mapInputTracker.put((VariableRef) assign.getLeft(), inputTracker);
		
		symbolicStacks.get(currentStack).push((VariableRef) assign.getLeft());
		nodeList.addNode(assign);
		entrypoints.add(assign);
		
		
		if (code.block() == null)
			return cfg;
		Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visited = visitBlock(code.block());

		if(visited == null) {
			NoOp instrumented  = new NoOp(cfg, MichelsonFileUtils.locationOf(filepath, code));
			NodeList<CFG, Statement, Edge> nLBlock = new NodeList<>(SEQUENTIAL_SINGLETON);
			nLBlock.addNode(instrumented);
			visited = Triple.of(instrumented, nLBlock, instrumented);
		} else
			addNopeStatement(visited); 
		
		nodeList.mergeWith(visited.getMiddle());
		addEdge(nodeList, new SequentialEdge(assign, visited.getLeft()));
		cfs.forEach(cf -> cfg.addControlFlowStructure(cf));

		if (cfg.getAllExitpoints().isEmpty()) {
			Ret ret = new Ret(cfg, descriptor.getLocation());
			if (cfg.getNodesCount() == 0) {
				// empty method, so the ret is also the entrypoint
				nodeList.addNode(ret);
				entrypoints.add(ret);
			} else {
				// every non-throwing instruction that does not have a follower
				// is ending the method
				Collection<Statement> preExits = new LinkedList<>();
				for (Statement st : nodeList.getNodes())
					if (!st.stopsExecution() && nodeList.followersOf(st).isEmpty())
						preExits.add(st);
				nodeList.addNode(ret);
				for (Statement st : preExits)
					addEdge(nodeList, new SequentialEdge(st, ret));
			}
		}
		
		simplifyCFG();
		return cfg;
	}

	private void addNopeStatement(Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visited) {
		
		Statement right = visited.getRight();
		
		if(!(right instanceof NoOp) && !visited.getMiddle().getIngoingEdges(right).isEmpty() && !right.stopsExecution() ) {
			NoOp nop = new NoOp(cfg, right.getLocation());
			visited.getMiddle().addNode(nop);		
			addEdge(visited.getMiddle(), new SequentialEdge(right,nop));
		}
	}

	private void simplifyCFG() {
		
		for( Statement st : nodeList.getExits())
			if((st instanceof NoOp ) && !nodeList.getIngoingEdges(st).isEmpty()) {
				Ret ret = new Ret(cfg, descriptor.getLocation());
				if (!st.stopsExecution() && nodeList.followersOf(st).isEmpty())
					nodeList.addNode(ret);
				nodeList.addEdge(new SequentialEdge(st, ret));
			}
		cfg.simplify();
		
	}

	private MichelsonPairData buildParameter_StoreData(CodeContext code, Type paramType, Type storageType) {
		
		MichelsonUnknownData param = new MichelsonUnknownData(cfg, MichelsonFileUtils.locationOf(filepath, code),"PARAMETER_VALUE" , paramType);
		MichelsonUnknownData storage = new MichelsonUnknownData(cfg, MichelsonFileUtils.locationOf(filepath, code), "STORAGE_VALUE",storageType);
		return new MichelsonParameterAndStore(cfg, MichelsonFileUtils.locationOf(filepath, code), new Expression[]{param, storage}) ;

	}

	//-------------------------------DATA------------------------------------------------
	@Override
	public Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitBlock(BlockContext ctx) {
		NodeList<CFG, Statement, Edge> nL = new NodeList<>(SEQUENTIAL_SINGLETON);

		Statement first = null, last = null;
		for (int i = 0; i < ctx.data().size(); i++) {
			Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> st = visitData(ctx.data(i));
			if(st == null)
				continue;
			nL.mergeWith(st.getMiddle());
			if (first == null)
				first = st.getLeft();
			if (last != null && !last.equals(st.getLeft()))
				addEdge(nL, new SequentialEdge(last, st.getLeft()));
			last = st.getRight();
		}

		if (first == null && last == null) {
			// empty block: instrument it with a noop
			return null;
		}

		return Triple.of(first, nL, last);
	}

	@Override
	public Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitData(DataContext ctx) {

		Expression e;
		if (ctx.int_const() != null)
			e = visitIntConst(ctx.int_const());
		else if (ctx.STRING_DATA() != null)
			e = visitStringConst(ctx.STRING_DATA());
		else if (ctx.BYTE_SEQUENCE_CONST() != null)
			e = visitByteSequenceConst(ctx.BYTE_SEQUENCE_CONST());
		else if (ctx.UNIT_DATA() != null)
			e = visitUnitData(ctx.UNIT_DATA());
		else if (ctx.TRUE_DATA() != null)
			e = visitTrueData(ctx.TRUE_DATA());
		else if (ctx.FALSE_DATA() != null)
			e = visitFalseData(ctx.FALSE_DATA());
		else if (ctx.pair_data() != null)
			e = visitPairData(ctx.pair_data());
		else if (ctx.left_data() != null)
			return visitLeftData(ctx.left_data());
		else if (ctx.right_data() != null)
			return visitRightData(ctx.right_data());
		else if (ctx.some_data() != null)
			return visitSomeData(ctx.some_data());
		else if (ctx.NONE_DATA() != null)
			e = visitNoneData(ctx.NONE_DATA());
		else if (ctx.or_data() != null)
			e = visitOrData(ctx.or_data());
		else if (ctx.block() != null)
			return visitBlock(ctx.block());
		else if (ctx.block_elt() != null)
			return visitBlockElt(ctx.block_elt());
		else if (ctx.instruction() != null)
			return visitInstruction(ctx.instruction());
		else
			throw new UnsupportedOperationException("Unsupported translation: " + ctx.getText());

		NodeList<CFG, Statement, Edge> nL = new NodeList<>(SEQUENTIAL_SINGLETON);
		nL.addNode(e);
		return Triple.of(e, nL, e);
	}

	private Expression visitOrData(Or_dataContext ctx) {
		
		List<DataContext> data = ctx.data();
		Expression[] exprs = new Expression[data.size()];
		for(int i = 0; i < data.size(); i++) {
			Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> d = visitData(data.get(i));
			exprs[i] = d != null ? (Expression) d.getRight() : new EmptyStructure(cfg, MichelsonFileUtils.locationOf(filepath, data.get(i)));
		}
		return new MichelsonOrData(cfg, MichelsonFileUtils.locationOf(filepath, ctx), exprs);
	}
	private Expression visitNoneData(TerminalNode ctx) {
		return new MichelsonNoneData(cfg, MichelsonFileUtils.locationOf(filepath, ctx));
	}

	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitSomeData(Some_dataContext ctx) {
		DataContext data = ctx.data();
		if (data != null) {
			SourceCodeLocation location = MichelsonFileUtils.locationOf(filepath, ctx);
			Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> tmp = visitData(data);
			
			NodeList<CFG, Statement, Edge> nL = new NodeList<>(SEQUENTIAL_SINGLETON);
			if(tmp != null)
				nL = tmp.getMiddle();
			
			MichelsonSomeData some_data = new MichelsonSomeData(cfg, location, tmp != null ? (Expression) tmp.getRight() : new EmptyStructure(cfg, location) );
			nL.addNode(some_data);

			return Triple.of(some_data, nL, some_data);
		}

		throw new UnsupportedOperationException("Unsupported translation: " + ctx.getText());
	}

	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitRightData(Right_dataContext ctx) {
		DataContext data = ctx.data();
		if (data != null) {
			SourceCodeLocation location = MichelsonFileUtils.locationOf(filepath, ctx);
			Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> tmp = visitData(data);
			
			NodeList<CFG, Statement, Edge> nL = new NodeList<>(SEQUENTIAL_SINGLETON);
			if(tmp != null)
				nL = tmp.getMiddle();
			
			MichelsonRightData right_data = new MichelsonRightData(cfg, location, tmp != null ? (Expression) tmp.getRight() : new EmptyStructure(cfg, location));
			nL.addNode(right_data);

			return Triple.of(right_data, nL, right_data);
		}

		throw new UnsupportedOperationException("Unsupported translation: " + ctx.getText());
	}

	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitLeftData(Left_dataContext ctx) {
		DataContext data = ctx.data();
		if (data != null) {
			SourceCodeLocation location = MichelsonFileUtils.locationOf(filepath, ctx);
			Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> tmp = visitData(data);
			
			NodeList<CFG, Statement, Edge> nL = new NodeList<>(SEQUENTIAL_SINGLETON);
			if(tmp != null)
				nL = tmp.getMiddle();
			
			MichelsonLeftData left_data = new MichelsonLeftData(cfg, location, tmp != null ? (Expression) tmp.getRight() : new EmptyStructure(cfg, location) );
			nL.addNode(left_data);

			return Triple.of(left_data, nL, left_data);
		}

		throw new UnsupportedOperationException("Unsupported translation: " + ctx.getText());
	}

	private Expression visitPairData(Pair_dataContext ctx) {
		List<DataContext> data = ctx.data();
		Expression[] exprs = new Expression[data.size()];
		for(int i = 0; i < data.size(); i++) {
			Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> d = visitData(data.get(i));
			exprs[i] = d != null ? (Expression) d.getRight() : new EmptyStructure(cfg, MichelsonFileUtils.locationOf(filepath, data.get(i)));
		}
		return new MichelsonPairData(cfg, MichelsonFileUtils.locationOf(filepath, ctx), exprs);
	}

	private Expression visitFalseData(TerminalNode ctx) {
		String value = ctx.getText();
		if (value != null) {
			SourceCodeLocation location = MichelsonFileUtils.locationOf(filepath, ctx);

			if (value.toUpperCase().equals("FALSE"))
				return new MichelsonBooleanData(cfg, location, Boolean.parseBoolean(value));
		}

		throw new UnsupportedOperationException("Unsupported translation: " + ctx.getText());
	}

	private Expression visitTrueData(TerminalNode ctx) {

		String value = ctx.getText();
		if (value != null) {
			SourceCodeLocation location = MichelsonFileUtils.locationOf(filepath, ctx);

			if (value.toUpperCase().equals("TRUE"))
				return new MichelsonBooleanData(cfg, location, Boolean.parseBoolean(value));
		}

		throw new UnsupportedOperationException("Unsupported translation: " + ctx.getText());
	}

	private Expression visitUnitData(TerminalNode ctx) {
		return new MichelsonUnitData(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx));
	}

	private Expression visitByteSequenceConst(TerminalNode ctx) {
		String value = ctx.getText();
		if (value != null) {
			SourceCodeLocation location = MichelsonFileUtils.locationOf(filepath, ctx);
			return new MichelsonByteSequenceData(cfg, location, value);
		}

		throw new UnsupportedOperationException("Unsupported translation: " + ctx.getText());
	}

	private Expression visitStringConst(TerminalNode ctx) {
		String value = ctx.getText();
		if (value != null) {
			SourceCodeLocation location = MichelsonFileUtils.locationOf(filepath, ctx);
			return new MichelsonStringData(cfg, location, value);
		}

		throw new UnsupportedOperationException("Unsupported string translation");
	}

	private Expression visitIntConst(Int_constContext ctx) {
		TerminalNode value = ctx.NAL_CONST();
		if (value != null && value.getText() != null) {
			SourceCodeLocation location = MichelsonFileUtils.locationOf(filepath, ctx);
			// TODO: should be use something to adopt an infinite arithmetic as Michelson
			return new MichelsonIntegerData(cfg, location, new BigInteger(ctx.MINUS() != null ? "-" + value.getText() : value.getText()));
		}

		throw new UnsupportedOperationException("Unsupported translation: " + ctx.getText());
	}

	private Expression visitNatConst(TerminalNode ctx) {
		if (ctx != null && ctx.getText() != null) {
			SourceCodeLocation location = MichelsonFileUtils.locationOf(filepath, ctx);
			// TODO: should be use something to adopt an infinite arithmetic as Michelson
			return new MichelsonNaturalData(cfg, location, new BigInteger(ctx.getText()));
		}

		throw new UnsupportedOperationException("Unsupported translation: " + ctx.getText());
	}

	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitBlockElt(
			Block_eltContext ctx) {
		NodeList<CFG, Statement, Edge> block = new NodeList<>(SEQUENTIAL_SINGLETON);
		
		MichelsonEltData elt = new MichelsonEltData(cfg, MichelsonFileUtils.locationOf(filepath, ctx));
		block.addNode(elt);
		;
		Statement first = null, last = null;
		for (int i = 0; i < ctx.data().size(); i++) {
			Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> st = visitData(ctx.data(i));
			if(st == null)
				continue;
			block.mergeWith(st.getMiddle());
			if (first == null)
				first = st.getLeft();
			if (last != null)
				addEdge(block, new SequentialEdge(last, st.getLeft()));
			last = st.getRight();
		}

		if (first == null && last == null) {
			// empty block: instrument it with a noop
			return null;
		}
		
		addEdge(block, new SequentialEdge(elt, first));

		return Triple.of(elt, block, last);
	}

//-------------------------------TYPE------------------------------------------------
	@Override
	public MichelsonTypeExpression visitType(TypeContext ctx) {
		if (ctx.comparable_type() != null)
			return visitComparable_type(ctx.comparable_type());
		else if (ctx.option_type() != null)
			return visitOption_type(ctx.option_type());
		else if (ctx.list_type() != null)
			return visitList_type(ctx.list_type());
		else if (ctx.set_type() != null)
			return visitSet_type(ctx.set_type());
		else if (ctx.OPERATION_TYPE() != null)
			return new MichelsonTypeExpression(cfg, new SourceCodeLocation(filepath, MichelsonFileUtils.getLine(ctx.OPERATION_TYPE()), MichelsonFileUtils.getCol(ctx.OPERATION_TYPE())), MichelsonOperationType.INSTANCE);
		else if (ctx.contract_type() != null)
			return visitContract_type(ctx.contract_type());
		else if (ctx.ticket_type() != null)
			return visitTicket_type(ctx.ticket_type());
		else if (ctx.pair_type() != null)
			return visitPair_type(ctx.pair_type());
		else if (ctx.or_type() != null)
			return visitOr_type(ctx.or_type());
		else if (ctx.lambda_type() != null)
			return visitLambda_type(ctx.lambda_type());
		else if (ctx.map_type() != null)
			return visitMap_type(ctx.map_type());
		else if (ctx.big_map_type() != null)
			return visitBig_map_type(ctx.big_map_type());
		else if (ctx.BLS12_381_G1_TYPE() != null)
			return new MichelsonTypeExpression(cfg, new SourceCodeLocation(filepath, MichelsonFileUtils.getLine(ctx.OPERATION_TYPE()), MichelsonFileUtils.getCol(ctx.OPERATION_TYPE())), MichelsonBLS12_381_G1Type.INSTANCE);
		else if (ctx.BLS12_381_G2_TYPE() != null)
			return new MichelsonTypeExpression(cfg, new SourceCodeLocation(filepath, MichelsonFileUtils.getLine(ctx.OPERATION_TYPE()), MichelsonFileUtils.getCol(ctx.OPERATION_TYPE())), MichelsonBLS12_381_G2Type.INSTANCE);
		else if (ctx.BLS12_381_FR_TYPE() != null)
			return new MichelsonTypeExpression(cfg, new SourceCodeLocation(filepath, MichelsonFileUtils.getLine(ctx.OPERATION_TYPE()), MichelsonFileUtils.getCol(ctx.OPERATION_TYPE())), MichelsonBLS12_381_FRType.INSTANCE);
		else if (ctx.sapling_transaction_type() != null)
			return visitSapling_transaction_type(ctx.sapling_transaction_type());
		else if (ctx.sapling_state_type() != null)
			return visitSapling_state_type(ctx.sapling_state_type());
		else if (ctx.CHEST_TYPE() != null)
			return new MichelsonTypeExpression(cfg, new SourceCodeLocation(filepath, MichelsonFileUtils.getLine(ctx.OPERATION_TYPE()), MichelsonFileUtils.getCol(ctx.OPERATION_TYPE())), MichelsonChestType.INSTANCE);
		else if (ctx.CHEST_KEY_TYPE() != null)
			return new MichelsonTypeExpression(cfg, new SourceCodeLocation(filepath, MichelsonFileUtils.getLine(ctx.OPERATION_TYPE()), MichelsonFileUtils.getCol(ctx.OPERATION_TYPE())), MichelsonChestKeyType.INSTANCE);
		else
			throw new UnsupportedOperationException("Unsupported translation: " + ctx.getText());
		
	}
	
	@Override
	public MichelsonTypeExpression visitOption_type(Option_typeContext ctx) {
		return new MichelsonTypeExpression(cfg, new SourceCodeLocation(filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx)), new MichelsonOptionType( visitType(ctx.type()).getValue()));
	}
	
	@Override
	public MichelsonTypeExpression visitSet_type(Set_typeContext ctx) {
		return new MichelsonTypeExpression(cfg, new SourceCodeLocation(filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx)), new MichelsonSetType( (MichelsonComparableType) visitComparable_type(ctx.comparable_type()).getValue()));
	}
	
	@Override
	public MichelsonTypeExpression visitContract_type(Contract_typeContext ctx) {
		return new MichelsonTypeExpression(cfg, new SourceCodeLocation(filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx)), new MichelsonContractType( visitType(ctx.type()).getValue()));
	}
	
	@Override
	public MichelsonTypeExpression visitTicket_type(Ticket_typeContext ctx) {
		return new MichelsonTypeExpression(cfg, new SourceCodeLocation(filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx)), new MichelsonTicketType( (MichelsonComparableType) visitComparable_type(ctx.comparable_type()).getValue()));
	}
	
	@Override
	public MichelsonTypeExpression visitPair_type(Pair_typeContext ctx) {
		MichelsonType[] elementTypes = new MichelsonType[ctx.type().size()];
		
		for(int i= 0;  i< ctx.type().size(); i++)
			elementTypes[i] = visitType(ctx.type(i)).getValue();
		
		return new MichelsonTypeExpression(cfg, new SourceCodeLocation(filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx)), new MichelsonPairType(elementTypes));
	}
	
	@Override
	public MichelsonTypeExpression visitOr_type(Or_typeContext ctx) {
		return new MichelsonTypeExpression(cfg, new SourceCodeLocation(filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx)), 
				new MichelsonOrType(visitType(ctx.type(0)).getValue(), visitType(ctx.type(1)).getValue()));
	}
	
	@Override
	public MichelsonTypeExpression visitLambda_type(Lambda_typeContext ctx) {
		return new MichelsonTypeExpression(cfg, new SourceCodeLocation(filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx)), 
				new MichelsonLambdaType(visitType(ctx.type(0)).getValue(), visitType(ctx.type(1)).getValue()));
	}
	
	@Override
	public MichelsonTypeExpression visitMap_type(Map_typeContext ctx) {
		return new MichelsonTypeExpression(cfg, new SourceCodeLocation(filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx)), 
				new MichelsonMapType((MichelsonComparableType) visitComparable_type(ctx.comparable_type()).getValue(), visitType(ctx.type()).getValue()));
	}
	
	@Override
	public MichelsonTypeExpression visitBig_map_type(Big_map_typeContext ctx) {
		return new MichelsonTypeExpression(cfg, new SourceCodeLocation(filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx)), 
				new MichelsonBigMapType((MichelsonComparableType) visitComparable_type(ctx.comparable_type()).getValue(), visitType(ctx.type()).getValue()));

	}
	
	@Override
	public MichelsonTypeExpression visitSapling_transaction_type(Sapling_transaction_typeContext ctx) {
		return new MichelsonTypeExpression(cfg, new SourceCodeLocation(filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx)), new MichelsonSaplingTransactionType( (MichelsonNaturalData) visitNatConst(ctx.NAL_CONST())));
	}
	
	@Override
	public MichelsonTypeExpression visitList_type(List_typeContext ctx) {
		return new MichelsonTypeExpression(cfg, new SourceCodeLocation(filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx)), new MichelsonListType( visitType(ctx.type()).getValue()));
	}
	
	@Override
	public MichelsonTypeExpression visitSapling_state_type(Sapling_state_typeContext ctx) {
		return new MichelsonTypeExpression(cfg, new SourceCodeLocation(filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx)), new MichelsonSaplingStateType( (MichelsonNaturalData) visitNatConst(ctx.NAL_CONST())));
	}
	
	
//-------------------------------COMPARABLE_TYPE------------------------------------------------
	@Override
	public MichelsonTypeExpression visitComparable_type(Comparable_typeContext ctx) {
	
		if (ctx.UNIT_TYPE() != null)
			return new MichelsonTypeExpression(cfg, new SourceCodeLocation(filepath, MichelsonFileUtils.getLine(ctx.UNIT_TYPE()), MichelsonFileUtils.getCol(ctx.UNIT_TYPE())), MichelsonUnitType.INSTANCE);
		if (ctx.NEVER_TYPE() != null)
			return new MichelsonTypeExpression(cfg, new SourceCodeLocation(filepath, MichelsonFileUtils.getLine(ctx.NEVER_TYPE()), MichelsonFileUtils.getCol(ctx.NEVER_TYPE())), MichelsonNeverType.INSTANCE);
		if (ctx.BOOL_TYPE() != null)
			return new MichelsonTypeExpression(cfg, new SourceCodeLocation(filepath, MichelsonFileUtils.getLine(ctx.BOOL_TYPE()), MichelsonFileUtils.getCol(ctx.BOOL_TYPE())), MichelsonBoolType.INSTANCE);
		if (ctx.INT_TYPE() != null)
			return new MichelsonTypeExpression(cfg, new SourceCodeLocation(filepath, MichelsonFileUtils.getLine(ctx.INT_TYPE()), MichelsonFileUtils.getCol(ctx.INT_TYPE())), MichelsonIntType.INSTANCE);
		if (ctx.NAT_TYPE() != null)
			return new MichelsonTypeExpression(cfg, new SourceCodeLocation(filepath, MichelsonFileUtils.getLine(ctx.NAT_TYPE()), MichelsonFileUtils.getCol(ctx.NAT_TYPE())), MichelsonNatType.INSTANCE);
		if (ctx.STRING_TYPE() != null)
			return new MichelsonTypeExpression(cfg, new SourceCodeLocation(filepath, MichelsonFileUtils.getLine(ctx.STRING_TYPE()), MichelsonFileUtils.getCol(ctx.STRING_TYPE())), MichelsonStringType.INSTANCE);
		if (ctx.CHAIN_ID_TYPE() != null)
			return new MichelsonTypeExpression(cfg, new SourceCodeLocation(filepath, MichelsonFileUtils.getLine(ctx.CHAIN_ID_TYPE()), MichelsonFileUtils.getCol(ctx.CHAIN_ID_TYPE())), MichelsonChainIDType.INSTANCE);
		if (ctx.BYTES_TYPE() != null)
			return new MichelsonTypeExpression(cfg, new SourceCodeLocation(filepath, MichelsonFileUtils.getLine(ctx.BYTES_TYPE()), MichelsonFileUtils.getCol(ctx.BYTES_TYPE())), MichelsonBytesType.INSTANCE);
		if (ctx.MUTEZ_TYPE() != null)
			return new MichelsonTypeExpression(cfg, new SourceCodeLocation(filepath, MichelsonFileUtils.getLine(ctx.MUTEZ_TYPE()), MichelsonFileUtils.getCol(ctx.MUTEZ_TYPE())), MichelsonMutezType.INSTANCE);
		if (ctx.KEY_HASH_TYPE() != null)
			return new MichelsonTypeExpression(cfg, new SourceCodeLocation(filepath, MichelsonFileUtils.getLine(ctx.KEY_HASH_TYPE()), MichelsonFileUtils.getCol(ctx.KEY_HASH_TYPE())), MichelsonKeyHashType.INSTANCE);
		if (ctx.KEY_TYPE() != null)
			return new MichelsonTypeExpression(cfg, new SourceCodeLocation(filepath, MichelsonFileUtils.getLine(ctx.KEY_TYPE()), MichelsonFileUtils.getCol(ctx.KEY_TYPE())), MichelsonKeyType.INSTANCE);
		if (ctx.SIGNATURE_TYPE() != null)
			return new MichelsonTypeExpression(cfg, new SourceCodeLocation(filepath, MichelsonFileUtils.getLine(ctx.SIGNATURE_TYPE()), MichelsonFileUtils.getCol(ctx.SIGNATURE_TYPE())), MichelsonSignatureType.INSTANCE);
		if (ctx.TIMESTAMP_TYPE() != null)
			return new MichelsonTypeExpression(cfg, new SourceCodeLocation(filepath, MichelsonFileUtils.getLine(ctx.TIMESTAMP_TYPE()), MichelsonFileUtils.getCol(ctx.TIMESTAMP_TYPE())), MichelsonTimestampType.INSTANCE);
		if (ctx.ADDRESS_TYPE() != null)
			return new MichelsonTypeExpression(cfg, new SourceCodeLocation(filepath, MichelsonFileUtils.getLine(ctx.ADDRESS_TYPE()), MichelsonFileUtils.getCol(ctx.ADDRESS_TYPE())), MichelsonAddressType.INSTANCE);
		if (ctx.option_comptype() != null)
			return visitOption_comptype(ctx.option_comptype());
		if (ctx.or_comptype() != null)
			return visitOr_comptype(ctx.or_comptype());
		if (ctx.pair_comptype() != null)
			return visitPair_comptype(ctx.pair_comptype());
		else
			throw new UnsupportedOperationException("Unsupported translation: " + ctx.getText());
	}
	
	@Override
	public MichelsonTypeExpression visitOption_comptype(Option_comptypeContext ctx) {
		return new MichelsonTypeExpression(cfg, new SourceCodeLocation(filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx)), 
				new MichelsonOptionCompType((MichelsonComparableType) visitComparable_type(ctx.comparable_type()).getValue()));
	}
	
	@Override
	public MichelsonTypeExpression visitOr_comptype(Or_comptypeContext ctx) {
		return new MichelsonTypeExpression(cfg, new SourceCodeLocation(filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx)), 
				new MichelsonOrCompType((MichelsonComparableType) visitComparable_type(ctx.comparable_type(0)).getValue(), (MichelsonComparableType) visitComparable_type(ctx.comparable_type(1)).getValue()));
	}
	
	@Override
	public MichelsonTypeExpression visitPair_comptype(Pair_comptypeContext ctx) {
		MichelsonComparableType[] elementTypes = new MichelsonComparableType[ctx.comparable_type().size()];
		
		for(int i= 0;  i< ctx.comparable_type().size(); i++)
			elementTypes[i] = (MichelsonComparableType) visitComparable_type(ctx.comparable_type(i)).getValue();
		
		return new MichelsonTypeExpression(cfg, new SourceCodeLocation(filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx)), new MichelsonPairCompType(elementTypes));
	}
	
//-------------------------------INSTRUCTION------------------------------------------------

	@Override
	public Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitInstruction(
			InstructionContext ctx) {

		Expression e;
		Object track = null;

		if (ctx.SWAP() != null)
			e = visitSwap(ctx.SWAP());
		else if (ctx.some_instr() != null)
			e = visitSome(ctx.some_instr());
		else if (ctx.unit_instr() != null)
			e = visitUnit(ctx.unit_instr());
		else if (ctx.never_instr() != null)
			e = visitNever(ctx.never_instr());
		else if (ctx.car_instr() != null) {
			Pair<Expression, Object> res =  visitCar(ctx.car_instr());
			 e = res.getLeft();
			 track = res.getRight();
		} else if (ctx.cdr_instr() != null) {
			Pair<Expression, Object> res = visitCdr(ctx.cdr_instr());
			 e = res.getLeft();
			 track = res.getRight();
		} else if (ctx.cons_instr() != null)
			e = visitCons(ctx.cons_instr());
		else if (ctx.size_instr() != null)
			e = visitSize(ctx.size_instr());
		else if (ctx.mem_instr() != null)
			e = visitMem(ctx.mem_instr());
		else if (ctx.exec_instr() != null)
			e = visitExec(ctx.exec_instr());
		else if (ctx.apply_instr() != null)
			e = visitApply(ctx.apply_instr());
		else if (ctx.FAILWITH() != null)
			e = visitFailwith(ctx.FAILWITH());
		else if (ctx.cast_instr() != null)
			e = visitCast(ctx.cast_instr());
		else if (ctx.rename_instr() != null)
			e = visitRename(ctx.rename_instr());
		else if (ctx.concat_instr() != null)
			e = visitConcat(ctx.concat_instr());
		else if (ctx.slice_instr() != null)
			e = visitSlice(ctx.slice_instr());
		else if (ctx.pack_instr() != null)
			e = visitPack(ctx.pack_instr());
		else if (ctx.add_instr() != null)
			e = visitAdd(ctx.add_instr());
		else if (ctx.sub_instr() != null)
			e = visitSub(ctx.sub_instr());
		else if (ctx.mul_instr() != null)
			e = visitMul(ctx.mul_instr());
		else if (ctx.ediv_instr() != null)
			e = visitEdiv(ctx.ediv_instr());
		else if (ctx.abs_instr() != null)
			e = visitAbs(ctx.abs_instr());
		else if (ctx.isnat_instr() != null)
			e = visitIsnat(ctx.isnat_instr());
		else if (ctx.int_instr() != null)
			e = visitInt(ctx.int_instr());
		else if (ctx.neg_instr() != null)
			e = visitNeg(ctx.neg_instr());
		else if (ctx.lsl_instr() != null)
			e = visitLsl(ctx.lsl_instr());
		else if (ctx.lsr_instr() != null)
			e = visitLsr(ctx.lsr_instr());
		else if (ctx.or_instr() != null)
			e = visitOr(ctx.or_instr());
		else if (ctx.and_instr() != null)
			e = visitAnd(ctx.and_instr());
		else if (ctx.xor_instr() != null)
			e = visitXor(ctx.xor_instr());
		else if (ctx.not_instr() != null)
			e = visitNot(ctx.not_instr());
		else if (ctx.compare_instr() != null)
			e = visitCompare(ctx.compare_instr());
		else if (ctx.eq_instr() != null)
			e = visitEq(ctx.eq_instr());
		else if (ctx.neq_instr() != null)
			e = visitNeq(ctx.neq_instr());
		else if (ctx.lt_instr() != null)
			e = visitLt(ctx.lt_instr());
		else if (ctx.gt_instr() != null)
			e = visitGt(ctx.gt_instr());
		else if (ctx.le_instr() != null)
			e = visitLe(ctx.le_instr());
		else if (ctx.ge_instr() != null)
			e = visitGe(ctx.ge_instr());
		else if (ctx.self_instr() != null)
			e = visitSelf(ctx.self_instr());
		else if (ctx.self_address_instr() != null)
			e = visitSelf_address(ctx.self_address_instr());
		else if (ctx.transfer_tokens_instr() != null)
			e = visitTransfer_tokens(ctx.transfer_tokens_instr());
		else if (ctx.set_delegate_instr() != null)
			e = visitSet_delegate(ctx.set_delegate_instr());
		else if (ctx.implicit_account_instr() != null)
			e = visitImplicit_account(ctx.implicit_account_instr());
		else if (ctx.voting_power_instr() != null)
			e = visitVoting_power(ctx.voting_power_instr());
		else if (ctx.now_instr() != null)
			e = visitNow(ctx.now_instr());
		else if (ctx.level_instr() != null)
			e = visitLevel(ctx.level_instr());
		else if (ctx.amount_instr() != null)
			e = visitAmount(ctx.amount_instr());
		else if (ctx.balance_instr() != null)
			e = visitBalance(ctx.balance_instr());
		else if (ctx.check_signature_instr() != null)
			e = visitCheck_signature(ctx.check_signature_instr());
		else if (ctx.blake2b_instr() != null)
			e = visitBlake2b(ctx.blake2b_instr());
		else if (ctx.keccak_instr() != null)
			e = visitKeccak(ctx.keccak_instr());
		else if (ctx.sha3_instr() != null)
			e = visitSha3(ctx.sha3_instr());
		else if (ctx.sha256_instr() != null)
			e = visitSha256(ctx.sha256_instr());
		else if (ctx.sha512_instr() != null)
			e = visitSha512(ctx.sha512_instr());
		else if (ctx.hash_key_instr() != null)
			e = visitHash_key(ctx.hash_key_instr());
		else if (ctx.source_instr() != null)
			e = visitSource(ctx.source_instr());
		else if (ctx.sender_instr() != null)
			e = visitSender(ctx.sender_instr());
		else if (ctx.address_instr() != null)
			e = visitAddress(ctx.address_instr());
		else if (ctx.chain_id_instr() != null)
			e = visitChain_id(ctx.chain_id_instr());
		else if (ctx.total_voting_power_instr() != null)
			e = visitTotal_voting_power(ctx.total_voting_power_instr());
		else if (ctx.pairing_check_instr() != null)
			e = visitPairing_check(ctx.pairing_check_instr());
		else if (ctx.sapling_verify_update_instr() != null)
			e = visitSapling_verify_update(ctx.sapling_verify_update_instr());
		else if (ctx.ticket_instr() != null)
			e = visitTicket(ctx.ticket_instr());
		else if (ctx.read_ticket_instr() != null)
			e = visitRead_ticket(ctx.read_ticket_instr());
		else if (ctx.split_ticket_instr() != null)
			e = visitSplit_ticket(ctx.split_ticket_instr());
		else if (ctx.join_tickets_instr() != null)
			e = visitJoin_tickets(ctx.join_tickets_instr());
		else if (ctx.open_chest_instr() != null)
			e = visitOpen_chest(ctx.open_chest_instr());
		else if (ctx.block() != null)
			return visitBlock(ctx.block());
		else if (ctx.drop_instr() != null)
			e = visitDrop_instr(ctx.drop_instr());
		else if (ctx.dup_instr() != null) {
			Pair<Expression, Object> res = visitDup_instr(ctx.dup_instr());
			 e = res.getLeft();
			 track = res.getRight();
		} else if (ctx.dig_instr() != null)
			e = visitDig_instr(ctx.dig_instr());
		else if (ctx.dug_instr() != null)
			e = visitDug_instr(ctx.dug_instr());
		else if (ctx.push_instr() != null)
			e = visitPush_instr(ctx.push_instr());
		else if (ctx.none_instr() != null)
			e = visitNone_instr(ctx.none_instr());
		else if (ctx.if_none_instr() != null)
			return visitIf_none_instr(ctx.if_none_instr());
		else if (ctx.pair_instr() != null)
			e = visitPair_instr(ctx.pair_instr());
		else if (ctx.unpair_instr() != null)	
			return visitUnpair_instr(ctx.unpair_instr());
		else if (ctx.left_instr() != null)
			e = visitLeft_instr(ctx.left_instr());
		else if (ctx.right_instr() != null)
			e = visitRight_instr(ctx.right_instr());
		else if (ctx.if_left_instr() != null)
			return visitIf_left_instr(ctx.if_left_instr());
		else if (ctx.nil_instr() != null)
			e = visitNil_instr(ctx.nil_instr());
		else if (ctx.if_cons_instr() != null)
			return visitIf_cons_instr(ctx.if_cons_instr());
		else if (ctx.empty_set_instr() != null)
			e =  visitEmpty_set_instr(ctx.empty_set_instr());
		else if (ctx.empty_map_instr() != null)
			e = visitEmpty_map_instr(ctx.empty_map_instr());
		else if (ctx.empty_big_map_instr() != null)
			e = visitEmpty_big_map_instr(ctx.empty_big_map_instr());
		else if (ctx.map_instr() != null)
			return visitMap_instr(ctx.map_instr());
		else if (ctx.iter_instr() != null)
			return visitIter_instr(ctx.iter_instr());
		else if (ctx.get_instr() != null)
			e = visitGet_instr(ctx.get_instr());
		else if (ctx.update_instr() != null)
			e = visitUpdate_instr(ctx.update_instr());
		else if (ctx.if_instr() != null)
			return visitIf_instr(ctx.if_instr());
		else if (ctx.loop_instr() != null)
			return visitLoop_instr(ctx.loop_instr());
		else if (ctx.loop_left_instr() != null)
			return visitLoop_left_instr(ctx.loop_left_instr());
		else if (ctx.lambda_instr() != null)
			e = visitLambda_instr(ctx.lambda_instr());
		else if (ctx.dip_instr() != null)
			return visitDip_instr(ctx.dip_instr());
		else if (ctx.unpack_instr() != null)
			e =  visitUnpack_instr(ctx.unpack_instr());
		else if (ctx.contract_instr() != null)
			e = visitContract_instr(ctx.contract_instr());
		else if (ctx.create_contract_instr() != null)
			e = visitCreate_contract_instr(ctx.create_contract_instr());
		else if (ctx.sapling_empty_state() != null)
			e = visitSapling_empty_state(ctx.sapling_empty_state());
		else if (ctx.macro() != null)
			return visitMacro(ctx.macro());
		else
			throw new UnsupportedOperationException("Unsupported translation: " + ctx.getText());
		
		NodeList<CFG, Statement, Edge> nL = new NodeList<>(SEQUENTIAL_SINGLETON);
		

		if (StackProducer.class.isAssignableFrom((e.getClass()))) {
			Assignment start = null;
			Assignment end = null;
			
			Assignment prev = null;
			for( int i = 0; i < ((StackProducer) e).getNumberOfStackElementsProduced(); i++) { 

				Assignment st = buildAssignment(e);
				checkPossibleInput((VariableRef) st.getLeft(), track);
				nL.addNode(st);
				
				if(i == 0)
					start = st;
				if(i == ((StackProducer) e).getNumberOfStackElementsProduced() -1)
					end = st;
				if(prev != null)
					addEdge(nL, new SequentialEdge(prev, st));
				prev = st;
			}
			
			return Triple.of(start, nL, end);
		}
		else {
			nL.addNode(e);
			return Triple.of(e, nL, e);
		}
	}

	private Assignment buildAssignment(Expression e) {
		return buildAssignment(e, e.getStaticType(), true);
	}

	private Assignment buildAssignment(Expression e, Type type, boolean push) {
		
		
		VariableRef ref = buildVariableRef(e.getLocation(), type, push);

		Assignment assign = new Assignment(cfg, e.getLocation(), type, ref,  e);

		mapAssignamentNode.put(ref, assign);
		
		return assign;
		
	}
	
	private VariableRef buildVariableRef(CodeLocation location, Type type) {
		return buildVariableRef(location, type, true); 
	} 
	
	private VariableRef buildVariableRef(CodeLocation location, Type type, boolean push) {
		
		
		VariableRef ref = new VariableRef(cfg, location,  "v"+var_count, type);
		var_count++;

		if(push)
			symbolicStacks.get(currentStack).push(ref);
		return ref;
		
	}
	
	@Override
	public Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitMacro(MacroContext ctx) {
		if (ctx.CMPEQ() != null)
				return visitCmpeq(cfg, ctx.CMPEQ());
		else if (ctx.CMPNEQ() != null)
				return visitCmpneq(cfg, ctx.CMPNEQ());
		else if (ctx.CMPLT() != null)
				return visitCmplt(cfg, ctx.CMPLT());
		else if (ctx.CMPGT() != null)
				return visitCmpgt(cfg, ctx.CMPGT());
		else if (ctx.CMPLE() != null)
				return visitCmple(cfg, ctx.CMPLE());
		else if (ctx.CMPGE() != null)
				return visitCmpge(cfg, ctx.CMPGE());
		else if (ctx.IFEQ() != null)
				return visitIfeq(cfg, ctx.IFEQ());
		else if (ctx.IFNEQ() != null)
				return visitIfneq(cfg, ctx.IFNEQ());
		else if (ctx.IFLT() != null)
				return visitIflt(cfg, ctx.IFLT());
		else if (ctx.IFGT() != null)
				return visitIfgt(cfg, ctx.IFGT());
		else if (ctx.IFLE() != null)
				return visitIfle(cfg, ctx.IFLE());
		else if (ctx.IFGE() != null)
				return visitIfge(cfg, ctx.IFGE());
		else if (ctx.IFCMPEQ() != null)
				return visitIfcmpeq(cfg, ctx.IFCMPEQ());
		else if (ctx.IFCMPNEQ() != null)
				return visitIfcmpneq(cfg, ctx.IFCMPNEQ());
		else if (ctx.IFCMPLT() != null)
				return visitIfcmplt(cfg, ctx.IFCMPLT());
		else if (ctx.IFCMPGT() != null)
				return visitIfcmpgt(cfg, ctx.IFCMPGT());
		else if (ctx.IFCMPLE() != null)
				return visitIfcmple(cfg, ctx.IFCMPLE());
		else if (ctx.IFCMPGE() != null)
				return visitIfcmpge(cfg, ctx.IFCMPGE());
		else if (ctx.ASSERT() != null)
				return visitAssert(cfg, ctx.ASSERT());
		else if (ctx.ASSERT_EQ() != null)
				return visitAssert_eq(cfg, ctx.ASSERT_EQ());
		else if (ctx.ASSERT_NEQ() != null)
				return visitAssert_neq(cfg, ctx.ASSERT_NEQ());
		else if (ctx.ASSERT_LT() != null)
				return visitAssert_lt(cfg, ctx.ASSERT_LT());
		else if (ctx.ASSERT_LE() != null)
				return visitAssert_le(cfg, ctx.ASSERT_LE());
		else if (ctx.ASSERT_GT() != null)
				return visitAssert_gt(cfg, ctx.ASSERT_GT());
		else if (ctx.ASSERT_GE() != null)
				return visitAssert_ge(cfg, ctx.ASSERT_GE());
		else if (ctx.ASSERT_CMPEQ() != null)
				return visitAssert_cmpeq(cfg, ctx.ASSERT_CMPEQ());
		else if (ctx.ASSERT_CMPNEQ() != null)
				return visitAssert_cmpneq(cfg, ctx.ASSERT_CMPNEQ());
		else if (ctx.ASSERT_CMPLT() != null)
				return visitAssert_cmplt(cfg, ctx.ASSERT_CMPLT());
		else if (ctx.ASSERT_CMPGT() != null)
				return visitAssert_cmpgt(cfg, ctx.ASSERT_CMPGT());
		else if (ctx.ASSERT_CMPLE() != null)
				return visitAssert_cmple(cfg, ctx.ASSERT_CMPLE());
		else if (ctx.ASSERT_CMPGE() != null)
				return visitAssert_cmpge(cfg, ctx.ASSERT_CMPGE());
		else if (ctx.ASSERT_NONE() != null)
				return visitAssert_none(cfg, ctx.ASSERT_NONE());
		else if (ctx.ASSERT_SOME() != null)
				return visitAssert_some(cfg, ctx.ASSERT_SOME());
		else if (ctx.ASSERT_LEFT() != null)
				return visitAssert_left(cfg, ctx.ASSERT_LEFT());
		else if (ctx.ASSERT_RIGHT() != null)
				return visitAssert_right(cfg, ctx.ASSERT_RIGHT());
		else if (ctx.FAIL() != null)
				return visitFail(cfg, ctx.FAIL());
		else 
			throw new UnsupportedOperationException("Unsupported translation: " + ctx.getText());
	}
	
	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitFail(CFG cfg, TerminalNode ctx) {
		NodeList<CFG, Statement, Edge> nL = new NodeList<>(SEQUENTIAL_SINGLETON);
		
		MichelsonUnit unit = new MichelsonUnit(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx));
		nL.addNode(unit);
		
		symbolicStacks.get(currentStack).push(new VariableRef(cfg, MichelsonFileUtils.locationOf(filepath, ctx), EXPLICIT_ABORT));
		MichelsonFailwith failWith = new MichelsonFailwith(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx));
		nL.addNode(failWith);
		
		addEdge(nL, new SequentialEdge(unit, failWith));
		
		return Triple.of(unit, nL, failWith);
	}

	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitCmpeq(CFG cfg, TerminalNode ctx) {

		List<VariableRef> elems = symbolicStacks.get(currentStack).pop(2);
		
		NodeList<CFG, Statement, Edge> ite = new NodeList<>(SEQUENTIAL_SINGLETON);

		MichelsonCompare compare =  new MichelsonCompare(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx), elems.get(0), elems.get(1));

		Assignment assign = new Assignment(cfg, MichelsonFileUtils.locationOf(filepath, ctx), compare.getStaticType(), new VariableRef(cfg, MichelsonFileUtils.locationOf(filepath, ctx), "v"+0, compare.getStaticType()), compare);
		
		symbolicStacks.get(currentStack).push((VariableRef) assign.getLeft());
		VariableRef res = symbolicStacks.get(currentStack).pop();
		
		
		Statement eq =  new MichelsonEq(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx), res);
		addEdge(ite, new SequentialEdge(compare, eq));

		return Triple.of(compare, ite, eq);
		
	}
	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitCmpneq(CFG cfg, TerminalNode ctx) {

		List<VariableRef> elems = symbolicStacks.get(currentStack).pop(2);
		
		
		NodeList<CFG, Statement, Edge> ite = new NodeList<>(SEQUENTIAL_SINGLETON);

		MichelsonCompare compare =  new MichelsonCompare(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx), elems.get(0), elems.get(1));
		
		Assignment assign = new Assignment(cfg, MichelsonFileUtils.locationOf(filepath, ctx), compare.getStaticType(), new VariableRef(cfg, MichelsonFileUtils.locationOf(filepath, ctx), "v"+0, compare.getStaticType()), compare);
		
		symbolicStacks.get(currentStack).push((VariableRef) assign.getLeft());
		VariableRef res = symbolicStacks.get(currentStack).pop();
		
		Statement neq =  new MichelsonNeq(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx), res);
		addEdge(ite, new SequentialEdge(compare, neq));

		return Triple.of(compare, ite, neq);
	}
	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitCmplt(CFG cfg, TerminalNode ctx) {
		
		List<VariableRef> elems = symbolicStacks.get(currentStack).pop(2);
		
		
		NodeList<CFG, Statement, Edge> ite = new NodeList<>(SEQUENTIAL_SINGLETON);

		MichelsonCompare compare =  new MichelsonCompare(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx), elems.get(0), elems.get(1));

		Assignment assign = new Assignment(cfg, MichelsonFileUtils.locationOf(filepath, ctx), compare.getStaticType(), new VariableRef(cfg, MichelsonFileUtils.locationOf(filepath, ctx), "v"+0, compare.getStaticType()), compare);
		
		symbolicStacks.get(currentStack).push((VariableRef) assign.getLeft());
		VariableRef res = symbolicStacks.get(currentStack).pop();
		
		Statement lt =  new MichelsonLt(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx), res);
		addEdge(ite, new SequentialEdge(compare, lt));

		return Triple.of(compare, ite, lt);
	}
	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitCmpgt(CFG cfg, TerminalNode ctx) {
		
		List<VariableRef> elems = symbolicStacks.get(currentStack).pop(2);
		
		
		NodeList<CFG, Statement, Edge> ite = new NodeList<>(SEQUENTIAL_SINGLETON);

		MichelsonCompare compare =  new MichelsonCompare(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx), elems.get(0), elems.get(1));

		Statement gt =  new MichelsonGt(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx), compare);
		addEdge(ite, new SequentialEdge(compare, gt));

		return Triple.of(compare, ite, gt);
	}
	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitCmple(CFG cfg, TerminalNode ctx) {
		
		List<VariableRef> elems = symbolicStacks.get(currentStack).pop(2);
		
		
		NodeList<CFG, Statement, Edge> ite = new NodeList<>(SEQUENTIAL_SINGLETON);

		MichelsonCompare compare =  new MichelsonCompare(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx), elems.get(0), elems.get(1));

		Assignment assign = new Assignment(cfg, MichelsonFileUtils.locationOf(filepath, ctx), compare.getStaticType(), new VariableRef(cfg, MichelsonFileUtils.locationOf(filepath, ctx), "v"+0, compare.getStaticType()), compare);
		
		symbolicStacks.get(currentStack).push((VariableRef) assign.getLeft());
		VariableRef res = symbolicStacks.get(currentStack).pop();
		
		Statement le =  new MichelsonLe(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx), res);
		addEdge(ite, new SequentialEdge(compare, le));

		return Triple.of(compare, ite, le);
	}
	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitCmpge(CFG cfg, TerminalNode ctx) {
		
		List<VariableRef> elems = symbolicStacks.get(currentStack).pop(2);
		
		
		NodeList<CFG, Statement, Edge> ite = new NodeList<>(SEQUENTIAL_SINGLETON);

		MichelsonCompare compare =  new MichelsonCompare(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx), elems.get(0), elems.get(1));

		Assignment assign = new Assignment(cfg, MichelsonFileUtils.locationOf(filepath, ctx), compare.getStaticType(), new VariableRef(cfg, MichelsonFileUtils.locationOf(filepath, ctx), "v"+0, compare.getStaticType()), compare);
		
		symbolicStacks.get(currentStack).push((VariableRef) assign.getLeft());
		VariableRef res = symbolicStacks.get(currentStack).pop();
		
		Statement ge =  new MichelsonGe(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx), res);
		addEdge(ite, new SequentialEdge(compare, ge));

		return Triple.of(compare, ite, ge);
	}
	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitIfeq(CFG cfg, TerminalNode ctx) {
		throw new UnsupportedOperationException("Macro "+ctx.getText()+" currently not supported!");
	}
	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitIfneq(CFG cfg, TerminalNode ctx) {
		throw new UnsupportedOperationException("Macro "+ctx.getText()+" currently not supported!");
	}
	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitIflt(CFG cfg, TerminalNode ctx) {
		throw new UnsupportedOperationException("Macro "+ctx.getText()+" currently not supported!");
	}
	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitIfgt(CFG cfg, TerminalNode ctx) {
		throw new UnsupportedOperationException("Macro "+ctx.getText()+" currently not supported!");
	}
	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitIfle(CFG cfg, TerminalNode ctx) {
		throw new UnsupportedOperationException("Macro "+ctx.getText()+" currently not supported!");
	}
	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitIfge(CFG cfg, TerminalNode ctx) {
		throw new UnsupportedOperationException("Macro "+ctx.getText()+" currently not supported!");
	}
	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitIfcmpeq(CFG cfg, TerminalNode ctx) {
		throw new UnsupportedOperationException("Macro "+ctx.getText()+" currently not supported!");
	}
	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitIfcmpneq(CFG cfg, TerminalNode ctx) {
		throw new UnsupportedOperationException("Macro "+ctx.getText()+" currently not supported!");
	}
	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitIfcmplt(CFG cfg, TerminalNode ctx) {
		throw new UnsupportedOperationException("Macro "+ctx.getText()+" currently not supported!");
	}
	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitIfcmpgt(CFG cfg, TerminalNode ctx) {
		throw new UnsupportedOperationException("Macro "+ctx.getText()+" currently not supported!");
	}
	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitIfcmple(CFG cfg, TerminalNode ctx) {
		throw new UnsupportedOperationException("Macro "+ctx.getText()+" currently not supported!");
	}
	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitIfcmpge(CFG cfg, TerminalNode ctx) {
		throw new UnsupportedOperationException("Macro "+ctx.getText()+" currently not supported!");
	}
	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitAssert(CFG cfg, TerminalNode ctx) {
		throw new UnsupportedOperationException("Macro "+ctx.getText()+" currently not supported!");
	}
	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitAssert_eq(CFG cfg, TerminalNode ctx) {
		throw new UnsupportedOperationException("Macro "+ctx.getText()+" currently not supported!");
	}
	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitAssert_neq(CFG cfg, TerminalNode ctx) {
		throw new UnsupportedOperationException("Macro "+ctx.getText()+" currently not supported!");
	}
	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitAssert_lt(CFG cfg, TerminalNode ctx) {
		throw new UnsupportedOperationException("Macro "+ctx.getText()+" currently not supported!");
	}
	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitAssert_le(CFG cfg, TerminalNode ctx) {
		throw new UnsupportedOperationException("Macro "+ctx.getText()+" currently not supported!");
	}
	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitAssert_gt(CFG cfg, TerminalNode ctx) {
		throw new UnsupportedOperationException("Macro "+ctx.getText()+" currently not supported!");
	}
	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitAssert_ge(CFG cfg, TerminalNode ctx) {
		throw new UnsupportedOperationException("Macro "+ctx.getText()+" currently not supported!");
	}
	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitAssert_cmpeq(CFG cfg, TerminalNode ctx) {
		throw new UnsupportedOperationException("Macro "+ctx.getText()+" currently not supported!");
	}
	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitAssert_cmpneq(CFG cfg, TerminalNode ctx) {
		throw new UnsupportedOperationException("Macro "+ctx.getText()+" currently not supported!");
	}
	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitAssert_cmplt(CFG cfg, TerminalNode ctx) {
		throw new UnsupportedOperationException("Macro "+ctx.getText()+" currently not supported!");
	}
	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitAssert_cmpgt(CFG cfg, TerminalNode ctx) {
		throw new UnsupportedOperationException("Macro "+ctx.getText()+" currently not supported!");
	}
	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitAssert_cmple(CFG cfg, TerminalNode ctx) {
		throw new UnsupportedOperationException("Macro "+ctx.getText()+" currently not supported!");
	}
	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitAssert_cmpge(CFG cfg, TerminalNode ctx) {
		throw new UnsupportedOperationException("Macro "+ctx.getText()+" currently not supported!");
	}
	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitAssert_none(CFG cfg, TerminalNode ctx) {
		throw new UnsupportedOperationException("Macro "+ctx.getText()+" currently not supported!");
	}
	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitAssert_some(CFG cfg, TerminalNode ctx) {
		throw new UnsupportedOperationException("Macro "+ctx.getText()+" currently not supported!");
	}
	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitAssert_left(CFG cfg, TerminalNode ctx) {
		throw new UnsupportedOperationException("Macro "+ctx.getText()+" currently not supported!");
	}
	private Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitAssert_right(CFG cfg, TerminalNode ctx) {
		throw new UnsupportedOperationException("Macro "+ctx.getText()+" currently not supported!");
	}


	private Expression visitSwap(TerminalNode ctx) {
		List<VariableRef> valuesToSwap = symbolicStacks.get(currentStack).pop(2);
		symbolicStacks.get(currentStack).push(valuesToSwap.get(0));
		symbolicStacks.get(currentStack).push(valuesToSwap.get(1));
		
		return new MichelsonSwap(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx));
	}

	private Expression visitSome(Some_instrContext some_instrContext) {
		VariableRef elem = symbolicStacks.get(currentStack).pop();
		return new MichelsonSome(cfg, filepath, MichelsonFileUtils.getLine(some_instrContext), MichelsonFileUtils.getCol(some_instrContext), elem);
	}

	private Expression visitUnit(Unit_instrContext unit_instrContext) {
		return new MichelsonUnit(cfg, filepath, MichelsonFileUtils.getLine(unit_instrContext), MichelsonFileUtils.getCol(unit_instrContext));
	}

	private Expression visitNever(Never_instrContext never_instrContext) {
		return new MichelsonNever(cfg, filepath, MichelsonFileUtils.getLine(never_instrContext), MichelsonFileUtils.getCol(never_instrContext));
	}

	private Pair<Expression, Object> visitCar(Car_instrContext car_instrContext) {
		VariableRef elem = symbolicStacks.get(currentStack).pop();
		InputTracker it = mapInputTracker.get(elem);
		Object track = it != null ? it.getLeft() : null;
		return Pair.of(new MichelsonCar(cfg, filepath, MichelsonFileUtils.getLine(car_instrContext), MichelsonFileUtils.getCol(car_instrContext), elem), track);
	}

	private Pair<Expression, Object> visitCdr(Cdr_instrContext cdr_instrContext) {
		VariableRef elem = symbolicStacks.get(currentStack).pop();
		InputTracker it = mapInputTracker.get(elem);
		Object track = it != null ? it.getRight() : null;
		return Pair.of(new MichelsonCdr(cfg, filepath, MichelsonFileUtils.getLine(cdr_instrContext), MichelsonFileUtils.getCol(cdr_instrContext), elem), track);
	}

	private Expression visitCons(Cons_instrContext cons_instrContext) {
		List<VariableRef> elems = symbolicStacks.get(currentStack).pop(2);
		
		return new MichelsonCons(cfg, filepath, MichelsonFileUtils.getLine(cons_instrContext), MichelsonFileUtils.getCol(cons_instrContext), elems.get(0), elems.get(1));
	}

	private Expression visitSize(Size_instrContext size_instrContext) {
		VariableRef elem = symbolicStacks.get(currentStack).pop();
		return new MichelsonSize(cfg, filepath, MichelsonFileUtils.getLine(size_instrContext), MichelsonFileUtils.getCol(size_instrContext), elem);
	}

	private Expression visitMem(Mem_instrContext mem_instrContext) {
		List<VariableRef> elems = symbolicStacks.get(currentStack).pop(2);
		
		return new MichelsonMem(cfg, filepath, MichelsonFileUtils.getLine(mem_instrContext), MichelsonFileUtils.getCol(mem_instrContext), elems.get(0), elems.get(1));
	}

	private Expression visitExec(Exec_instrContext exec_instrContext) {
		List<VariableRef> elems =  symbolicStacks.get(currentStack).pop(2);
		//TODO: handle lambda execution
		return new MichelsonExec(cfg, filepath, MichelsonFileUtils.getLine(exec_instrContext), MichelsonFileUtils.getCol(exec_instrContext), elems.get(0), elems.get(1));
	}

	private Expression visitApply(Apply_instrContext apply_instrContext) {
		List<VariableRef> elems = symbolicStacks.get(currentStack).pop(2);
		
		return new MichelsonApply(cfg, filepath, MichelsonFileUtils.getLine(apply_instrContext), MichelsonFileUtils.getCol(apply_instrContext), elems.get(0), elems.get(1));
	}

	private final String EXPLICIT_ABORT = "FAIL";
	private Expression visitFailwith(TerminalNode ctx) {
		symbolicStacks.get(currentStack).push(new VariableRef(cfg, MichelsonFileUtils.locationOf(filepath, ctx), EXPLICIT_ABORT));
		return new MichelsonFailwith(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx));
	}

	private Expression visitCast(Cast_instrContext cast_instrContext) {
		VariableRef elem = symbolicStacks.get(currentStack).pop();
		return new MichelsonCast(cfg, filepath, MichelsonFileUtils.getLine(cast_instrContext), MichelsonFileUtils.getCol(cast_instrContext), elem);
	}

	private Expression visitRename(Rename_instrContext rename_instrContext) {
		return new MichelsonRename(cfg, filepath, MichelsonFileUtils.getLine(rename_instrContext), MichelsonFileUtils.getCol(rename_instrContext));
	}

	private Expression visitConcat(Concat_instrContext concat_instrContext) {
		
		
		VariableRef topElement = symbolicStacks.get(currentStack).pop();
		
		if(topElement.getStaticType() instanceof MichelsonStringType || topElement.getStaticType() instanceof MichelsonBytesType) {
			VariableRef secondElement = symbolicStacks.get(currentStack).pop();
			return new MichelsonConcat(cfg, filepath, MichelsonFileUtils.getLine(concat_instrContext), MichelsonFileUtils.getCol(concat_instrContext), topElement, secondElement);
		}else {
			//list
			return new MichelsonConcat(cfg, filepath, MichelsonFileUtils.getLine(concat_instrContext), MichelsonFileUtils.getCol(concat_instrContext), topElement);
		}
	}

	private Expression visitSlice(Slice_instrContext slice_instrContext) {
		List<VariableRef> elems = symbolicStacks.get(currentStack).pop(3);
		
		return new MichelsonSlice(cfg, filepath, MichelsonFileUtils.getLine(slice_instrContext), MichelsonFileUtils.getCol(slice_instrContext), elems.get(0), elems.get(1), elems.get(2));
	}

	private Expression visitPack(Pack_instrContext pack_instrContext) {
		VariableRef elem = symbolicStacks.get(currentStack).pop();
		return new MichelsonPack(cfg, filepath, MichelsonFileUtils.getLine(pack_instrContext), MichelsonFileUtils.getCol(pack_instrContext), elem);
	}

	private Expression visitAdd(Add_instrContext add_instrContext) {
		List<VariableRef> elems = symbolicStacks.get(currentStack).pop(2);
		
		return new MichelsonAdd(cfg, filepath, MichelsonFileUtils.getLine(add_instrContext), MichelsonFileUtils.getCol(add_instrContext), elems.get(0), elems.get(1));
	}

	private Expression visitSub(Sub_instrContext sub_instrContext) {
		List<VariableRef> elems = symbolicStacks.get(currentStack).pop(2);
		
		return new MichelsonSub(cfg, filepath, MichelsonFileUtils.getLine(sub_instrContext), MichelsonFileUtils.getCol(sub_instrContext), elems.get(0), elems.get(1));
	}

	private Expression visitMul(Mul_instrContext mul_instrContext) {
		List<VariableRef> elems = symbolicStacks.get(currentStack).pop(2);
		
		return new MichelsonMul(cfg, filepath, MichelsonFileUtils.getLine(mul_instrContext), MichelsonFileUtils.getCol(mul_instrContext), elems.get(0), elems.get(1));
	}

	private Expression visitEdiv(Ediv_instrContext ediv_instrContext) {
		List<VariableRef> elems = symbolicStacks.get(currentStack).pop(2);
		
		return new MichelsonEdiv(cfg, filepath, MichelsonFileUtils.getLine(ediv_instrContext), MichelsonFileUtils.getCol(ediv_instrContext), elems.get(0), elems.get(1));
	}

	private Expression visitAbs(Abs_instrContext abs_instrContext) {
		VariableRef elem = symbolicStacks.get(currentStack).pop();
		return new MichelsonAbs(cfg, filepath, MichelsonFileUtils.getLine(abs_instrContext), MichelsonFileUtils.getCol(abs_instrContext), elem);
	}

	private Expression visitIsnat(Isnat_instrContext isnat_instrContext) {
		VariableRef elem = symbolicStacks.get(currentStack).pop();
		return new MichelsonIsnat(cfg, filepath, MichelsonFileUtils.getLine(isnat_instrContext), MichelsonFileUtils.getCol(isnat_instrContext), elem);
	}

	private Expression visitInt(Int_instrContext int_instrContext) {
		VariableRef elem = symbolicStacks.get(currentStack).pop();
		return new MichelsonInt(cfg, filepath, MichelsonFileUtils.getLine(int_instrContext), MichelsonFileUtils.getCol(int_instrContext), elem);
	}

	private Expression visitNeg(Neg_instrContext neg_instrContext) {
		VariableRef elem = symbolicStacks.get(currentStack).pop();
		return new MichelsonNeg(cfg, filepath, MichelsonFileUtils.getLine(neg_instrContext), MichelsonFileUtils.getCol(neg_instrContext), elem);
	}

	private Expression visitLsl(Lsl_instrContext lsl_instrContext) {
		 List<VariableRef> values = symbolicStacks.get(currentStack).pop(2);
		return new MichelsonLsl(cfg, filepath, MichelsonFileUtils.getLine(lsl_instrContext), MichelsonFileUtils.getCol(lsl_instrContext), values.get(0), values.get(1));
	}

	private Expression visitLsr(Lsr_instrContext lsr_instrContext) {
		List<VariableRef> values = symbolicStacks.get(currentStack).pop(2);
		return new MichelsonLsr(cfg, filepath, MichelsonFileUtils.getLine(lsr_instrContext), MichelsonFileUtils.getCol(lsr_instrContext), values.get(0), values.get(1));
	}

	private Expression visitOr(Or_instrContext or_instrContext) {
		List<VariableRef> elems = symbolicStacks.get(currentStack).pop(2);
		
		return new MichelsonOr(cfg, filepath, MichelsonFileUtils.getLine(or_instrContext), MichelsonFileUtils.getCol(or_instrContext), elems.get(0), elems.get(1));
	}

	private Expression visitAnd(And_instrContext and_instrContext) {
		List<VariableRef> elems = symbolicStacks.get(currentStack).pop(2);
		
		return new MichelsonAnd(cfg, filepath, MichelsonFileUtils.getLine(and_instrContext), MichelsonFileUtils.getCol(and_instrContext), elems.get(0), elems.get(1));
	}

	private Expression visitXor(Xor_instrContext xor_instrContext) {
		List<VariableRef> elems = symbolicStacks.get(currentStack).pop(2);
		
		return new MichelsonXor(cfg, filepath, MichelsonFileUtils.getLine(xor_instrContext), MichelsonFileUtils.getCol(xor_instrContext), elems.get(0), elems.get(1));
	}

	private Expression visitNot(Not_instrContext not_instrContext) {
		VariableRef elem = symbolicStacks.get(currentStack).pop();
		return new MichelsonNot(cfg, filepath, MichelsonFileUtils.getLine(not_instrContext), MichelsonFileUtils.getCol(not_instrContext), elem);
	}

	private Expression visitCompare(Compare_instrContext compare_instrContext) {
		List<VariableRef> elems = symbolicStacks.get(currentStack).pop(2);
		
		return new MichelsonCompare(cfg, filepath, MichelsonFileUtils.getLine(compare_instrContext), MichelsonFileUtils.getCol(compare_instrContext), elems.get(0), elems.get(1));
	}

	private Expression visitEq(Eq_instrContext eq_instrContext) {
		VariableRef elem = symbolicStacks.get(currentStack).pop();
		return new MichelsonEq(cfg, filepath, MichelsonFileUtils.getLine(eq_instrContext), MichelsonFileUtils.getCol(eq_instrContext), elem);
	}

	private Expression visitNeq(Neq_instrContext neq_instrContext) {
		VariableRef elem = symbolicStacks.get(currentStack).pop();
		return new MichelsonNeq(cfg, filepath, MichelsonFileUtils.getLine(neq_instrContext), MichelsonFileUtils.getCol(neq_instrContext), elem);
	}

	private Expression visitLt(Lt_instrContext lt_instrContext) {
		VariableRef elem = symbolicStacks.get(currentStack).pop();
		return new MichelsonLt(cfg, filepath, MichelsonFileUtils.getLine(lt_instrContext), MichelsonFileUtils.getCol(lt_instrContext), elem);
	}

	private Expression visitGt(Gt_instrContext gt_instrContext) {
		VariableRef elem = symbolicStacks.get(currentStack).pop();
		return new MichelsonGt(cfg, filepath, MichelsonFileUtils.getLine(gt_instrContext), MichelsonFileUtils.getCol(gt_instrContext), elem);
	}

	private Expression visitLe(Le_instrContext le_instrContext) {
		VariableRef elem = symbolicStacks.get(currentStack).pop();
		return new MichelsonLe(cfg, filepath, MichelsonFileUtils.getLine(le_instrContext), MichelsonFileUtils.getCol(le_instrContext), elem);
	}

	private Expression visitGe(Ge_instrContext ge_instrContext) {
		VariableRef elem = symbolicStacks.get(currentStack).pop();
		return new MichelsonGe(cfg, filepath, MichelsonFileUtils.getLine(ge_instrContext), MichelsonFileUtils.getCol(ge_instrContext), elem);
	}

	private Expression visitSelf(Self_instrContext self_instrContext) {
		return new MichelsonSelf(cfg, filepath, MichelsonFileUtils.getLine(self_instrContext), MichelsonFileUtils.getCol(self_instrContext), selfType);
	}

	private Expression visitSelf_address(Self_address_instrContext self_address_instrContext) {
		return new MichelsonSelf_address(cfg, filepath, MichelsonFileUtils.getLine(self_address_instrContext),
				MichelsonFileUtils.getCol(self_address_instrContext));
	}

	private Expression visitTransfer_tokens(Transfer_tokens_instrContext transfer_tokens_instrContext) {
		List<VariableRef> elems = symbolicStacks.get(currentStack).pop(3);
		
		return new MichelsonTransfer_tokens(cfg, filepath, MichelsonFileUtils.getLine(transfer_tokens_instrContext),
				MichelsonFileUtils.getCol(transfer_tokens_instrContext), elems.get(0), elems.get(1), elems.get(2));
	}

	private Expression visitSet_delegate(Set_delegate_instrContext set_delegate_instrContext) {
		VariableRef elem = symbolicStacks.get(currentStack).pop();
		return new MichelsonSet_delegate(cfg, filepath, MichelsonFileUtils.getLine(set_delegate_instrContext),
				MichelsonFileUtils.getCol(set_delegate_instrContext), elem);
	}

	private Expression visitImplicit_account(Implicit_account_instrContext implicit_account_instrContext) {
		VariableRef elem = symbolicStacks.get(currentStack).pop();
		return new MichelsonImplicit_account(cfg, filepath, MichelsonFileUtils.getLine(implicit_account_instrContext),
				MichelsonFileUtils.getCol(implicit_account_instrContext), elem);
	}

	private Expression visitVoting_power(Voting_power_instrContext voting_power_instrContext) {
		return new MichelsonVoting_power(cfg, filepath, MichelsonFileUtils.getLine(voting_power_instrContext),
				MichelsonFileUtils.getCol(voting_power_instrContext));
	}

	private Expression visitNow(Now_instrContext now_instrContext) {
		return new MichelsonNow(cfg, filepath, MichelsonFileUtils.getLine(now_instrContext), MichelsonFileUtils.getCol(now_instrContext));
	}

	private Expression visitLevel(Level_instrContext level_instrContext) {
		return new MichelsonLevel(cfg, filepath, MichelsonFileUtils.getLine(level_instrContext), MichelsonFileUtils.getCol(level_instrContext));
	}

	private Expression visitAmount(Amount_instrContext amount_instrContext) {
		return new MichelsonAmount(cfg, filepath, MichelsonFileUtils.getLine(amount_instrContext), MichelsonFileUtils.getCol(amount_instrContext));
	}

	private Expression visitBalance(Balance_instrContext balance_instrContext) {
		return new MichelsonBalance(cfg, filepath, MichelsonFileUtils.getLine(balance_instrContext), MichelsonFileUtils.getCol(balance_instrContext));
	}

	private Expression visitCheck_signature(Check_signature_instrContext check_signature_instrContext) {
		List<VariableRef> elems = symbolicStacks.get(currentStack).pop(3);
		
		return new MichelsonCheck_signature(cfg, filepath, MichelsonFileUtils.getLine(check_signature_instrContext),
				MichelsonFileUtils.getCol(check_signature_instrContext), elems.get(0), elems.get(1), elems.get(2));
	}

	private Expression visitBlake2b(Blake2b_instrContext blake2b_instrContext) {
		VariableRef elem = symbolicStacks.get(currentStack).pop();
		return new MichelsonBlake2b(cfg, filepath, MichelsonFileUtils.getLine(blake2b_instrContext), MichelsonFileUtils.getCol(blake2b_instrContext), elem);
	}

	private Expression visitKeccak(Keccak_instrContext keccak_instrContext) {
		VariableRef elem = symbolicStacks.get(currentStack).pop();
		return new MichelsonKeccak(cfg, filepath, MichelsonFileUtils.getLine(keccak_instrContext), MichelsonFileUtils.getCol(keccak_instrContext), elem);
	}

	private Expression visitSha3(Sha3_instrContext sha3_instrContext) {
		VariableRef elem = symbolicStacks.get(currentStack).pop();
		return new MichelsonSha3(cfg, filepath, MichelsonFileUtils.getLine(sha3_instrContext), MichelsonFileUtils.getCol(sha3_instrContext), elem);
	}

	private Expression visitSha256(Sha256_instrContext sha256_instrContext) {
		VariableRef elem = symbolicStacks.get(currentStack).pop();
		return new MichelsonSha256(cfg, filepath, MichelsonFileUtils.getLine(sha256_instrContext), MichelsonFileUtils.getCol(sha256_instrContext), elem);
	}

	private Expression visitSha512(Sha512_instrContext sha512_instrContext) {
		VariableRef elem = symbolicStacks.get(currentStack).pop();
		return new MichelsonSha512(cfg, filepath, MichelsonFileUtils.getLine(sha512_instrContext), MichelsonFileUtils.getCol(sha512_instrContext), elem);
	}

	private Expression visitHash_key(Hash_key_instrContext hash_key_instrContext) {
		VariableRef elem = symbolicStacks.get(currentStack).pop();
		return new MichelsonHash_key(cfg, filepath, MichelsonFileUtils.getLine(hash_key_instrContext), MichelsonFileUtils.getCol(hash_key_instrContext), elem);
	}

	private Expression visitSource(Source_instrContext source_instrContext) {
		return new MichelsonSource(cfg, filepath, MichelsonFileUtils.getLine(source_instrContext), MichelsonFileUtils.getCol(source_instrContext));
	}

	private Expression visitSender(Sender_instrContext sender_instrContext) {
		return new MichelsonSender(cfg, filepath, MichelsonFileUtils.getLine(sender_instrContext), MichelsonFileUtils.getCol(sender_instrContext));
	}

	private Expression visitAddress(Address_instrContext address_instrContext) {
		VariableRef ref = symbolicStacks.get(currentStack).pop();
		return new MichelsonAddress(cfg, filepath, MichelsonFileUtils.getLine(address_instrContext), MichelsonFileUtils.getCol(address_instrContext), ref);
	}

	private Expression visitChain_id(Chain_id_instrContext chain_id_instrContext) {
		return new MichelsonChain_id(cfg, filepath, MichelsonFileUtils.getLine(chain_id_instrContext), MichelsonFileUtils.getCol(chain_id_instrContext));
	}

	private Expression visitTotal_voting_power(Total_voting_power_instrContext total_voting_power_instrContext) {
		return new MichelsonTotal_voting_power(cfg, filepath, MichelsonFileUtils.getLine(total_voting_power_instrContext),
				MichelsonFileUtils.getCol(total_voting_power_instrContext));
	}

	private Expression visitPairing_check(Pairing_check_instrContext pairing_check_instrContext) {
		VariableRef elem = symbolicStacks.get(currentStack).pop();
		return new MichelsonPairing_check(cfg, filepath, MichelsonFileUtils.getLine(pairing_check_instrContext),
				MichelsonFileUtils.getCol(pairing_check_instrContext), elem);
	}

	private Expression visitSapling_verify_update(Sapling_verify_update_instrContext sapling_verify_update_instrContext) {
		List<VariableRef> elems = symbolicStacks.get(currentStack).pop(2);
		
		return new MichelsonSapling_verify_update(cfg, filepath, MichelsonFileUtils.getLine(sapling_verify_update_instrContext),
				MichelsonFileUtils.getCol(sapling_verify_update_instrContext), elems.get(0), elems.get(1));
	}

	private Expression visitTicket(Ticket_instrContext ticket_instrContext) {
		List<VariableRef> elems = symbolicStacks.get(currentStack).pop(2);
		return new MichelsonTicket(cfg, filepath, MichelsonFileUtils.getLine(ticket_instrContext), MichelsonFileUtils.getCol(ticket_instrContext), elems.get(0), elems.get(1));
	}

	private Expression visitRead_ticket(Read_ticket_instrContext read_ticket_instrContext) {
		VariableRef ticket = symbolicStacks.get(currentStack).pop();
		
		return new MichelsonRead_ticket(cfg, filepath, MichelsonFileUtils.getLine(read_ticket_instrContext), MichelsonFileUtils.getCol(read_ticket_instrContext), ticket);
	}

	private Expression visitSplit_ticket(Split_ticket_instrContext split_ticket_instrContext) {
		VariableRef elem = symbolicStacks.get(currentStack).pop();
		return new MichelsonSplit_ticket(cfg, filepath, MichelsonFileUtils.getLine(split_ticket_instrContext),
				MichelsonFileUtils.getCol(split_ticket_instrContext), elem);
	}

	private Expression visitJoin_tickets(Join_tickets_instrContext join_tickets_instrContext) {
		List<VariableRef> elems = symbolicStacks.get(currentStack).pop(2);
		
		return new MichelsonJoin_tickets(cfg, filepath, MichelsonFileUtils.getLine(join_tickets_instrContext),
				MichelsonFileUtils.getCol(join_tickets_instrContext), elems.get(0), elems.get(1));
	}

	private Expression visitOpen_chest(Open_chest_instrContext open_chest_instrContext) {
		return new MichelsonOpen_chest(cfg, filepath, MichelsonFileUtils.getLine(open_chest_instrContext), MichelsonFileUtils.getCol(open_chest_instrContext));
	}

	public Expression visitDrop_instr(Drop_instrContext ctx) {
		if (ctx.NAL_CONST() == null) {
			symbolicStacks.get(currentStack).pop();
			return new MichelsonDrop(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx));
		} else {
			MichelsonNaturalData nat = (MichelsonNaturalData) visitNatConst(ctx.NAL_CONST());
			symbolicStacks.get(currentStack).pop(nat.getValue().intValueExact());
			return new MichelsonDrop(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx), nat);
		}
	}

	public Pair<Expression, Object> visitDup_instr(Dup_instrContext ctx) {
		
		if (ctx.NAL_CONST() == null) {
			VariableRef elem = symbolicStacks.get(currentStack).get((symbolicStacks.get(currentStack).size() - symbolicStacks.get(currentStack).getTopProtectedSlots()) -1);
			InputTracker it = mapInputTracker.get(elem);
			return Pair.of(new MichelsonDup(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx), elem), it);
		}else {
			MichelsonNaturalData nat = (MichelsonNaturalData) visitNatConst(ctx.NAL_CONST());
			VariableRef elem = symbolicStacks.get(currentStack).get(((symbolicStacks.get(currentStack).size() - symbolicStacks.get(currentStack).getTopProtectedSlots()) - nat.getValue().intValueExact()));
			InputTracker it = mapInputTracker.get(elem);
			return Pair.of(new MichelsonDup(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx), nat, elem), it);
		}
	}

	public Expression visitDig_instr(Dig_instrContext ctx) {

		int n = 1;
		
		Expression e;
		if (ctx.NAL_CONST() == null) {
			e =  new MichelsonDig(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx));
		}else {
			n = Integer.parseInt(ctx.NAL_CONST().getText())+1;
			e =  new MichelsonDig(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx),
					(MichelsonNaturalData) visitNatConst(ctx.NAL_CONST()));
		}
		
		List<VariableRef> pops = null;
		if(n > 0)
			pops = symbolicStacks.get(currentStack).pop(n);
		
		if(pops != null) {
			for(int i = pops.size()-2; i >= 0; i--)
				symbolicStacks.get(currentStack).push(pops.get(i));
			symbolicStacks.get(currentStack).push(pops.get(pops.size()-1));
		}
		return e;
	}

	public Expression visitDug_instr(Dug_instrContext ctx) {
		int n = 1;
		
		Expression e;
		if (ctx.NAL_CONST() == null) {
			e = new MichelsonDug(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx));
		}else {
			n = Integer.parseInt(ctx.NAL_CONST().getText())+1;
			e =  new MichelsonDug(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx),
					(MichelsonNaturalData) visitNatConst(ctx.NAL_CONST()));
		}
		
		List<VariableRef> pops = null;
		if(n > 0)
			pops = symbolicStacks.get(currentStack).pop(n);
		
		if(pops != null) {
			symbolicStacks.get(currentStack).push(pops.get(0));
			
			for(int i = pops.size()-1; i > 0; i--)
				symbolicStacks.get(currentStack).push(pops.get(i));
		}

		return e;
	}

	public Expression visitPush_instr(Push_instrContext ctx) {
		
		MichelsonTypeExpression type = visitType(ctx.type());
		
		Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> data = visitData(ctx.data());

		return new MichelsonPush(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx), type.getValue(), data != null ? (Expression) data.getRight() : new EmptyStructure(cfg, MichelsonFileUtils.locationOf(filepath, ctx)));
	}

	public Expression visitNone_instr(None_instrContext ctx) {
		MichelsonTypeExpression type = visitType(ctx.type());
		return new MichelsonNone(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx), type.getValue());
	}

	public Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitIf_none_instr(
			If_none_instrContext ctx) {
		NodeList<CFG, Statement, Edge> nL = new NodeList<>(SEQUENTIAL_SINGLETON);

		VariableRef elem = symbolicStacks.get(currentStack).pop();
		
		ExtractValueFromOption extract = new ExtractValueFromOption(cfg, MichelsonFileUtils.locationOf(filepath, ctx), elem);
		Assignment ifGuard = buildAssignment(extract, extract.getStaticType(), false);
		nL.addNode(ifGuard);
		
		int stackBeforeBranches = currentStack;
		
		MichelsonStack<VariableRef> copy = MichelsonStack.copy(symbolicStacks.get(currentStack));
		symbolicStacks.add(copy);
		currentStack++;

		Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> then = visitBlock(ctx.block(0));
		
		if(then != null) {
			nL.mergeWith(then.getMiddle());
			addEdge(nL, new TrueEdge(ifGuard, then.getLeft()));
		}

		int stackAfterTrueBranch = currentStack;
		
		int stackAfterFalseBranch = stackBeforeBranches;
		
		Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> otherwise = null;
		if (ctx.block(1) != null && ctx.block(1) != null) {
			MichelsonStack<VariableRef> copy2 = MichelsonStack.copy(symbolicStacks.get(stackAfterFalseBranch));
			symbolicStacks.add(copy2);
			currentStack++;
			
			symbolicStacks.get(currentStack).push((VariableRef) ifGuard.getLeft());
			
			otherwise = visitBlock(ctx.block(1));
			stackAfterFalseBranch = currentStack;
			if(otherwise != null) {
				nL.mergeWith(otherwise.getMiddle());
				addEdge(nL, new FalseEdge(ifGuard, otherwise.getLeft()));
			}
	
		}
			
		Pair<MichelsonStack<VariableRef>,List<Assignment>> jp = junctionPointForStacks(ctx, stackAfterTrueBranch, stackAfterFalseBranch);
		
		symbolicStacks.add(jp.getLeft());
		currentStack++;	
		
		Pair<Statement, Statement> junctionAndEndNodes = computeJunctionAndEndNode(jp.getRight(), nL, MichelsonFileUtils.locationOf(filepath, ctx));
		
		if (then != null)
			addEdge(nL, new SequentialEdge(then.getRight(), junctionAndEndNodes.getLeft()));
		else
			addEdge(nL, new TrueEdge(ifGuard, junctionAndEndNodes.getLeft()));
		
		if (otherwise != null)
			addEdge(nL, new SequentialEdge(otherwise.getRight(), junctionAndEndNodes.getLeft()));
		else
			addEdge(nL, new FalseEdge(ifGuard, junctionAndEndNodes.getLeft()));

		cfs.add(new IfThenElse(nL, ifGuard, junctionAndEndNodes.getRight(), then != null ? then.getMiddle().getNodes() : Collections.emptyList(),
				otherwise == null ? Collections.emptyList() : otherwise.getMiddle().getNodes()));

		return Triple.of(ifGuard, nL, junctionAndEndNodes.getRight());
	}

	public Expression visitPair_instr(Pair_instrContext ctx) {
		
		List<VariableRef> elems = symbolicStacks.get(currentStack).pop(2);
		
		
		if (ctx.NAL_CONST() == null)
			return new MichelsonPair(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx), elems.get(0), elems.get(1));
		else {
			return new MichelsonPair(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx), buildNExpressionArray((MichelsonNaturalData) visitNatConst(ctx.NAL_CONST())));
		}
	}
	
	private Expression[] buildNExpressionArray(MichelsonNaturalData nat) {
		Expression[] exprs = new Expression[nat.getValue().intValueExact()+1];
		exprs[0] = nat;
		for(int i= 1; i< exprs.length; i++)
			exprs[i] = symbolicStacks.get(currentStack).pop();
		return exprs;
	} 

	public Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitUnpair_instr(Unpair_instrContext ctx) {
		VariableRef elem = symbolicStacks.get(currentStack).pop();
		
		InputTracker inputTracker = mapInputTracker.get(elem);

		MichelsonUnpair e;
		MichelsonNaturalData nat = null;
		if (ctx.NAL_CONST() == null) {
			e =  new MichelsonUnpair(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx), elem);
		}else {
			nat = (MichelsonNaturalData) visitNatConst(ctx.NAL_CONST());
			e = new MichelsonUnpair(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx),
					buildNExpressionArray(nat));
			//FIXME: c'è un bug perchè se faccio la unpair di N poi devo aggiungere N allo stack e non 2 valori e basta
		}
		
		NodeList<CFG, Statement, Edge> nL = new NodeList<>(SEQUENTIAL_SINGLETON);
		
		Assignment left = buildAssignment(new LeftUNPAIR(cfg, MichelsonFileUtils.locationOf(filepath, ctx), elem));
		Assignment right = buildAssignment(new RightUNPAIR(cfg, MichelsonFileUtils.locationOf(filepath, ctx), elem));
		
		mapAssignamentNode.put((VariableRef) left.getLeft(), left);
		mapAssignamentNode.put((VariableRef) right.getLeft(), right);

		//FIXME: c'è un bug perchè se faccio la unpair di N poi devo aggiungere N allo stack e non 2 valori e basta
		List<Object> list = nat != null ? inputTracker.getUnpair(nat.getValue().intValueExact()) : inputTracker.getUnpair();
		checkPossibleInput((VariableRef)left.getLeft(), list.get(0));
		checkPossibleInput((VariableRef)right.getLeft(), list.get(1));
		
		nL.addNode(e);
		nL.addNode(left);
		nL.addNode(right);
		
		addEdge(nL, new SequentialEdge(e, left));
		addEdge(nL, new SequentialEdge(left, right));

		return Triple.of(e, nL, right);
	}

	private void checkPossibleInput(VariableRef ref, Object obj) {
		if(obj != null) {
			if(obj instanceof InputTracker)
				mapInputTracker.put(ref, (InputTracker) obj);
			else if(obj instanceof InputAnnotation)
				mapEntryPoints.put(ref, (InputAnnotation) obj);
		}
	}

	public Expression visitLeft_instr(Left_instrContext ctx) {
		VariableRef elem = symbolicStacks.get(currentStack).pop();
		if(ctx.type() == null)
			throw new UnsupportedOperationException("Unsupported translation: " + ctx.getText() +" missing the value type");
		
		return new MichelsonLeft(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx), visitType(ctx.type()).getStaticType(), elem);

	}

	public Expression visitRight_instr(
			Right_instrContext ctx) {
		
		VariableRef elem = symbolicStacks.get(currentStack).pop();
		if(ctx.type() == null)
			throw new UnsupportedOperationException("Unsupported translation: " + ctx.getText() +" missing the value type");
		
		return new MichelsonRight(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx), visitType(ctx.type()).getStaticType(), elem);

	}

	public Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitIf_left_instr(
			If_left_instrContext ctx) {
		NodeList<CFG, Statement, Edge> nL = new NodeList<>(SEQUENTIAL_SINGLETON);

		VariableRef elem = symbolicStacks.get(currentStack).pop();
		
		Assignment ifGuard = buildAssignment(new ConsumeLEFT_RIGHT_ProduceOr(cfg, MichelsonFileUtils.locationOf(filepath, ctx), elem));
		nL.addNode(ifGuard);
		
		int stackBeforeBranches = currentStack;
		
		MichelsonStack<VariableRef> copy = MichelsonStack.copy(symbolicStacks.get(currentStack));
		symbolicStacks.add(copy);
		currentStack++;
		
		Assignment getLeft = buildAssignment(new GetLeft(cfg, MichelsonFileUtils.locationOf(filepath, ctx), symbolicStacks.get(currentStack).pop()));
		nL.addNode(getLeft);
		addEdge(nL, new TrueEdge(ifGuard, getLeft));
		
		Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> then = visitBlock(ctx.block(0));
		
		if(then != null) {
			nL.mergeWith(then.getMiddle());
			addEdge(nL, new SequentialEdge(getLeft, then.getLeft()));
		}


		int stackAfterTrueBranch = currentStack;
		
		int stackAfterFalseBranch = stackBeforeBranches;
		
		Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> otherwise = null;
		
		MichelsonStack<VariableRef> copy2 = MichelsonStack.copy(symbolicStacks.get(stackAfterFalseBranch));
		symbolicStacks.add(copy2);
		currentStack++;
		
		Assignment getRight = buildAssignment(new GetRight(cfg, MichelsonFileUtils.locationOf(filepath, ctx), symbolicStacks.get(currentStack).pop()));
		nL.addNode(getRight);
		addEdge(nL, new FalseEdge(ifGuard, getRight));
		
		if (ctx.block(1) != null && ctx.block(1) != null) {
			otherwise = visitBlock(ctx.block(1));
			stackAfterFalseBranch = currentStack;
			
			if(otherwise != null) {
				nL.mergeWith(otherwise.getMiddle());
				addEdge(nL, new SequentialEdge(getRight, otherwise.getLeft()));
			}
		}
			
		Pair<MichelsonStack<VariableRef>,List<Assignment>> jp = junctionPointForStacks(ctx, stackAfterTrueBranch, stackAfterFalseBranch);
		
		symbolicStacks.add(jp.getLeft());
		currentStack++;	
			
		Pair<Statement, Statement> junctionAndEndNodes = computeJunctionAndEndNode(jp.getRight(), nL, MichelsonFileUtils.locationOf(filepath, ctx));

		if (then != null)
			addEdge(nL, new SequentialEdge(then.getRight(), junctionAndEndNodes.getLeft()));
		else
			addEdge(nL, new TrueEdge(getLeft, junctionAndEndNodes.getLeft()));
		
		if (otherwise != null)
			addEdge(nL, new SequentialEdge(otherwise.getRight(), junctionAndEndNodes.getLeft()));
		else
			addEdge(nL, new FalseEdge(getRight, junctionAndEndNodes.getLeft()));

		Collection<Statement> thenNodes = new TreeSet<>();
		thenNodes.add(getLeft);
		if(then != null)
			thenNodes.addAll(then.getMiddle().getNodes());
		
		Collection<Statement> otherwiseNodes = new TreeSet<>();
		otherwiseNodes.add(getRight);
		if(otherwise != null)
			otherwiseNodes.addAll(otherwise.getMiddle().getNodes());

		cfs.add(new IfThenElse(nL, ifGuard, junctionAndEndNodes.getRight(), thenNodes, otherwiseNodes));

		return Triple.of(ifGuard, nL, junctionAndEndNodes.getRight());
	}

	private Pair<Statement, Statement> computeJunctionAndEndNode(List<Assignment> phiList,
			NodeList<CFG, Statement, Edge> nL, SourceCodeLocation location) {
		
		Statement endNode = null;
		Statement junctionNode = null;
		
		if(!phiList.isEmpty()) {
			
			phiList.forEach(n -> nL.addNode(n));
			
			for(int i=0; i < phiList.size()-1; i++)
				addEdge(nL, new SequentialEdge(phiList.get(i), phiList.get(i+1)));

			junctionNode = phiList.get(0);
			endNode = new NoOp(cfg, location);
			nL.addNode(endNode);
			nL.addEdge(new SequentialEdge(phiList.get(phiList.size()-1), endNode));
		} else {
			endNode = new NoOp(cfg, location);
			junctionNode = endNode;
			nL.addNode(endNode);
		}
		
		return Pair.of(junctionNode, endNode);
	}

	public Expression visitNil_instr(Nil_instrContext ctx) {
		if(ctx.type() == null)
			throw new UnsupportedOperationException("Unsupported translation: " + ctx.getText() +" missing type for the values");
		
		return new MichelsonNil(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx), visitType(ctx.type()).getStaticType());
	}

	public Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitIf_cons_instr(
			If_cons_instrContext ctx) {

		NodeList<CFG, Statement, Edge> nL = new NodeList<>(SEQUENTIAL_SINGLETON);

		VariableRef elem = symbolicStacks.get(currentStack).firstElement();
		
		Statement listGuard = mapAssignamentNode.get(elem);
		nL.addNode(listGuard);
		
		int stackBeforeBranches = currentStack;
		
		MichelsonStack<VariableRef> copy = MichelsonStack.copy(symbolicStacks.get(currentStack));
		symbolicStacks.add(copy);
		currentStack++;

		GetValueFromList getValueFromList = new GetValueFromList(cfg, MichelsonFileUtils.locationOf(filepath, ctx), elem);
		Assignment firstValueOfList = buildAssignment(getValueFromList);

		nL.addNode(firstValueOfList);
		
		Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> then = visitBlock(ctx.block(0));
		
		if(then != null) {
			nL.mergeWith(then.getMiddle());
			addEdge(nL, new SequentialEdge(firstValueOfList, then.getLeft()));
			
		}

		addEdge(nL, new TrueEdge(listGuard, firstValueOfList));
		
		int stackAfterTrueBranch = currentStack;
		
		int stackAfterFalseBranch = stackBeforeBranches;
		
		Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> otherwise = null;
		if (ctx.block(1) != null && ctx.block(1) != null) {
			MichelsonStack<VariableRef> copy2 = MichelsonStack.copy(symbolicStacks.get(stackAfterFalseBranch));
			symbolicStacks.add(copy2);
			currentStack++;
			
			symbolicStacks.get(currentStack).pop();
			otherwise = visitBlock(ctx.block(1));
			stackAfterFalseBranch = currentStack;
			
			if(otherwise != null) {
				nL.mergeWith(otherwise.getMiddle());
				addEdge(nL, new FalseEdge(listGuard, otherwise.getLeft()));
			}
	
		}
			
		Pair<MichelsonStack<VariableRef>,List<Assignment>> jp = junctionPointForStacks(ctx, stackAfterTrueBranch, stackAfterFalseBranch);
		
		symbolicStacks.add(jp.getLeft());
		currentStack++;	
		
		Pair<Statement, Statement> junctionAndEndNodes = computeJunctionAndEndNode(jp.getRight(), nL, MichelsonFileUtils.locationOf(filepath, ctx));
		
		if (then != null)
			addEdge(nL, new SequentialEdge(then.getRight(), junctionAndEndNodes.getLeft()));
		else
			addEdge(nL, new TrueEdge(listGuard, junctionAndEndNodes.getLeft()));
		
		if (otherwise != null)
			addEdge(nL, new SequentialEdge(otherwise.getRight(), junctionAndEndNodes.getLeft()));
		else
			addEdge(nL, new FalseEdge(listGuard, junctionAndEndNodes.getLeft()));

		cfs.add(new IfThenElse(nL, listGuard, junctionAndEndNodes.getRight(), then != null ? then.getMiddle().getNodes() : Collections.emptyList(),
				otherwise == null ? Collections.emptyList() : otherwise.getMiddle().getNodes()));

		return Triple.of(listGuard, nL, junctionAndEndNodes.getRight());	
	}

	public Expression visitEmpty_set_instr(
			Empty_set_instrContext ctx) {
		if(ctx.comparable_type() == null)
			throw new UnsupportedOperationException("Unsupported translation: " + ctx.getText() +" missing comparable type for the values");
		
		return new MichelsonEmptySet(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx),  visitComparable_type(ctx.comparable_type()).getStaticType());

	}

	public Expression visitEmpty_map_instr(
			Empty_map_instrContext ctx) {
		if(ctx.comparable_type() == null)
			throw new UnsupportedOperationException("Unsupported translation: " + ctx.getText() +" missing comparable type for the key");
		if(ctx.type() == null)
			throw new UnsupportedOperationException("Unsupported translation: " + ctx.getText() +" missing type for the values");
		
		return new MichelsonEmptyMap(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx),  visitComparable_type(ctx.comparable_type()).getStaticType(), visitType(ctx.type()).getStaticType());
		
	}

	public Expression visitEmpty_big_map_instr(
			Empty_big_map_instrContext ctx) {
		if(ctx.comparable_type() == null)
			throw new UnsupportedOperationException("Unsupported translation: " + ctx.getText() +" missing comparable type for the key");
		if(ctx.type() == null)
			throw new UnsupportedOperationException("Unsupported translation: " + ctx.getText() +" missing type for the values");
		
		return new MichelsonEmptyBigMap(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx),  visitComparable_type(ctx.comparable_type()).getStaticType(), visitType(ctx.type()).getStaticType());
		
	}

	public Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitMap_instr(Map_instrContext ctx) {
		

		NodeList<CFG, Statement, Edge> nL = new NodeList<>(SEQUENTIAL_SINGLETON);
		
		int stackBeforeMap = currentStack;
		MichelsonStack<VariableRef> copy = MichelsonStack.copy(symbolicStacks.get(currentStack));
		symbolicStacks.add(copy);
		currentStack++;
		
		
		VariableRef structure = symbolicStacks.get(currentStack).pop();

		GetNextElementFromIterableStructure elem = new GetNextElementFromIterableStructure(cfg, MichelsonFileUtils.locationOf(filepath, ctx), structure);
		Assignment firstElement = buildAssignment(elem);

		nL.addNode(firstElement);
		
		Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> block = visitBlock(ctx.block());
		
		int stackComputationAtEndIter = currentStack;
		Assignment iterGuard = firstElement;
		if(block != null)
			nL.mergeWith(block.getMiddle());
		
		Pair<MichelsonStack<VariableRef>,List<Assignment>> jp = junctionPointForStacks(ctx, stackBeforeMap, stackComputationAtEndIter);
			
		symbolicStacks.add(jp.getLeft());
		currentStack++;	
		
		List<Assignment> phiList = jp.getRight();
		
		Statement junctionNode = null;
		Statement endNode = null;

		if(!phiList.isEmpty()) {
				
			phiList.forEach(n -> nL.addNode(n));

			for(int i=0; i < phiList.size()-1; i++)
				addEdge(nL, new SequentialEdge(phiList.get(i), phiList.get(i+1)));
					
			junctionNode = phiList.get(0);
			endNode = new NoOp(cfg, MichelsonFileUtils.locationOf(filepath, ctx));
			nL.addNode(endNode);
			nL.addEdge(new SequentialEdge(phiList.get(phiList.size()-1), endNode));
		} else {
			endNode = new NoOp(cfg, MichelsonFileUtils.locationOf(filepath, ctx));
			junctionNode = endNode;
			nL.addNode(endNode);
			nL.addNode(firstElement);
			addEdge(nL, new SequentialEdge(firstElement, endNode));
		}
			
		if(block != null) {
			nL.addNode(block.getLeft());
			addEdge(nL, new SequentialEdge(iterGuard, block.getLeft()));
			addEdge(nL, new SequentialEdge(block.getRight(), iterGuard));
		}else
			addEdge(nL, new SequentialEdge(iterGuard, iterGuard));
		
		addEdge(nL, new SequentialEdge(iterGuard, junctionNode));
		
		cfs.add(new Loop(nL, iterGuard, endNode, block != null ? block.getMiddle().getNodes() : Collections.emptyList()));

		return Triple.of(iterGuard, nL, endNode);
	}

	public Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitIter_instr(Iter_instrContext ctx) {
		

		NodeList<CFG, Statement, Edge> nL = new NodeList<>(SEQUENTIAL_SINGLETON);
		
		VariableRef structure = symbolicStacks.get(currentStack).pop();
		
		int stackBeforeIter = currentStack;
		
		MichelsonStack<VariableRef> copy = MichelsonStack.copy(symbolicStacks.get(currentStack));
		symbolicStacks.add(copy);
		currentStack++;
		
		GetNextElementFromIterableStructure elem = new GetNextElementFromIterableStructure(cfg, MichelsonFileUtils.locationOf(filepath, ctx), structure);
		Assignment firstElement = buildAssignment(elem);

		nL.addNode(firstElement);
		
		Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> block = visitBlock(ctx.block());
		
		int stackComputationAtEndIter = currentStack;
		Assignment iterGuard = firstElement;
		if(block != null)
			nL.mergeWith(block.getMiddle());
		
		Pair<MichelsonStack<VariableRef>,List<Assignment>> jp = junctionPointForStacks(ctx, stackBeforeIter, stackComputationAtEndIter);
			
		symbolicStacks.add(jp.getLeft());
		currentStack++;	
		
		List<Assignment> phiList = jp.getRight();
		
		Statement junctionNode = null;
		Statement endNode = null;

		if(!phiList.isEmpty()) {
				
			phiList.forEach(n -> nL.addNode(n));

			for(int i=0; i < phiList.size()-1; i++)
				addEdge(nL, new SequentialEdge(phiList.get(i), phiList.get(i+1)));
					
			junctionNode = phiList.get(0);
			endNode = new NoOp(cfg, MichelsonFileUtils.locationOf(filepath, ctx));
			nL.addNode(endNode);
			nL.addEdge(new SequentialEdge(phiList.get(phiList.size()-1), endNode));
		} else {
			endNode = new NoOp(cfg, MichelsonFileUtils.locationOf(filepath, ctx));
			junctionNode = endNode;
			nL.addNode(endNode);
			nL.addNode(firstElement);
			addEdge(nL, new SequentialEdge(firstElement, endNode));
		}
			
		if(block != null) {
			nL.addNode(block.getLeft());
			addEdge(nL, new SequentialEdge(iterGuard, block.getLeft()));
			addEdge(nL, new SequentialEdge(block.getRight(), iterGuard));
		}else
			addEdge(nL, new SequentialEdge(iterGuard, iterGuard));
		
		addEdge(nL, new SequentialEdge(iterGuard, junctionNode));
		
		cfs.add(new Loop(nL, iterGuard, endNode, block != null ? block.getMiddle().getNodes() : Collections.emptyList()));

		return Triple.of(iterGuard, nL, endNode);

	}

	public Expression visitGet_instr(Get_instrContext ctx) {
		if (ctx.NAL_CONST() == null) {
			// consume a key and a map
			List<VariableRef> values = symbolicStacks.get(currentStack).pop(2);
			return new MichelsonGet(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx), values.get(0), values.get(1));
		} else { 
			VariableRef elem = symbolicStacks.get(currentStack).pop();
			return new MichelsonGet(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx),
					(MichelsonNaturalData) visitNatConst(ctx.NAL_CONST()), elem);
		}
	}

	public Expression visitUpdate_instr(
			Update_instrContext ctx) {
		
		
		if (ctx.NAL_CONST() == null) {
			//update map and list
			List<VariableRef> values = symbolicStacks.get(currentStack).pop(3);
			return new MichelsonUpdate(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx), values.get(0), values.get(1), values.get(2));
		}else {
			
			// update Right
			List<VariableRef> values = symbolicStacks.get(currentStack).pop(2);
			return new MichelsonUpdate(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx),
					(MichelsonNaturalData) visitNatConst(ctx.NAL_CONST()), values.get(0), values.get(1));
		}

	}

	public Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitIf_instr(If_instrContext ctx) {

		NodeList<CFG, Statement, Edge> nL = new NodeList<>(SEQUENTIAL_SINGLETON);

		VariableRef elem = symbolicStacks.get(currentStack).pop();
		
		Statement booleanGuard = mapAssignamentNode.get(elem);
		nL.addNode(booleanGuard);
		
		int stackBeforeBranches = currentStack;
		
		MichelsonStack<VariableRef> copy = MichelsonStack.copy(symbolicStacks.get(currentStack));
		symbolicStacks.add(copy);
		currentStack++;

		Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> then = visitBlock(ctx.block(0));
		
		if(then != null) {
			nL.mergeWith(then.getMiddle());
			addEdge(nL, new TrueEdge(booleanGuard, then.getLeft()));
		}
		

		int stackAfterTrueBranch = currentStack;
		
		int stackAfterFalseBranch = stackBeforeBranches;
		
		Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> otherwise = null;
		if (ctx.block(1) != null && ctx.block(1) != null) {
			MichelsonStack<VariableRef> copy2 = MichelsonStack.copy(symbolicStacks.get(stackAfterFalseBranch));
			symbolicStacks.add(copy2);
			currentStack++;
			
			otherwise = visitBlock(ctx.block(1));
			stackAfterFalseBranch = currentStack;
			
			if(otherwise != null) {
				nL.mergeWith(otherwise.getMiddle());
				addEdge(nL, new FalseEdge(booleanGuard, otherwise.getLeft()));
			}
		}
			
		Pair<MichelsonStack<VariableRef>,List<Assignment>> jp = junctionPointForStacks(ctx, stackAfterTrueBranch, stackAfterFalseBranch);
		
		symbolicStacks.add(jp.getLeft());
		currentStack++;	
		
		Pair<Statement, Statement> junctionAndEndNodes = computeJunctionAndEndNode(jp.getRight(), nL, MichelsonFileUtils.locationOf(filepath, ctx));
		
		if(then != null) {
			addEdge(nL, new SequentialEdge(then.getRight(), junctionAndEndNodes.getLeft()));
		} else
			addEdge(nL, new TrueEdge(booleanGuard, junctionAndEndNodes.getLeft()));
		
		if (otherwise != null)
			addEdge(nL, new SequentialEdge(otherwise.getRight(), junctionAndEndNodes.getLeft()));
		else
			addEdge(nL, new FalseEdge(booleanGuard, junctionAndEndNodes.getLeft()));

		cfs.add(new IfThenElse(nL, booleanGuard, junctionAndEndNodes.getRight(), then != null ? then.getMiddle().getNodes() : Collections.emptyList(),
				otherwise == null ? Collections.emptyList() : otherwise.getMiddle().getNodes()));

		return Triple.of(booleanGuard, nL, junctionAndEndNodes.getRight());
	}
	
	private Pair<MichelsonStack<VariableRef>,List<Assignment>> junctionPointForStacks(ParserRuleContext ctx, int pos1, int pos2) {
		
		//implement Delphine suggestions, to merge and "Empty stack"
		
		
		MichelsonStack<VariableRef> stack1 = symbolicStacks.get(pos1);
		MichelsonStack<VariableRef> stack2 = symbolicStacks.get(pos2);
		
		List<Assignment> phiList = new ArrayList<>();
		MichelsonStack<VariableRef> stackJP = new MichelsonStack<VariableRef>();
		
		if(containExplicitlyAbort(stack1) && containExplicitlyAbort(stack2)) {
			stackJP.add(new VariableRef(cfg, MichelsonFileUtils.locationOf(filepath, ctx), EXPLICIT_ABORT));
			return Pair.of(stackJP, phiList); 
		}
			
		if(containExplicitlyAbort(stack1) && !containExplicitlyAbort(stack2)) {
			stackJP = MichelsonStack.copy(stack2);
		} else if(containExplicitlyAbort(stack2) && !containExplicitlyAbort(stack1)) {
			stackJP = MichelsonStack.copy(stack1);
		} else {
			
			checkStackSize(pos1, pos2);
			
			for(int i = 0; i < stack1.size(); i++) {
				if(stack1.get(i).equals(stack2.get(i)))
						stackJP.push(stack1.get(i));
				else {
					Phi phi = new Phi(cfg, MichelsonFileUtils.locationOf(filepath, ctx), stack1.get(i), stack2.get(i));
					Assignment assign = buildAssignment(phi);
					phiList.add(assign);
					stackJP.push((VariableRef) assign.getLeft());
				}
			}
			
			stackJP.setTopSlotProtection(stack1.getTopProtectedSlots());
		
		}
		
		return Pair.of(stackJP, phiList);
	}

	private boolean containExplicitlyAbort(MichelsonStack<VariableRef> stack) {
		return stack.stream().anyMatch(r -> r.getName().equals(EXPLICIT_ABORT));
	}

	private void checkStackSize(int pos1, int pos2) {
		MichelsonStack<VariableRef> stack1 = symbolicStacks.get(pos1);
		MichelsonStack<VariableRef> stack2 = symbolicStacks.get(pos2);
		if(stack1.size() != stack2.size())
			throw new IllegalStateException("Different stack heigh after branch computation");
		if(stack1.getTopProtectedSlots() != stack2.getTopProtectedSlots())
			throw new IllegalStateException("Different top protected stack slots after branch computation");
		
	}

	public Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitLoop_instr(Loop_instrContext ctx) {
		NodeList<CFG, Statement, Edge> nL = new NodeList<>(SEQUENTIAL_SINGLETON);

		
		VariableRef firstElement = symbolicStacks.get(currentStack).pop();
		
		
		int stackBeforeBranch = currentStack;
		
		MichelsonStack<VariableRef> copy = MichelsonStack.copy(symbolicStacks.get(currentStack));
		symbolicStacks.add(copy);
		currentStack++;
		
		Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> block = visitBlock(ctx.block());
		
		int stackComputationAtEndBranch = currentStack;
		Assignment booleanGuard = mapAssignamentNode.get(firstElement);
		if(block != null)
			nL.mergeWith(block.getMiddle());
	
		VariableRef secondElement = symbolicStacks.get(stackComputationAtEndBranch).pop();
		
		Phi phi = new Phi(cfg, MichelsonFileUtils.locationOf(filepath, ctx), firstElement, secondElement);
		Assignment assignBooleanGuard = buildAssignment(phi, phi.getStaticType(), false);
		nL.addNode(assignBooleanGuard);
		booleanGuard = assignBooleanGuard;
		
		Pair<MichelsonStack<VariableRef>,List<Assignment>> jp = junctionPointForStacks(ctx, stackBeforeBranch, stackComputationAtEndBranch);
			
		symbolicStacks.add(jp.getLeft());
		currentStack++;	
		
		List<Assignment> phiList = jp.getRight();
		
		Statement junctionNode = null;
		Statement endNode = null;

		if(!phiList.isEmpty()) {
				
			phiList.forEach(n -> nL.addNode(n));

			for(int i=0; i < phiList.size()-1; i++)
				addEdge(nL, new SequentialEdge(phiList.get(i), phiList.get(i+1)));
					
			junctionNode = phiList.get(0);
			endNode = new NoOp(cfg, MichelsonFileUtils.locationOf(filepath, ctx));
			nL.addNode(endNode);
			nL.addEdge(new SequentialEdge(phiList.get(phiList.size()-1), endNode));
		} else {
			endNode = new NoOp(cfg, MichelsonFileUtils.locationOf(filepath, ctx));
			junctionNode = endNode;
			nL.addNode(endNode);
			nL.addNode(mapAssignamentNode.get(firstElement));
			addEdge(nL, new SequentialEdge(mapAssignamentNode.get(firstElement), endNode));
		}
			
		if(block != null) {
			nL.addNode(block.getLeft());
			addEdge(nL, new SequentialEdge(booleanGuard, block.getLeft()));
			addEdge(nL, new SequentialEdge(block.getRight(), booleanGuard));
		}else
			addEdge(nL, new SequentialEdge(booleanGuard, booleanGuard));
		
		addEdge(nL, new SequentialEdge(booleanGuard, junctionNode));
		
		cfs.add(new Loop(nL, booleanGuard, endNode, block != null ? block.getMiddle().getNodes() : Collections.emptyList()));

		return Triple.of(booleanGuard, nL, endNode);

		
	}

	public Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitLoop_left_instr(
			Loop_left_instrContext ctx) {
		NodeList<CFG, Statement, Edge> nL = new NodeList<>(SEQUENTIAL_SINGLETON);

		VariableRef firstElement = symbolicStacks.get(currentStack).pop();
		nL.addNode(mapAssignamentNode.get(firstElement));
		
		VariableRef leftGuardRef = buildVariableRef(firstElement.getLocation(), firstElement.getStaticType());

		symbolicStacks.get(currentStack).pop();
		Assignment valueFromLeft = buildAssignment(new ConsumeLEFT_RIGHT_ProduceOr(cfg, MichelsonFileUtils.locationOf(filepath, ctx), leftGuardRef));
		
		nL.addNode(valueFromLeft);
		
		int stackBeforeBranch = currentStack;
		
		MichelsonStack<VariableRef> copy = MichelsonStack.copy(symbolicStacks.get(currentStack));
		symbolicStacks.add(copy);
		currentStack++;
		
		Assignment getLeft = buildAssignment(new GetLeft(cfg, MichelsonFileUtils.locationOf(filepath, ctx), symbolicStacks.get(currentStack).pop()));
		
		Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> block = visitBlock(ctx.block());
		
		nL.addNode(getLeft);
		
		if(block != null) {
			nL.mergeWith(block.getMiddle());
			addEdge(nL, new SequentialEdge(getLeft, block.getLeft()));
		}
		
		int stackComputationAtEndBranch = currentStack;

		Assignment leftGuard = null;

		VariableRef secondElement = symbolicStacks.get(stackComputationAtEndBranch).firstElement();
		
		Phi phi = new Phi(cfg, MichelsonFileUtils.locationOf(filepath, ctx), (VariableRef) firstElement, secondElement);
		
		Assignment assignleftGuard = new Assignment(cfg, leftGuardRef.getLocation(), phi.getStaticType(), leftGuardRef, phi);
		
		nL.addNode(assignleftGuard);
		
		leftGuard = assignleftGuard;
		
		Assignment getRight = buildAssignment(new GetRight(cfg, MichelsonFileUtils.locationOf(filepath, ctx), symbolicStacks.get(currentStack).pop()));
		nL.addNode(getRight);;
		
		checkStackSize(stackBeforeBranch, stackComputationAtEndBranch);
		
		symbolicStacks.add(MichelsonStack.copy(symbolicStacks.get(stackBeforeBranch)));
		currentStack++;	
			
		symbolicStacks.get(currentStack).push(leftGuardRef);
			
		addEdge(nL, new SequentialEdge(mapAssignamentNode.get(firstElement), leftGuard));
		addEdge(nL, new SequentialEdge(leftGuard, valueFromLeft));
			
		
		if(block != null) {
			addEdge(nL, new SequentialEdge(block.getRight(), leftGuard));
		} else {
			addEdge(nL, new SequentialEdge(valueFromLeft, getLeft));
			addEdge(nL, new SequentialEdge(getLeft, leftGuard));
		}
		
		addEdge(nL, new SequentialEdge(leftGuard, getRight));
			
		Collection<Statement> nodes = new TreeSet<>();
		nodes.add(getLeft);
		nodes.add(getRight);
		
		if(block != null)
			nodes.addAll(block.getMiddle().getNodes());
		
		cfs.add(new Loop(nL, leftGuard, valueFromLeft,	nodes));

		return Triple.of(leftGuard, nL, valueFromLeft);
	}
	
	private int countLambda = 0;

	public Expression visitLambda_instr(
			Lambda_instrContext ctx) {
		

		Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> block = null;
		
		MichelsonLambdaVisitor visitor = new MichelsonLambdaVisitor(filepath, descriptor, countLambda);
		block = visitor.visitLambdaMember(ctx);
		
		MichelsonLambda lambda = new MichelsonLambda(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx), visitor.getParamType(), visitor.getRetType());
		
		
		/*
		List<StackModifier, Integer, Type> stackTransformations = visitor.getStackTransformations();
		mapLambda.put(ref, Triple.of(lambda, block, stackTransformations);		
		*/
		
		return lambda;
	}

	public Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> visitDip_instr(Dip_instrContext ctx) {

		int n = 1;
		MichelsonDip dip;
		if(ctx.NAL_CONST() != null) {
			MichelsonNaturalData nat = ((MichelsonNaturalData) visitNatConst(ctx.NAL_CONST()));
			n = nat.getValue().intValueExact();
			dip = new MichelsonDip(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getLine(ctx), nat);
		}else {
			dip = new MichelsonDip(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getLine(ctx));
		}

		NodeList<CFG, Statement, Edge> nL = new NodeList<>(SEQUENTIAL_SINGLETON);
		nL.addNode(dip);

		symbolicStacks.get(currentStack).setTopSlotProtection((symbolicStacks.get(currentStack).getTopProtectedSlots()+n));
		
		Triple<Statement, NodeList<CFG, Statement, Edge>, Statement> block = visitBlock(ctx.block());

		symbolicStacks.get(currentStack).setTopSlotProtection((symbolicStacks.get(currentStack).getTopProtectedSlots()-n));

		if(block == null) {
			NoOp instrumented  = new NoOp(cfg, MichelsonFileUtils.locationOf(filepath, ctx));
			NodeList<CFG, Statement, Edge> nLBlock = new NodeList<>(SEQUENTIAL_SINGLETON);
			nLBlock.addNode(instrumented);
			block = Triple.of(instrumented, nLBlock, instrumented);
		}
		
		nL.mergeWith(block.getMiddle());
		
		addEdge(nL, new SequentialEdge(dip, block.getLeft()));
		
		return Triple.of(dip, nL, block.getRight());

	}

	public Expression visitUnpack_instr(
			Unpack_instrContext ctx) {
		VariableRef element = symbolicStacks.get(currentStack).pop();
		if(ctx.type()== null)
			throw new UnsupportedOperationException("Unsupported translation: " + ctx.getText() +" missing type to unpack");
		return new MichelsonUnpack(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx),  visitType(ctx.type()), element);
	}

	public Expression visitContract_instr(
			Contract_instrContext ctx) {
		
		VariableRef address = symbolicStacks.get(currentStack).pop();
		MichelsonTypeExpression type = visitType(ctx.type());
		
		return new MichelsonContract(cfg, MichelsonFileUtils.locationOf(filepath, ctx), type.getValue(), address);
	}

	private int counterCreateContract = 0;
	public Expression visitCreate_contract_instr(
			Create_contract_instrContext ctx) {

		List<VariableRef> values = symbolicStacks.get(currentStack).pop(3);
		MichelsonCreateContract create = new MichelsonCreateContract(cfg, MichelsonFileUtils.locationOf(filepath, ctx), values.get(0), values.get(1), values.get(2));
		
		ContractContext contract = ctx.contract(); 		
		ParameterContext parameter = contract.parameter();
		StorageContext storage = contract.storage();
		CodeContext code = contract.code();
		MichelsonCodeMemberVisitor contractVisitor = new MichelsonCodeMemberVisitor(filepath, new CodeMemberDescriptor(MichelsonFileUtils.locationOf(filepath, contract), descriptor.getUnit(), false, "CREATE_CONTRACT_"+counterCreateContract));
		CFG contractCfg = contractVisitor.visitCodeMember(parameter, storage, code);	
		mapCreateContract.put(create, contractCfg);
		
		return create;
	}

	public Expression visitSapling_empty_state(
			Sapling_empty_stateContext ctx) {
		if(ctx.NAL_CONST() != null)
			return new MichelsonSaplingEmptyState(cfg, filepath, MichelsonFileUtils.getLine(ctx), MichelsonFileUtils.getCol(ctx),
						(MichelsonNaturalData) visitNatConst(ctx.NAL_CONST()));
		throw new UnsupportedOperationException("Unsupported translation: " + ctx.getText() +" missing natural value");
	}

	protected void addEdge(NodeList<CFG, Statement, Edge> nodeList, Edge edge) {
		if (!(edge.getSource() instanceof Ret) && !(edge.getSource() instanceof MichelsonFailwith) )
			nodeList.addEdge(edge);
	}
}
