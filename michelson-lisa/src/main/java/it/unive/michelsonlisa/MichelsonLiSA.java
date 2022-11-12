package it.unive.michelsonlisa;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;

import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.unive.lisa.AnalysisSetupException;
import it.unive.lisa.LiSA;
import it.unive.lisa.LiSAConfiguration;
import it.unive.lisa.LiSAConfiguration.GraphType;
import it.unive.lisa.analysis.SimpleAbstractState;
import it.unive.lisa.analysis.heap.MonolithicHeap;
import it.unive.lisa.analysis.nonrelational.value.TypeEnvironment;
import it.unive.lisa.analysis.nonrelational.value.ValueEnvironment;
import it.unive.lisa.analysis.types.InferredTypes;
import it.unive.lisa.outputs.json.JsonReport;
import it.unive.lisa.program.Program;
import it.unive.lisa.util.file.FileManager;
import it.unive.michelsonlisa.analysis.flow.taint.TaintDomain;
import it.unive.michelsonlisa.analysis.numerical.sign.Sign;
import it.unive.michelsonlisa.annotations.sets.CrossContractInvokingAnnotationSet;
import it.unive.michelsonlisa.checkers.TaintChecker;
import it.unive.michelsonlisa.frontend.MichelsonFrontEnd;
import it.unive.michelsonlisa.loader.AnnotationLoader;

public class MichelsonLiSA {

	private static final Logger LOG = LogManager.getLogger(MichelsonLiSA.class);

	public static void main(String[] args) throws AnalysisSetupException {

		Options options = new Options();

		Option input = new Option("i", "input", true, "input file path");
		input.setRequired(true);
		options.addOption(input);

		Option output = new Option("o", "output", true, "output file path");
		output.setRequired(true);
		options.addOption(output);
		
		Option analysis = new Option("a", "analysis", true, "the analysis to perform (sign, ucci)");
		output.setRequired(true);
		options.addOption(analysis);
		
		Option dump_opt = new Option("d", "dump", false, "dump the analysis");
		dump_opt.setRequired(false);
		options.addOption(dump_opt);
		
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd = null;

		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			formatter.printHelp("help", options);

			System.exit(1);
		}

		String filePath = cmd.getOptionValue("input");

		String outputDir = cmd.getOptionValue("output");
		
		String analysisChoice = cmd.getOptionValue("analysis");
		
		LiSAConfiguration conf = new LiSAConfiguration();

		conf.workdir =outputDir;
		conf.jsonOutput = true;
		
		conf.analysisGraphs = cmd.hasOption(dump_opt) ? GraphType.HTML_WITH_SUBNODES : GraphType.NONE;
		
		AnnotationLoader annotationLoader = null;
		
		switch(analysisChoice) {
	
			case "sign":
				conf.abstractState = new SimpleAbstractState<>(
						new MonolithicHeap(),
						new ValueEnvironment<>(new Sign()),
						new TypeEnvironment<>(new InferredTypes()));
				break;
			
			case "ucci": 
				if(!containSinks(filePath))
					exit(conf);
				conf.abstractState = new SimpleAbstractState<>(
						new MonolithicHeap(),
						new ValueEnvironment<>(new TaintDomain()),
						new TypeEnvironment<>(new InferredTypes()));
				conf.semanticChecks.add(new TaintChecker());
				
				annotationLoader = new AnnotationLoader(new CrossContractInvokingAnnotationSet());
				break;
		}
		
		Program program = null;

		File theDir = new File(outputDir);
		if (!theDir.exists())
			theDir.mkdirs();

		try {

			program = MichelsonFrontEnd.processFile(filePath);
			if(annotationLoader != null)
				annotationLoader.load(program);
		} catch (ParseCancellationException e) {
			// a parsing error occurred
			e.printStackTrace();
			System.err.println("Parsing error.");
			return;
		} catch (IOException e) {
			// the file does not exists
			System.err.println("File " + filePath + "does not exist.");
			return;
		} catch (UnsupportedOperationException e1) {
			// an unsupported operations has been encountered
			System.err.println(e1 + " " + e1.getStackTrace()[0].toString());
			e1.printStackTrace();
			return;
		} catch (Exception e2) {
			// other exception
			e2.printStackTrace();
			System.err.println(e2 + " " + e2.getStackTrace()[0].toString());
			return;
		}

		LiSA lisa = new LiSA(conf);

		try {
			lisa.run(program);
		} catch (Exception e) {
			// an error occurred during the analysis
			e.printStackTrace();
			return;
		}
	}

	private static void exit(LiSAConfiguration conf) {
		
		if(conf.jsonOutput) {
			LOG.info("Dumping reported warnings to 'report.json'");
			FileManager fileManager = new FileManager(conf.workdir);
			
			JsonReport report = new JsonReport(new HashSet<>(), fileManager.createdFiles());
			try {
				
				fileManager.mkOutputFile("report.json", writer -> {
					report.dump(writer);
					LOG.info("Report file dumped to report.json");
				});
			} catch (IOException e) {
				LOG.error("Unable to dump report file", e);
			}
		}
		System.exit(0);
	}

	private static boolean containSinks(String filePath) {
		
		try {
			FileInputStream inputStream = new FileInputStream(filePath);
		    String rawText = IOUtils.toString(inputStream);
		    inputStream.close();
		    return rawText.toUpperCase().contains("TRANSFER_TOKENS");	
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return true;
	}

}
