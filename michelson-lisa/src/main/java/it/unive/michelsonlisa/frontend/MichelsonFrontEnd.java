package it.unive.michelsonlisa.frontend;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.unive.lisa.program.ClassUnit;
import it.unive.lisa.program.Program;
import it.unive.lisa.program.ProgramUnit;
import it.unive.lisa.program.cfg.CFG;
import it.unive.lisa.program.cfg.CodeMemberDescriptor;
import it.unive.michelsonlisa.antlr.MichelsonLexer;
import it.unive.michelsonlisa.antlr.MichelsonParser;
import it.unive.michelsonlisa.antlr.MichelsonParser.CodeContext;
import it.unive.michelsonlisa.antlr.MichelsonParser.ContractContext;
import it.unive.michelsonlisa.antlr.MichelsonParser.ParameterContext;
import it.unive.michelsonlisa.antlr.MichelsonParser.StorageContext;
import it.unive.michelsonlisa.frontend.visitors.MichelsonCodeMemberVisitor;
import it.unive.michelsonlisa.utils.MichelsonFileUtils;

/**
 * @MichelsonFrontEnd manages the translation from a Michelson program to the corresponding
 *                 LiSA @CFG.
 * 
 * @author <a href="mailto:luca.olivieri@univr.it">Luca Olivieri</a>
 */
public class MichelsonFrontEnd  {

	private static final Logger log = LogManager.getLogger(MichelsonFrontEnd.class);

	/**
	 * Michelson program file path.
	 */
	private final String filePath;
	

	/**
	 * Builds an instance of @MichelsonToCFG for a given Michelson program given at the
	 * location filePath.
	 * 
	 * @param filePath file path to a Michelson program.
	 */
	private MichelsonFrontEnd(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * Returns the parsed file path.
	 * 
	 * @return the parsed file path
	 */
	public String getFilePath() {
		return filePath;
	}

	public static Program processFile(String filePath) throws IOException {
		return new MichelsonFrontEnd(filePath).toLiSAProgram();
	}

	/**
	 * Returns the collection of @CFG in a Michelson program at filePath.
	 * 
	 * @return collection of @CFG in file
	 * 
	 * @throws IOException if {@code stream} to file cannot be written to or
	 *                         closed
	 */
	private Program toLiSAProgram() throws IOException {
		log.info("Michelson front-end setup...");
		log.info("Reading file... " + filePath);

		long start = System.currentTimeMillis();

		InputStream stream = new FileInputStream(getFilePath());

		log.info("LOCS: " + Files.lines(Paths.get(getFilePath())).count());

		MichelsonLexer lexer = new MichelsonLexer(CharStreams.fromStream(stream, StandardCharsets.UTF_8));
		MichelsonParser parser = new MichelsonParser(new CommonTokenStream(lexer));
		parser.setErrorHandler(new BailErrorStrategy());
		
		long parsingTime = System.currentTimeMillis();
		
		Program program = visitMichelsonFile(parser.contract());

		log.info("PARSING TIME: " + (parsingTime - start) + " CFG time: " + (System.currentTimeMillis() - parsingTime));

		stream.close();
		
		//registerMichelsonTypes(program);

		return program;
	}

	private Program visitMichelsonFile(ContractContext file) {
		Program program  = new Program(new MichelsonLanguageFeatures(), new MichelsonTypeSystem());
		ParameterContext parameter = file.parameter();
		StorageContext storage = file.storage();
		CodeContext code = file.code();
		
		ProgramUnit unit = new ClassUnit(MichelsonFileUtils.locationOf(getFilePath(), code), program, "", false);

		program.addUnit(unit);
		
		MichelsonCodeMemberVisitor visitor = new MichelsonCodeMemberVisitor(getFilePath(), new CodeMemberDescriptor(MichelsonFileUtils.locationOf(getFilePath(), code), unit, false, "code"));
		
		CFG cfg = visitor.visitCodeMember(parameter, storage, code);
		program.addEntryPoint(cfg);
		unit.addCodeMember(cfg);
		
		visitor.printMapEntryPoints();
		
		return program;
	}
}