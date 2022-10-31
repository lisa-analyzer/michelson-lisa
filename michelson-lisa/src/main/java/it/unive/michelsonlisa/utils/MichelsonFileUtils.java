package it.unive.michelsonlisa.utils;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import it.unive.lisa.program.SourceCodeLocation;

public class MichelsonFileUtils {
	
	public static SourceCodeLocation locationOf(String filepath, ParserRuleContext ctx) {
		return new SourceCodeLocation(filepath, getLine(ctx), getCol(ctx));
	}

	public static int getLine(ParserRuleContext ctx) {
		return ctx.getStart().getLine();
	}

	public static int getLine(TerminalNode ctx) {
		return ctx.getSymbol().getLine();
	}

	public static int getCol(ParserRuleContext ctx) {
		return ctx.getStop().getCharPositionInLine();
	}

	public static int getCol(TerminalNode ctx) {
		return ctx.getSymbol().getCharPositionInLine();
	}

	public static SourceCodeLocation locationOf(String filepath, TerminalNode ctx) {
		return new SourceCodeLocation(filepath, getLine(ctx), getCol(ctx));
	}
}
