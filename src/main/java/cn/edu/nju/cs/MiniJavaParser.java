// Generated from MiniJavaParser.g4 by ANTLR 4.13.2
package cn.edu.nju.cs;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class MiniJavaParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		ABSTRACT=1, ASSERT=2, BOOLEAN=3, BREAK=4, BYTE=5, CASE=6, CATCH=7, CHAR=8, 
		CLASS=9, CONST=10, CONTINUE=11, DEFAULT=12, DO=13, DOUBLE=14, ELSE=15, 
		ENUM=16, EXTENDS=17, FINAL=18, FINALLY=19, FLOAT=20, FOR=21, IF=22, GOTO=23, 
		IMPLEMENTS=24, IMPORT=25, INSTANCEOF=26, INT=27, STRING=28, INTERFACE=29, 
		LONG=30, NATIVE=31, NEW=32, PACKAGE=33, PRIVATE=34, PROTECTED=35, PUBLIC=36, 
		RETURN=37, SHORT=38, STATIC=39, STRICTFP=40, SUPER=41, SWITCH=42, SYNCHRONIZED=43, 
		THIS=44, THROW=45, THROWS=46, TRANSIENT=47, TRY=48, VOID=49, VOLATILE=50, 
		WHILE=51, MODULE=52, OPEN=53, REQUIRES=54, EXPORTS=55, OPENS=56, TO=57, 
		USES=58, PROVIDES=59, WITH=60, TRANSITIVE=61, VAR=62, YIELD=63, RECORD=64, 
		SEALED=65, PERMITS=66, NON_SEALED=67, DECIMAL_LITERAL=68, HEX_LITERAL=69, 
		OCT_LITERAL=70, BINARY_LITERAL=71, FLOAT_LITERAL=72, HEX_FLOAT_LITERAL=73, 
		BOOL_LITERAL=74, CHAR_LITERAL=75, STRING_LITERAL=76, TEXT_BLOCK=77, NULL_LITERAL=78, 
		LPAREN=79, RPAREN=80, LBRACE=81, RBRACE=82, LBRACK=83, RBRACK=84, SEMI=85, 
		COMMA=86, DOT=87, ASSIGN=88, GT=89, LT=90, BANG=91, TILDE=92, QUESTION=93, 
		COLON=94, EQUAL=95, LE=96, GE=97, NOTEQUAL=98, AND=99, OR=100, INC=101, 
		DEC=102, ADD=103, SUB=104, MUL=105, DIV=106, BITAND=107, BITOR=108, CARET=109, 
		MOD=110, LSHIFT=111, RSHIFT=112, URSHIFT=113, ADD_ASSIGN=114, SUB_ASSIGN=115, 
		MUL_ASSIGN=116, DIV_ASSIGN=117, AND_ASSIGN=118, OR_ASSIGN=119, XOR_ASSIGN=120, 
		MOD_ASSIGN=121, LSHIFT_ASSIGN=122, RSHIFT_ASSIGN=123, URSHIFT_ASSIGN=124, 
		ARROW=125, COLONCOLON=126, AT=127, ELLIPSIS=128, WS=129, COMMENT=130, 
		LINE_COMMENT=131, IDENTIFIER=132;
	public static final int
		RULE_compilationUnit = 0, RULE_block = 1, RULE_blockStatement = 2, RULE_localVariableDeclaration = 3, 
		RULE_statement = 4, RULE_parExpression = 5, RULE_forControl = 6, RULE_forInit = 7, 
		RULE_expressionList = 8, RULE_expression = 9, RULE_primary = 10, RULE_literal = 11, 
		RULE_identifier = 12, RULE_primitiveType = 13;
	private static String[] makeRuleNames() {
		return new String[] {
			"compilationUnit", "block", "blockStatement", "localVariableDeclaration", 
			"statement", "parExpression", "forControl", "forInit", "expressionList", 
			"expression", "primary", "literal", "identifier", "primitiveType"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'abstract'", "'assert'", "'boolean'", "'break'", "'byte'", "'case'", 
			"'catch'", "'char'", "'class'", "'const'", "'continue'", "'default'", 
			"'do'", "'double'", "'else'", "'enum'", "'extends'", "'final'", "'finally'", 
			"'float'", "'for'", "'if'", "'goto'", "'implements'", "'import'", "'instanceof'", 
			"'int'", "'string'", "'interface'", "'long'", "'native'", "'new'", "'package'", 
			"'private'", "'protected'", "'public'", "'return'", "'short'", "'static'", 
			"'strictfp'", "'super'", "'switch'", "'synchronized'", "'this'", "'throw'", 
			"'throws'", "'transient'", "'try'", "'void'", "'volatile'", "'while'", 
			"'module'", "'open'", "'requires'", "'exports'", "'opens'", "'to'", "'uses'", 
			"'provides'", "'with'", "'transitive'", "'var'", "'yield'", "'record'", 
			"'sealed'", "'permits'", "'non-sealed'", null, null, null, null, null, 
			null, null, null, null, null, "'null'", "'('", "')'", "'{'", "'}'", "'['", 
			"']'", "';'", "','", "'.'", "'='", "'>'", "'<'", "'not'", "'~'", "'?'", 
			"':'", "'=='", "'<='", "'>='", "'!='", "'and'", "'or'", "'++'", "'--'", 
			"'+'", "'-'", "'*'", "'/'", "'&'", "'|'", "'^'", "'%'", "'<<'", "'>>'", 
			"'>>>'", "'+='", "'-='", "'*='", "'/='", "'&='", "'|='", "'^='", "'%='", 
			"'<<='", "'>>='", "'>>>='", "'->'", "'::'", "'@'", "'...'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "ABSTRACT", "ASSERT", "BOOLEAN", "BREAK", "BYTE", "CASE", "CATCH", 
			"CHAR", "CLASS", "CONST", "CONTINUE", "DEFAULT", "DO", "DOUBLE", "ELSE", 
			"ENUM", "EXTENDS", "FINAL", "FINALLY", "FLOAT", "FOR", "IF", "GOTO", 
			"IMPLEMENTS", "IMPORT", "INSTANCEOF", "INT", "STRING", "INTERFACE", "LONG", 
			"NATIVE", "NEW", "PACKAGE", "PRIVATE", "PROTECTED", "PUBLIC", "RETURN", 
			"SHORT", "STATIC", "STRICTFP", "SUPER", "SWITCH", "SYNCHRONIZED", "THIS", 
			"THROW", "THROWS", "TRANSIENT", "TRY", "VOID", "VOLATILE", "WHILE", "MODULE", 
			"OPEN", "REQUIRES", "EXPORTS", "OPENS", "TO", "USES", "PROVIDES", "WITH", 
			"TRANSITIVE", "VAR", "YIELD", "RECORD", "SEALED", "PERMITS", "NON_SEALED", 
			"DECIMAL_LITERAL", "HEX_LITERAL", "OCT_LITERAL", "BINARY_LITERAL", "FLOAT_LITERAL", 
			"HEX_FLOAT_LITERAL", "BOOL_LITERAL", "CHAR_LITERAL", "STRING_LITERAL", 
			"TEXT_BLOCK", "NULL_LITERAL", "LPAREN", "RPAREN", "LBRACE", "RBRACE", 
			"LBRACK", "RBRACK", "SEMI", "COMMA", "DOT", "ASSIGN", "GT", "LT", "BANG", 
			"TILDE", "QUESTION", "COLON", "EQUAL", "LE", "GE", "NOTEQUAL", "AND", 
			"OR", "INC", "DEC", "ADD", "SUB", "MUL", "DIV", "BITAND", "BITOR", "CARET", 
			"MOD", "LSHIFT", "RSHIFT", "URSHIFT", "ADD_ASSIGN", "SUB_ASSIGN", "MUL_ASSIGN", 
			"DIV_ASSIGN", "AND_ASSIGN", "OR_ASSIGN", "XOR_ASSIGN", "MOD_ASSIGN", 
			"LSHIFT_ASSIGN", "RSHIFT_ASSIGN", "URSHIFT_ASSIGN", "ARROW", "COLONCOLON", 
			"AT", "ELLIPSIS", "WS", "COMMENT", "LINE_COMMENT", "IDENTIFIER"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "MiniJavaParser.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MiniJavaParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class CompilationUnitContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public TerminalNode EOF() { return getToken(MiniJavaParser.EOF, 0); }
		public CompilationUnitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_compilationUnit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniJavaParserListener ) ((MiniJavaParserListener)listener).enterCompilationUnit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniJavaParserListener ) ((MiniJavaParserListener)listener).exitCompilationUnit(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiniJavaParserVisitor ) return ((MiniJavaParserVisitor<? extends T>)visitor).visitCompilationUnit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CompilationUnitContext compilationUnit() throws RecognitionException {
		CompilationUnitContext _localctx = new CompilationUnitContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_compilationUnit);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(28);
			block();
			setState(29);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BlockContext extends ParserRuleContext {
		public TerminalNode LBRACE() { return getToken(MiniJavaParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(MiniJavaParser.RBRACE, 0); }
		public List<BlockStatementContext> blockStatement() {
			return getRuleContexts(BlockStatementContext.class);
		}
		public BlockStatementContext blockStatement(int i) {
			return getRuleContext(BlockStatementContext.class,i);
		}
		public BlockContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_block; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniJavaParserListener ) ((MiniJavaParserListener)listener).enterBlock(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniJavaParserListener ) ((MiniJavaParserListener)listener).exitBlock(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiniJavaParserVisitor ) return ((MiniJavaParserVisitor<? extends T>)visitor).visitBlock(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockContext block() throws RecognitionException {
		BlockContext _localctx = new BlockContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_block);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(31);
			match(LBRACE);
			setState(35);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & -2251799404738276L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 2061989239831L) != 0) || _la==IDENTIFIER) {
				{
				{
				setState(32);
				blockStatement();
				}
				}
				setState(37);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(38);
			match(RBRACE);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class BlockStatementContext extends ParserRuleContext {
		public LocalVariableDeclarationContext localVariableDeclaration() {
			return getRuleContext(LocalVariableDeclarationContext.class,0);
		}
		public TerminalNode SEMI() { return getToken(MiniJavaParser.SEMI, 0); }
		public StatementContext statement() {
			return getRuleContext(StatementContext.class,0);
		}
		public BlockStatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_blockStatement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniJavaParserListener ) ((MiniJavaParserListener)listener).enterBlockStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniJavaParserListener ) ((MiniJavaParserListener)listener).exitBlockStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiniJavaParserVisitor ) return ((MiniJavaParserVisitor<? extends T>)visitor).visitBlockStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BlockStatementContext blockStatement() throws RecognitionException {
		BlockStatementContext _localctx = new BlockStatementContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_blockStatement);
		try {
			setState(44);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case BOOLEAN:
			case CHAR:
			case INT:
			case STRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(40);
				localVariableDeclaration();
				setState(41);
				match(SEMI);
				}
				break;
			case ASSERT:
			case BREAK:
			case CONTINUE:
			case FOR:
			case IF:
			case WHILE:
			case MODULE:
			case OPEN:
			case REQUIRES:
			case EXPORTS:
			case OPENS:
			case TO:
			case USES:
			case PROVIDES:
			case WITH:
			case TRANSITIVE:
			case VAR:
			case YIELD:
			case RECORD:
			case SEALED:
			case PERMITS:
			case DECIMAL_LITERAL:
			case BOOL_LITERAL:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NULL_LITERAL:
			case LPAREN:
			case LBRACE:
			case SEMI:
			case BANG:
			case TILDE:
			case INC:
			case DEC:
			case ADD:
			case SUB:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(43);
				statement();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LocalVariableDeclarationContext extends ParserRuleContext {
		public PrimitiveTypeContext primitiveType() {
			return getRuleContext(PrimitiveTypeContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public TerminalNode ASSIGN() { return getToken(MiniJavaParser.ASSIGN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public LocalVariableDeclarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_localVariableDeclaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniJavaParserListener ) ((MiniJavaParserListener)listener).enterLocalVariableDeclaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniJavaParserListener ) ((MiniJavaParserListener)listener).exitLocalVariableDeclaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiniJavaParserVisitor ) return ((MiniJavaParserVisitor<? extends T>)visitor).visitLocalVariableDeclaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LocalVariableDeclarationContext localVariableDeclaration() throws RecognitionException {
		LocalVariableDeclarationContext _localctx = new LocalVariableDeclarationContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_localVariableDeclaration);
		try {
			setState(54);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(46);
				primitiveType();
				setState(47);
				identifier();
				setState(48);
				match(ASSIGN);
				setState(49);
				expression(0);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(51);
				primitiveType();
				setState(52);
				identifier();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StatementContext extends ParserRuleContext {
		public BlockContext block() {
			return getRuleContext(BlockContext.class,0);
		}
		public TerminalNode IF() { return getToken(MiniJavaParser.IF, 0); }
		public ParExpressionContext parExpression() {
			return getRuleContext(ParExpressionContext.class,0);
		}
		public List<StatementContext> statement() {
			return getRuleContexts(StatementContext.class);
		}
		public StatementContext statement(int i) {
			return getRuleContext(StatementContext.class,i);
		}
		public TerminalNode ELSE() { return getToken(MiniJavaParser.ELSE, 0); }
		public TerminalNode FOR() { return getToken(MiniJavaParser.FOR, 0); }
		public TerminalNode LPAREN() { return getToken(MiniJavaParser.LPAREN, 0); }
		public ForControlContext forControl() {
			return getRuleContext(ForControlContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(MiniJavaParser.RPAREN, 0); }
		public TerminalNode WHILE() { return getToken(MiniJavaParser.WHILE, 0); }
		public TerminalNode BREAK() { return getToken(MiniJavaParser.BREAK, 0); }
		public TerminalNode SEMI() { return getToken(MiniJavaParser.SEMI, 0); }
		public TerminalNode CONTINUE() { return getToken(MiniJavaParser.CONTINUE, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public StatementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniJavaParserListener ) ((MiniJavaParserListener)listener).enterStatement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniJavaParserListener ) ((MiniJavaParserListener)listener).exitStatement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiniJavaParserVisitor ) return ((MiniJavaParserVisitor<? extends T>)visitor).visitStatement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StatementContext statement() throws RecognitionException {
		StatementContext _localctx = new StatementContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_statement);
		try {
			setState(82);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LBRACE:
				enterOuterAlt(_localctx, 1);
				{
				setState(56);
				block();
				}
				break;
			case IF:
				enterOuterAlt(_localctx, 2);
				{
				setState(57);
				match(IF);
				setState(58);
				parExpression();
				setState(59);
				statement();
				setState(62);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
				case 1:
					{
					setState(60);
					match(ELSE);
					setState(61);
					statement();
					}
					break;
				}
				}
				break;
			case FOR:
				enterOuterAlt(_localctx, 3);
				{
				setState(64);
				match(FOR);
				setState(65);
				match(LPAREN);
				setState(66);
				forControl();
				setState(67);
				match(RPAREN);
				setState(68);
				statement();
				}
				break;
			case WHILE:
				enterOuterAlt(_localctx, 4);
				{
				setState(70);
				match(WHILE);
				setState(71);
				parExpression();
				setState(72);
				statement();
				}
				break;
			case BREAK:
				enterOuterAlt(_localctx, 5);
				{
				setState(74);
				match(BREAK);
				setState(75);
				match(SEMI);
				}
				break;
			case CONTINUE:
				enterOuterAlt(_localctx, 6);
				{
				setState(76);
				match(CONTINUE);
				setState(77);
				match(SEMI);
				}
				break;
			case SEMI:
				enterOuterAlt(_localctx, 7);
				{
				setState(78);
				match(SEMI);
				}
				break;
			case ASSERT:
			case MODULE:
			case OPEN:
			case REQUIRES:
			case EXPORTS:
			case OPENS:
			case TO:
			case USES:
			case PROVIDES:
			case WITH:
			case TRANSITIVE:
			case VAR:
			case YIELD:
			case RECORD:
			case SEALED:
			case PERMITS:
			case DECIMAL_LITERAL:
			case BOOL_LITERAL:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NULL_LITERAL:
			case LPAREN:
			case BANG:
			case TILDE:
			case INC:
			case DEC:
			case ADD:
			case SUB:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 8);
				{
				setState(79);
				expression(0);
				setState(80);
				match(SEMI);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ParExpressionContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(MiniJavaParser.LPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(MiniJavaParser.RPAREN, 0); }
		public ParExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parExpression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniJavaParserListener ) ((MiniJavaParserListener)listener).enterParExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniJavaParserListener ) ((MiniJavaParserListener)listener).exitParExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiniJavaParserVisitor ) return ((MiniJavaParserVisitor<? extends T>)visitor).visitParExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParExpressionContext parExpression() throws RecognitionException {
		ParExpressionContext _localctx = new ParExpressionContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_parExpression);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(84);
			match(LPAREN);
			setState(85);
			expression(0);
			setState(86);
			match(RPAREN);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ForControlContext extends ParserRuleContext {
		public ExpressionListContext forUpdate;
		public List<TerminalNode> SEMI() { return getTokens(MiniJavaParser.SEMI); }
		public TerminalNode SEMI(int i) {
			return getToken(MiniJavaParser.SEMI, i);
		}
		public ForInitContext forInit() {
			return getRuleContext(ForInitContext.class,0);
		}
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public ForControlContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forControl; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniJavaParserListener ) ((MiniJavaParserListener)listener).enterForControl(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniJavaParserListener ) ((MiniJavaParserListener)listener).exitForControl(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiniJavaParserVisitor ) return ((MiniJavaParserVisitor<? extends T>)visitor).visitForControl(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForControlContext forControl() throws RecognitionException {
		ForControlContext _localctx = new ForControlContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_forControl);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(89);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -4503599224717044L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 2061987011607L) != 0) || _la==IDENTIFIER) {
				{
				setState(88);
				forInit();
				}
			}

			setState(91);
			match(SEMI);
			setState(93);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -4503599627370492L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 2061987011607L) != 0) || _la==IDENTIFIER) {
				{
				setState(92);
				expression(0);
				}
			}

			setState(95);
			match(SEMI);
			setState(97);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & -4503599627370492L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 2061987011607L) != 0) || _la==IDENTIFIER) {
				{
				setState(96);
				((ForControlContext)_localctx).forUpdate = expressionList();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ForInitContext extends ParserRuleContext {
		public LocalVariableDeclarationContext localVariableDeclaration() {
			return getRuleContext(LocalVariableDeclarationContext.class,0);
		}
		public ExpressionListContext expressionList() {
			return getRuleContext(ExpressionListContext.class,0);
		}
		public ForInitContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_forInit; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniJavaParserListener ) ((MiniJavaParserListener)listener).enterForInit(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniJavaParserListener ) ((MiniJavaParserListener)listener).exitForInit(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiniJavaParserVisitor ) return ((MiniJavaParserVisitor<? extends T>)visitor).visitForInit(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ForInitContext forInit() throws RecognitionException {
		ForInitContext _localctx = new ForInitContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_forInit);
		try {
			setState(101);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case BOOLEAN:
			case CHAR:
			case INT:
			case STRING:
				enterOuterAlt(_localctx, 1);
				{
				setState(99);
				localVariableDeclaration();
				}
				break;
			case ASSERT:
			case MODULE:
			case OPEN:
			case REQUIRES:
			case EXPORTS:
			case OPENS:
			case TO:
			case USES:
			case PROVIDES:
			case WITH:
			case TRANSITIVE:
			case VAR:
			case YIELD:
			case RECORD:
			case SEALED:
			case PERMITS:
			case DECIMAL_LITERAL:
			case BOOL_LITERAL:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NULL_LITERAL:
			case LPAREN:
			case BANG:
			case TILDE:
			case INC:
			case DEC:
			case ADD:
			case SUB:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(100);
				expressionList();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionListContext extends ParserRuleContext {
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public List<TerminalNode> COMMA() { return getTokens(MiniJavaParser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(MiniJavaParser.COMMA, i);
		}
		public ExpressionListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expressionList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniJavaParserListener ) ((MiniJavaParserListener)listener).enterExpressionList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniJavaParserListener ) ((MiniJavaParserListener)listener).exitExpressionList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiniJavaParserVisitor ) return ((MiniJavaParserVisitor<? extends T>)visitor).visitExpressionList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionListContext expressionList() throws RecognitionException {
		ExpressionListContext _localctx = new ExpressionListContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_expressionList);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(103);
			expression(0);
			setState(108);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMMA) {
				{
				{
				setState(104);
				match(COMMA);
				setState(105);
				expression(0);
				}
				}
				setState(110);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ExpressionContext extends ParserRuleContext {
		public Token prefix;
		public Token bop;
		public Token postfix;
		public PrimaryContext primary() {
			return getRuleContext(PrimaryContext.class,0);
		}
		public List<ExpressionContext> expression() {
			return getRuleContexts(ExpressionContext.class);
		}
		public ExpressionContext expression(int i) {
			return getRuleContext(ExpressionContext.class,i);
		}
		public TerminalNode ADD() { return getToken(MiniJavaParser.ADD, 0); }
		public TerminalNode SUB() { return getToken(MiniJavaParser.SUB, 0); }
		public TerminalNode INC() { return getToken(MiniJavaParser.INC, 0); }
		public TerminalNode DEC() { return getToken(MiniJavaParser.DEC, 0); }
		public TerminalNode TILDE() { return getToken(MiniJavaParser.TILDE, 0); }
		public TerminalNode BANG() { return getToken(MiniJavaParser.BANG, 0); }
		public TerminalNode LPAREN() { return getToken(MiniJavaParser.LPAREN, 0); }
		public PrimitiveTypeContext primitiveType() {
			return getRuleContext(PrimitiveTypeContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(MiniJavaParser.RPAREN, 0); }
		public TerminalNode MUL() { return getToken(MiniJavaParser.MUL, 0); }
		public TerminalNode DIV() { return getToken(MiniJavaParser.DIV, 0); }
		public TerminalNode MOD() { return getToken(MiniJavaParser.MOD, 0); }
		public TerminalNode LSHIFT() { return getToken(MiniJavaParser.LSHIFT, 0); }
		public TerminalNode URSHIFT() { return getToken(MiniJavaParser.URSHIFT, 0); }
		public TerminalNode RSHIFT() { return getToken(MiniJavaParser.RSHIFT, 0); }
		public TerminalNode LE() { return getToken(MiniJavaParser.LE, 0); }
		public TerminalNode GE() { return getToken(MiniJavaParser.GE, 0); }
		public TerminalNode GT() { return getToken(MiniJavaParser.GT, 0); }
		public TerminalNode LT() { return getToken(MiniJavaParser.LT, 0); }
		public TerminalNode EQUAL() { return getToken(MiniJavaParser.EQUAL, 0); }
		public TerminalNode NOTEQUAL() { return getToken(MiniJavaParser.NOTEQUAL, 0); }
		public TerminalNode BITAND() { return getToken(MiniJavaParser.BITAND, 0); }
		public TerminalNode CARET() { return getToken(MiniJavaParser.CARET, 0); }
		public TerminalNode BITOR() { return getToken(MiniJavaParser.BITOR, 0); }
		public TerminalNode AND() { return getToken(MiniJavaParser.AND, 0); }
		public TerminalNode OR() { return getToken(MiniJavaParser.OR, 0); }
		public TerminalNode COLON() { return getToken(MiniJavaParser.COLON, 0); }
		public TerminalNode QUESTION() { return getToken(MiniJavaParser.QUESTION, 0); }
		public TerminalNode ASSIGN() { return getToken(MiniJavaParser.ASSIGN, 0); }
		public TerminalNode ADD_ASSIGN() { return getToken(MiniJavaParser.ADD_ASSIGN, 0); }
		public TerminalNode SUB_ASSIGN() { return getToken(MiniJavaParser.SUB_ASSIGN, 0); }
		public TerminalNode MUL_ASSIGN() { return getToken(MiniJavaParser.MUL_ASSIGN, 0); }
		public TerminalNode DIV_ASSIGN() { return getToken(MiniJavaParser.DIV_ASSIGN, 0); }
		public TerminalNode AND_ASSIGN() { return getToken(MiniJavaParser.AND_ASSIGN, 0); }
		public TerminalNode OR_ASSIGN() { return getToken(MiniJavaParser.OR_ASSIGN, 0); }
		public TerminalNode XOR_ASSIGN() { return getToken(MiniJavaParser.XOR_ASSIGN, 0); }
		public TerminalNode RSHIFT_ASSIGN() { return getToken(MiniJavaParser.RSHIFT_ASSIGN, 0); }
		public TerminalNode URSHIFT_ASSIGN() { return getToken(MiniJavaParser.URSHIFT_ASSIGN, 0); }
		public TerminalNode LSHIFT_ASSIGN() { return getToken(MiniJavaParser.LSHIFT_ASSIGN, 0); }
		public TerminalNode MOD_ASSIGN() { return getToken(MiniJavaParser.MOD_ASSIGN, 0); }
		public ExpressionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expression; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniJavaParserListener ) ((MiniJavaParserListener)listener).enterExpression(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniJavaParserListener ) ((MiniJavaParserListener)listener).exitExpression(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiniJavaParserVisitor ) return ((MiniJavaParserVisitor<? extends T>)visitor).visitExpression(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpressionContext expression() throws RecognitionException {
		return expression(0);
	}

	private ExpressionContext expression(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
		ExpressionContext _prevctx = _localctx;
		int _startState = 18;
		enterRecursionRule(_localctx, 18, RULE_expression, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(120);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				setState(112);
				primary();
				}
				break;
			case 2:
				{
				setState(113);
				((ExpressionContext)_localctx).prefix = _input.LT(1);
				_la = _input.LA(1);
				if ( !(((((_la - 91)) & ~0x3f) == 0 && ((1L << (_la - 91)) & 15363L) != 0)) ) {
					((ExpressionContext)_localctx).prefix = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(114);
				expression(14);
				}
				break;
			case 3:
				{
				setState(115);
				match(LPAREN);
				setState(116);
				primitiveType();
				setState(117);
				match(RPAREN);
				setState(118);
				expression(13);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(165);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(163);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
					case 1:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(122);
						if (!(precpred(_ctx, 12))) throw new FailedPredicateException(this, "precpred(_ctx, 12)");
						setState(123);
						((ExpressionContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 105)) & ~0x3f) == 0 && ((1L << (_la - 105)) & 35L) != 0)) ) {
							((ExpressionContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(124);
						expression(13);
						}
						break;
					case 2:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(125);
						if (!(precpred(_ctx, 11))) throw new FailedPredicateException(this, "precpred(_ctx, 11)");
						setState(126);
						((ExpressionContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==ADD || _la==SUB) ) {
							((ExpressionContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(127);
						expression(12);
						}
						break;
					case 3:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(128);
						if (!(precpred(_ctx, 10))) throw new FailedPredicateException(this, "precpred(_ctx, 10)");
						setState(129);
						((ExpressionContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 111)) & ~0x3f) == 0 && ((1L << (_la - 111)) & 7L) != 0)) ) {
							((ExpressionContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(130);
						expression(11);
						}
						break;
					case 4:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(131);
						if (!(precpred(_ctx, 9))) throw new FailedPredicateException(this, "precpred(_ctx, 9)");
						setState(132);
						((ExpressionContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 89)) & ~0x3f) == 0 && ((1L << (_la - 89)) & 387L) != 0)) ) {
							((ExpressionContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(133);
						expression(10);
						}
						break;
					case 5:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(134);
						if (!(precpred(_ctx, 8))) throw new FailedPredicateException(this, "precpred(_ctx, 8)");
						setState(135);
						((ExpressionContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==EQUAL || _la==NOTEQUAL) ) {
							((ExpressionContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(136);
						expression(9);
						}
						break;
					case 6:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(137);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(138);
						((ExpressionContext)_localctx).bop = match(BITAND);
						setState(139);
						expression(8);
						}
						break;
					case 7:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(140);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(141);
						((ExpressionContext)_localctx).bop = match(CARET);
						setState(142);
						expression(7);
						}
						break;
					case 8:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(143);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(144);
						((ExpressionContext)_localctx).bop = match(BITOR);
						setState(145);
						expression(6);
						}
						break;
					case 9:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(146);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(147);
						((ExpressionContext)_localctx).bop = match(AND);
						setState(148);
						expression(5);
						}
						break;
					case 10:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(149);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(150);
						((ExpressionContext)_localctx).bop = match(OR);
						setState(151);
						expression(4);
						}
						break;
					case 11:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(152);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(153);
						((ExpressionContext)_localctx).bop = match(QUESTION);
						setState(154);
						expression(0);
						setState(155);
						match(COLON);
						setState(156);
						expression(2);
						}
						break;
					case 12:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(158);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(159);
						((ExpressionContext)_localctx).bop = _input.LT(1);
						_la = _input.LA(1);
						if ( !(((((_la - 88)) & ~0x3f) == 0 && ((1L << (_la - 88)) & 137371844609L) != 0)) ) {
							((ExpressionContext)_localctx).bop = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(160);
						expression(1);
						}
						break;
					case 13:
						{
						_localctx = new ExpressionContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_expression);
						setState(161);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(162);
						((ExpressionContext)_localctx).postfix = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==INC || _la==DEC) ) {
							((ExpressionContext)_localctx).postfix = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						}
						break;
					}
					} 
				}
				setState(167);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,12,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrimaryContext extends ParserRuleContext {
		public TerminalNode LPAREN() { return getToken(MiniJavaParser.LPAREN, 0); }
		public ExpressionContext expression() {
			return getRuleContext(ExpressionContext.class,0);
		}
		public TerminalNode RPAREN() { return getToken(MiniJavaParser.RPAREN, 0); }
		public LiteralContext literal() {
			return getRuleContext(LiteralContext.class,0);
		}
		public IdentifierContext identifier() {
			return getRuleContext(IdentifierContext.class,0);
		}
		public PrimaryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primary; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniJavaParserListener ) ((MiniJavaParserListener)listener).enterPrimary(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniJavaParserListener ) ((MiniJavaParserListener)listener).exitPrimary(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiniJavaParserVisitor ) return ((MiniJavaParserVisitor<? extends T>)visitor).visitPrimary(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimaryContext primary() throws RecognitionException {
		PrimaryContext _localctx = new PrimaryContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_primary);
		try {
			setState(174);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case LPAREN:
				enterOuterAlt(_localctx, 1);
				{
				setState(168);
				match(LPAREN);
				setState(169);
				expression(0);
				setState(170);
				match(RPAREN);
				}
				break;
			case DECIMAL_LITERAL:
			case BOOL_LITERAL:
			case CHAR_LITERAL:
			case STRING_LITERAL:
			case NULL_LITERAL:
				enterOuterAlt(_localctx, 2);
				{
				setState(172);
				literal();
				}
				break;
			case ASSERT:
			case MODULE:
			case OPEN:
			case REQUIRES:
			case EXPORTS:
			case OPENS:
			case TO:
			case USES:
			case PROVIDES:
			case WITH:
			case TRANSITIVE:
			case VAR:
			case YIELD:
			case RECORD:
			case SEALED:
			case PERMITS:
			case IDENTIFIER:
				enterOuterAlt(_localctx, 3);
				{
				setState(173);
				identifier();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class LiteralContext extends ParserRuleContext {
		public TerminalNode DECIMAL_LITERAL() { return getToken(MiniJavaParser.DECIMAL_LITERAL, 0); }
		public TerminalNode CHAR_LITERAL() { return getToken(MiniJavaParser.CHAR_LITERAL, 0); }
		public TerminalNode STRING_LITERAL() { return getToken(MiniJavaParser.STRING_LITERAL, 0); }
		public TerminalNode BOOL_LITERAL() { return getToken(MiniJavaParser.BOOL_LITERAL, 0); }
		public TerminalNode NULL_LITERAL() { return getToken(MiniJavaParser.NULL_LITERAL, 0); }
		public LiteralContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_literal; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniJavaParserListener ) ((MiniJavaParserListener)listener).enterLiteral(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniJavaParserListener ) ((MiniJavaParserListener)listener).exitLiteral(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiniJavaParserVisitor ) return ((MiniJavaParserVisitor<? extends T>)visitor).visitLiteral(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LiteralContext literal() throws RecognitionException {
		LiteralContext _localctx = new LiteralContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_literal);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(176);
			_la = _input.LA(1);
			if ( !(((((_la - 68)) & ~0x3f) == 0 && ((1L << (_la - 68)) & 1473L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IdentifierContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(MiniJavaParser.IDENTIFIER, 0); }
		public TerminalNode MODULE() { return getToken(MiniJavaParser.MODULE, 0); }
		public TerminalNode OPEN() { return getToken(MiniJavaParser.OPEN, 0); }
		public TerminalNode REQUIRES() { return getToken(MiniJavaParser.REQUIRES, 0); }
		public TerminalNode EXPORTS() { return getToken(MiniJavaParser.EXPORTS, 0); }
		public TerminalNode OPENS() { return getToken(MiniJavaParser.OPENS, 0); }
		public TerminalNode TO() { return getToken(MiniJavaParser.TO, 0); }
		public TerminalNode USES() { return getToken(MiniJavaParser.USES, 0); }
		public TerminalNode PROVIDES() { return getToken(MiniJavaParser.PROVIDES, 0); }
		public TerminalNode WITH() { return getToken(MiniJavaParser.WITH, 0); }
		public TerminalNode TRANSITIVE() { return getToken(MiniJavaParser.TRANSITIVE, 0); }
		public TerminalNode YIELD() { return getToken(MiniJavaParser.YIELD, 0); }
		public TerminalNode SEALED() { return getToken(MiniJavaParser.SEALED, 0); }
		public TerminalNode PERMITS() { return getToken(MiniJavaParser.PERMITS, 0); }
		public TerminalNode RECORD() { return getToken(MiniJavaParser.RECORD, 0); }
		public TerminalNode VAR() { return getToken(MiniJavaParser.VAR, 0); }
		public TerminalNode ASSERT() { return getToken(MiniJavaParser.ASSERT, 0); }
		public IdentifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_identifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniJavaParserListener ) ((MiniJavaParserListener)listener).enterIdentifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniJavaParserListener ) ((MiniJavaParserListener)listener).exitIdentifier(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiniJavaParserVisitor ) return ((MiniJavaParserVisitor<? extends T>)visitor).visitIdentifier(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdentifierContext identifier() throws RecognitionException {
		IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_identifier);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(178);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & -4503599627370492L) != 0) || ((((_la - 64)) & ~0x3f) == 0 && ((1L << (_la - 64)) & 7L) != 0) || _la==IDENTIFIER) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class PrimitiveTypeContext extends ParserRuleContext {
		public TerminalNode BOOLEAN() { return getToken(MiniJavaParser.BOOLEAN, 0); }
		public TerminalNode CHAR() { return getToken(MiniJavaParser.CHAR, 0); }
		public TerminalNode INT() { return getToken(MiniJavaParser.INT, 0); }
		public TerminalNode STRING() { return getToken(MiniJavaParser.STRING, 0); }
		public PrimitiveTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_primitiveType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof MiniJavaParserListener ) ((MiniJavaParserListener)listener).enterPrimitiveType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof MiniJavaParserListener ) ((MiniJavaParserListener)listener).exitPrimitiveType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiniJavaParserVisitor ) return ((MiniJavaParserVisitor<? extends T>)visitor).visitPrimitiveType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PrimitiveTypeContext primitiveType() throws RecognitionException {
		PrimitiveTypeContext _localctx = new PrimitiveTypeContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_primitiveType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(180);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 402653448L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 9:
			return expression_sempred((ExpressionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 12);
		case 1:
			return precpred(_ctx, 11);
		case 2:
			return precpred(_ctx, 10);
		case 3:
			return precpred(_ctx, 9);
		case 4:
			return precpred(_ctx, 8);
		case 5:
			return precpred(_ctx, 7);
		case 6:
			return precpred(_ctx, 6);
		case 7:
			return precpred(_ctx, 5);
		case 8:
			return precpred(_ctx, 4);
		case 9:
			return precpred(_ctx, 3);
		case 10:
			return precpred(_ctx, 2);
		case 11:
			return precpred(_ctx, 1);
		case 12:
			return precpred(_ctx, 15);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001\u0084\u00b7\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001"+
		"\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004"+
		"\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007"+
		"\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b"+
		"\u0002\f\u0007\f\u0002\r\u0007\r\u0001\u0000\u0001\u0000\u0001\u0000\u0001"+
		"\u0001\u0001\u0001\u0005\u0001\"\b\u0001\n\u0001\f\u0001%\t\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0002\u0003"+
		"\u0002-\b\u0002\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001"+
		"\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0003\u00037\b\u0003\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0003"+
		"\u0004?\b\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001"+
		"\u0004\u0001\u0004\u0003\u0004S\b\u0004\u0001\u0005\u0001\u0005\u0001"+
		"\u0005\u0001\u0005\u0001\u0006\u0003\u0006Z\b\u0006\u0001\u0006\u0001"+
		"\u0006\u0003\u0006^\b\u0006\u0001\u0006\u0001\u0006\u0003\u0006b\b\u0006"+
		"\u0001\u0007\u0001\u0007\u0003\u0007f\b\u0007\u0001\b\u0001\b\u0001\b"+
		"\u0005\bk\b\b\n\b\f\bn\t\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0003\ty\b\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0005\t\u00a4\b\t\n\t\f\t\u00a7\t\t\u0001\n\u0001\n\u0001\n"+
		"\u0001\n\u0001\n\u0001\n\u0003\n\u00af\b\n\u0001\u000b\u0001\u000b\u0001"+
		"\f\u0001\f\u0001\r\u0001\r\u0001\r\u0000\u0001\u0012\u000e\u0000\u0002"+
		"\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u0000\u000b"+
		"\u0002\u0000[\\eh\u0002\u0000ijnn\u0001\u0000gh\u0001\u0000oq\u0002\u0000"+
		"YZ`a\u0002\u0000__bb\u0002\u0000XXr|\u0001\u0000ef\u0003\u0000DDJLNN\u0003"+
		"\u0000\u0002\u00024B\u0084\u0084\u0003\u0000\u0003\u0003\b\b\u001b\u001c"+
		"\u00c9\u0000\u001c\u0001\u0000\u0000\u0000\u0002\u001f\u0001\u0000\u0000"+
		"\u0000\u0004,\u0001\u0000\u0000\u0000\u00066\u0001\u0000\u0000\u0000\b"+
		"R\u0001\u0000\u0000\u0000\nT\u0001\u0000\u0000\u0000\fY\u0001\u0000\u0000"+
		"\u0000\u000ee\u0001\u0000\u0000\u0000\u0010g\u0001\u0000\u0000\u0000\u0012"+
		"x\u0001\u0000\u0000\u0000\u0014\u00ae\u0001\u0000\u0000\u0000\u0016\u00b0"+
		"\u0001\u0000\u0000\u0000\u0018\u00b2\u0001\u0000\u0000\u0000\u001a\u00b4"+
		"\u0001\u0000\u0000\u0000\u001c\u001d\u0003\u0002\u0001\u0000\u001d\u001e"+
		"\u0005\u0000\u0000\u0001\u001e\u0001\u0001\u0000\u0000\u0000\u001f#\u0005"+
		"Q\u0000\u0000 \"\u0003\u0004\u0002\u0000! \u0001\u0000\u0000\u0000\"%"+
		"\u0001\u0000\u0000\u0000#!\u0001\u0000\u0000\u0000#$\u0001\u0000\u0000"+
		"\u0000$&\u0001\u0000\u0000\u0000%#\u0001\u0000\u0000\u0000&\'\u0005R\u0000"+
		"\u0000\'\u0003\u0001\u0000\u0000\u0000()\u0003\u0006\u0003\u0000)*\u0005"+
		"U\u0000\u0000*-\u0001\u0000\u0000\u0000+-\u0003\b\u0004\u0000,(\u0001"+
		"\u0000\u0000\u0000,+\u0001\u0000\u0000\u0000-\u0005\u0001\u0000\u0000"+
		"\u0000./\u0003\u001a\r\u0000/0\u0003\u0018\f\u000001\u0005X\u0000\u0000"+
		"12\u0003\u0012\t\u000027\u0001\u0000\u0000\u000034\u0003\u001a\r\u0000"+
		"45\u0003\u0018\f\u000057\u0001\u0000\u0000\u00006.\u0001\u0000\u0000\u0000"+
		"63\u0001\u0000\u0000\u00007\u0007\u0001\u0000\u0000\u00008S\u0003\u0002"+
		"\u0001\u00009:\u0005\u0016\u0000\u0000:;\u0003\n\u0005\u0000;>\u0003\b"+
		"\u0004\u0000<=\u0005\u000f\u0000\u0000=?\u0003\b\u0004\u0000><\u0001\u0000"+
		"\u0000\u0000>?\u0001\u0000\u0000\u0000?S\u0001\u0000\u0000\u0000@A\u0005"+
		"\u0015\u0000\u0000AB\u0005O\u0000\u0000BC\u0003\f\u0006\u0000CD\u0005"+
		"P\u0000\u0000DE\u0003\b\u0004\u0000ES\u0001\u0000\u0000\u0000FG\u0005"+
		"3\u0000\u0000GH\u0003\n\u0005\u0000HI\u0003\b\u0004\u0000IS\u0001\u0000"+
		"\u0000\u0000JK\u0005\u0004\u0000\u0000KS\u0005U\u0000\u0000LM\u0005\u000b"+
		"\u0000\u0000MS\u0005U\u0000\u0000NS\u0005U\u0000\u0000OP\u0003\u0012\t"+
		"\u0000PQ\u0005U\u0000\u0000QS\u0001\u0000\u0000\u0000R8\u0001\u0000\u0000"+
		"\u0000R9\u0001\u0000\u0000\u0000R@\u0001\u0000\u0000\u0000RF\u0001\u0000"+
		"\u0000\u0000RJ\u0001\u0000\u0000\u0000RL\u0001\u0000\u0000\u0000RN\u0001"+
		"\u0000\u0000\u0000RO\u0001\u0000\u0000\u0000S\t\u0001\u0000\u0000\u0000"+
		"TU\u0005O\u0000\u0000UV\u0003\u0012\t\u0000VW\u0005P\u0000\u0000W\u000b"+
		"\u0001\u0000\u0000\u0000XZ\u0003\u000e\u0007\u0000YX\u0001\u0000\u0000"+
		"\u0000YZ\u0001\u0000\u0000\u0000Z[\u0001\u0000\u0000\u0000[]\u0005U\u0000"+
		"\u0000\\^\u0003\u0012\t\u0000]\\\u0001\u0000\u0000\u0000]^\u0001\u0000"+
		"\u0000\u0000^_\u0001\u0000\u0000\u0000_a\u0005U\u0000\u0000`b\u0003\u0010"+
		"\b\u0000a`\u0001\u0000\u0000\u0000ab\u0001\u0000\u0000\u0000b\r\u0001"+
		"\u0000\u0000\u0000cf\u0003\u0006\u0003\u0000df\u0003\u0010\b\u0000ec\u0001"+
		"\u0000\u0000\u0000ed\u0001\u0000\u0000\u0000f\u000f\u0001\u0000\u0000"+
		"\u0000gl\u0003\u0012\t\u0000hi\u0005V\u0000\u0000ik\u0003\u0012\t\u0000"+
		"jh\u0001\u0000\u0000\u0000kn\u0001\u0000\u0000\u0000lj\u0001\u0000\u0000"+
		"\u0000lm\u0001\u0000\u0000\u0000m\u0011\u0001\u0000\u0000\u0000nl\u0001"+
		"\u0000\u0000\u0000op\u0006\t\uffff\uffff\u0000py\u0003\u0014\n\u0000q"+
		"r\u0007\u0000\u0000\u0000ry\u0003\u0012\t\u000est\u0005O\u0000\u0000t"+
		"u\u0003\u001a\r\u0000uv\u0005P\u0000\u0000vw\u0003\u0012\t\rwy\u0001\u0000"+
		"\u0000\u0000xo\u0001\u0000\u0000\u0000xq\u0001\u0000\u0000\u0000xs\u0001"+
		"\u0000\u0000\u0000y\u00a5\u0001\u0000\u0000\u0000z{\n\f\u0000\u0000{|"+
		"\u0007\u0001\u0000\u0000|\u00a4\u0003\u0012\t\r}~\n\u000b\u0000\u0000"+
		"~\u007f\u0007\u0002\u0000\u0000\u007f\u00a4\u0003\u0012\t\f\u0080\u0081"+
		"\n\n\u0000\u0000\u0081\u0082\u0007\u0003\u0000\u0000\u0082\u00a4\u0003"+
		"\u0012\t\u000b\u0083\u0084\n\t\u0000\u0000\u0084\u0085\u0007\u0004\u0000"+
		"\u0000\u0085\u00a4\u0003\u0012\t\n\u0086\u0087\n\b\u0000\u0000\u0087\u0088"+
		"\u0007\u0005\u0000\u0000\u0088\u00a4\u0003\u0012\t\t\u0089\u008a\n\u0007"+
		"\u0000\u0000\u008a\u008b\u0005k\u0000\u0000\u008b\u00a4\u0003\u0012\t"+
		"\b\u008c\u008d\n\u0006\u0000\u0000\u008d\u008e\u0005m\u0000\u0000\u008e"+
		"\u00a4\u0003\u0012\t\u0007\u008f\u0090\n\u0005\u0000\u0000\u0090\u0091"+
		"\u0005l\u0000\u0000\u0091\u00a4\u0003\u0012\t\u0006\u0092\u0093\n\u0004"+
		"\u0000\u0000\u0093\u0094\u0005c\u0000\u0000\u0094\u00a4\u0003\u0012\t"+
		"\u0005\u0095\u0096\n\u0003\u0000\u0000\u0096\u0097\u0005d\u0000\u0000"+
		"\u0097\u00a4\u0003\u0012\t\u0004\u0098\u0099\n\u0002\u0000\u0000\u0099"+
		"\u009a\u0005]\u0000\u0000\u009a\u009b\u0003\u0012\t\u0000\u009b\u009c"+
		"\u0005^\u0000\u0000\u009c\u009d\u0003\u0012\t\u0002\u009d\u00a4\u0001"+
		"\u0000\u0000\u0000\u009e\u009f\n\u0001\u0000\u0000\u009f\u00a0\u0007\u0006"+
		"\u0000\u0000\u00a0\u00a4\u0003\u0012\t\u0001\u00a1\u00a2\n\u000f\u0000"+
		"\u0000\u00a2\u00a4\u0007\u0007\u0000\u0000\u00a3z\u0001\u0000\u0000\u0000"+
		"\u00a3}\u0001\u0000\u0000\u0000\u00a3\u0080\u0001\u0000\u0000\u0000\u00a3"+
		"\u0083\u0001\u0000\u0000\u0000\u00a3\u0086\u0001\u0000\u0000\u0000\u00a3"+
		"\u0089\u0001\u0000\u0000\u0000\u00a3\u008c\u0001\u0000\u0000\u0000\u00a3"+
		"\u008f\u0001\u0000\u0000\u0000\u00a3\u0092\u0001\u0000\u0000\u0000\u00a3"+
		"\u0095\u0001\u0000\u0000\u0000\u00a3\u0098\u0001\u0000\u0000\u0000\u00a3"+
		"\u009e\u0001\u0000\u0000\u0000\u00a3\u00a1\u0001\u0000\u0000\u0000\u00a4"+
		"\u00a7\u0001\u0000\u0000\u0000\u00a5\u00a3\u0001\u0000\u0000\u0000\u00a5"+
		"\u00a6\u0001\u0000\u0000\u0000\u00a6\u0013\u0001\u0000\u0000\u0000\u00a7"+
		"\u00a5\u0001\u0000\u0000\u0000\u00a8\u00a9\u0005O\u0000\u0000\u00a9\u00aa"+
		"\u0003\u0012\t\u0000\u00aa\u00ab\u0005P\u0000\u0000\u00ab\u00af\u0001"+
		"\u0000\u0000\u0000\u00ac\u00af\u0003\u0016\u000b\u0000\u00ad\u00af\u0003"+
		"\u0018\f\u0000\u00ae\u00a8\u0001\u0000\u0000\u0000\u00ae\u00ac\u0001\u0000"+
		"\u0000\u0000\u00ae\u00ad\u0001\u0000\u0000\u0000\u00af\u0015\u0001\u0000"+
		"\u0000\u0000\u00b0\u00b1\u0007\b\u0000\u0000\u00b1\u0017\u0001\u0000\u0000"+
		"\u0000\u00b2\u00b3\u0007\t\u0000\u0000\u00b3\u0019\u0001\u0000\u0000\u0000"+
		"\u00b4\u00b5\u0007\n\u0000\u0000\u00b5\u001b\u0001\u0000\u0000\u0000\u000e"+
		"#,6>RY]aelx\u00a3\u00a5\u00ae";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}