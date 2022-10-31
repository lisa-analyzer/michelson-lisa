import org.junit.Test;

import it.unive.lisa.AnalysisSetupException;
import it.unive.lisa.LiSAConfiguration;
import it.unive.lisa.LiSAConfiguration.GraphType;
import it.unive.lisa.analysis.SimpleAbstractState;
import it.unive.lisa.analysis.heap.MonolithicHeap;
import it.unive.lisa.analysis.nonrelational.value.TypeEnvironment;
import it.unive.lisa.analysis.nonrelational.value.ValueEnvironment;
import it.unive.lisa.analysis.types.InferredTypes;
import it.unive.michelsonlisa.analysis.numerical.sign.Sign;

public class SmartContractsTest extends MichelsonAnalysisTestExecutor {

	
	@Test
	public void test1() throws AnalysisSetupException {
		LiSAConfiguration conf = new LiSAConfiguration().setSerializeResults(true);
		
		conf.setAbstractState(new SimpleAbstractState<>(
				new MonolithicHeap(),
				new ValueEnvironment<>(new Sign()),
				new TypeEnvironment<>(new InferredTypes())));
		perform("test1", "code1.tz", conf);
	}
	
	@Test
	public void test2() throws AnalysisSetupException {
		LiSAConfiguration conf = new LiSAConfiguration().setSerializeResults(true);
		
		conf.setAbstractState(new SimpleAbstractState<>(
				new MonolithicHeap(),
				new ValueEnvironment<>(new Sign()),
				new TypeEnvironment<>(new InferredTypes())));
		perform("test2", "code2.tz", conf);
	}
	
	@Test
	public void test3() throws AnalysisSetupException {
		LiSAConfiguration conf = new LiSAConfiguration().setSerializeResults(true);
		
		conf.setAbstractState(new SimpleAbstractState<>(
				new MonolithicHeap(),
				new ValueEnvironment<>(new Sign()),
				new TypeEnvironment<>(new InferredTypes())));
		perform("test3", "code3.tz", conf);
	}
	
	@Test
	public void test4() throws AnalysisSetupException {
		LiSAConfiguration conf = new LiSAConfiguration().setSerializeResults(true);
		
		conf.setAbstractState(new SimpleAbstractState<>(
				new MonolithicHeap(),
				new ValueEnvironment<>(new Sign()),
				new TypeEnvironment<>(new InferredTypes())));
		perform("test4", "code4.tz", conf);
	}
		
	@Test
	public void test5() throws AnalysisSetupException {
		LiSAConfiguration conf = new LiSAConfiguration().setSerializeResults(true);
		
		conf.setAbstractState(new SimpleAbstractState<>(
				new MonolithicHeap(),
				new ValueEnvironment<>(new Sign()),
				new TypeEnvironment<>(new InferredTypes())));
		perform("test5", "code5.tz", conf);
	}

	@Test
	public void test6() throws AnalysisSetupException {
		LiSAConfiguration conf = new LiSAConfiguration().setSerializeResults(true);
		
		conf.setAbstractState(new SimpleAbstractState<>(
				new MonolithicHeap(),
				new ValueEnvironment<>(new Sign()),
				new TypeEnvironment<>(new InferredTypes())));
		perform("test6", "code6.tz", conf);
	}

	@Test
	public void test7() throws AnalysisSetupException {
		LiSAConfiguration conf = new LiSAConfiguration().setSerializeResults(true);
		
		conf.setAbstractState(new SimpleAbstractState<>(
				new MonolithicHeap(),
				new ValueEnvironment<>(new Sign()),
				new TypeEnvironment<>(new InferredTypes())));
		perform("test7", "code7.tz", conf);
	}


	@Test
	public void test8() throws AnalysisSetupException {
		LiSAConfiguration conf = new LiSAConfiguration().setSerializeResults(true);
		
		conf.setAbstractState(new SimpleAbstractState<>(
				new MonolithicHeap(),
				new ValueEnvironment<>(new Sign()),
				new TypeEnvironment<>(new InferredTypes())));
		perform("test8", "code8.tz", conf);
	}


	@Test
	public void test9() throws AnalysisSetupException {
		LiSAConfiguration conf = new LiSAConfiguration().setSerializeResults(true);
		
		conf.setAbstractState(new SimpleAbstractState<>(
				new MonolithicHeap(),
				new ValueEnvironment<>(new Sign()),
				new TypeEnvironment<>(new InferredTypes())));
		perform("test9", "code9.tz", conf);
	}


	@Test
	public void test10() throws AnalysisSetupException {
		LiSAConfiguration conf = new LiSAConfiguration().setSerializeResults(true);
		
		conf.setAbstractState(new SimpleAbstractState<>(
				new MonolithicHeap(),
				new ValueEnvironment<>(new Sign()),
				new TypeEnvironment<>(new InferredTypes())));
		perform("test10", "code10.tz", conf);
	}


	@Test
	public void test11() throws AnalysisSetupException {
		LiSAConfiguration conf = new LiSAConfiguration().setSerializeResults(true);
		
		conf.setAbstractState(new SimpleAbstractState<>(
				new MonolithicHeap(),
				new ValueEnvironment<>(new Sign()),
				new TypeEnvironment<>(new InferredTypes())));
		perform("test11", "code11.tz", conf);
	}


	@Test
	public void test12() throws AnalysisSetupException {
		LiSAConfiguration conf = new LiSAConfiguration().setSerializeResults(true);
		
		conf.setAbstractState(new SimpleAbstractState<>(
				new MonolithicHeap(),
				new ValueEnvironment<>(new Sign()),
				new TypeEnvironment<>(new InferredTypes())));
		perform("test12", "code12.tz", conf);
	}


	@Test
	public void test13() throws AnalysisSetupException {
		LiSAConfiguration conf = new LiSAConfiguration().setSerializeResults(true);
		
		conf.setAbstractState(new SimpleAbstractState<>(
				new MonolithicHeap(),
				new ValueEnvironment<>(new Sign()),
				new TypeEnvironment<>(new InferredTypes())));
		perform("test13", "code13.tz", conf);
	}

	@Test
	public void test14() throws AnalysisSetupException {
		LiSAConfiguration conf = new LiSAConfiguration().setSerializeResults(true);
		
		conf.setAbstractState(new SimpleAbstractState<>(
				new MonolithicHeap(),
				new ValueEnvironment<>(new Sign()),
				new TypeEnvironment<>(new InferredTypes())));
		perform("test14", "code14.tz", conf);
	}

	@Test
	public void test15() throws AnalysisSetupException {
		LiSAConfiguration conf = new LiSAConfiguration().setSerializeResults(true);
		
		conf.setAbstractState(new SimpleAbstractState<>(
				new MonolithicHeap(),
				new ValueEnvironment<>(new Sign()),
				new TypeEnvironment<>(new InferredTypes())));
		perform("test15", "code15.tz", conf);
	}

	@Test
	public void test16() throws AnalysisSetupException {
		LiSAConfiguration conf = new LiSAConfiguration().setSerializeResults(true);
		
		conf.setAbstractState(new SimpleAbstractState<>(
				new MonolithicHeap(),
				new ValueEnvironment<>(new Sign()),
				new TypeEnvironment<>(new InferredTypes())));
		perform("test16", "code16.tz", conf);
	}

	@Test
	public void test17() throws AnalysisSetupException {
		LiSAConfiguration conf = new LiSAConfiguration().setSerializeResults(true);
		
		conf.setAbstractState(new SimpleAbstractState<>(
				new MonolithicHeap(),
				new ValueEnvironment<>(new Sign()),
				new TypeEnvironment<>(new InferredTypes())));
		perform("test17", "code17.tz", conf);
	}

	@Test
	public void test18() throws AnalysisSetupException {
		LiSAConfiguration conf = new LiSAConfiguration().setSerializeResults(true);
		
		conf.setAbstractState(new SimpleAbstractState<>(
				new MonolithicHeap(),
				new ValueEnvironment<>(new Sign()),
				new TypeEnvironment<>(new InferredTypes())));
		perform("test18", "code18.tz", conf);
	}

	@Test
	public void test19() throws AnalysisSetupException {
		LiSAConfiguration conf = new LiSAConfiguration().setSerializeResults(true);
		
		conf.setAbstractState(new SimpleAbstractState<>(
				new MonolithicHeap(),
				new ValueEnvironment<>(new Sign()),
				new TypeEnvironment<>(new InferredTypes())));
		perform("test19", "code19.tz", conf);
	}

	@Test
	public void test20() throws AnalysisSetupException {
		LiSAConfiguration conf = new LiSAConfiguration().setSerializeResults(true);
		
		conf.setAbstractState(new SimpleAbstractState<>(
				new MonolithicHeap(),
				new ValueEnvironment<>(new Sign()),
				new TypeEnvironment<>(new InferredTypes())));
		perform("test20", "code20.tz", conf);
	}
}
