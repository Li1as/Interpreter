package cn.edu.nju.cs;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;

public class Main {
    public static void run(File mjFile) throws Exception {
        var input = CharStreams.fromFileName(mjFile.getAbsolutePath());
        MiniJavaLexer lexer = new MiniJavaLexer(input);
        lexer.removeErrorListeners();
        lexer.addErrorListener(new org.antlr.v4.runtime.BaseErrorListener() {
            @Override
            public void syntaxError(org.antlr.v4.runtime.Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, org.antlr.v4.runtime.RecognitionException e) {
                System.err.println("Process exits with 34.");
                System.exit(34);
            }
        });
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        MiniJavaParser parser = new MiniJavaParser(tokenStream);
        parser.removeErrorListeners();
        parser.setErrorHandler(new org.antlr.v4.runtime.BailErrorStrategy());
        try {
            ParseTree pt = parser.compilationUnit();
            Interpreter interpreter = new Interpreter();
            Value result = interpreter.visit(pt);
            switch (result.type) {
                case INT -> System.out.println(result.value);
                case BOOL -> System.out.println(result.value);
                case STRING -> System.out.println(result.value);
                case CHAR -> System.out.println((char) (result.asInt() & 0xFF));
            }
        } catch (Exception e) {
            System.err.println("Process exits with 34.");
            System.exit(34);
        }
        // new MiniJavaParserBaseVisitor<>().visit(pt);
    }


    public static void main(String[] args) throws Exception  {
        if(args.length!= 1) {
            System.err.println("Only one argument is allowed: the path of MiniJava file.");
            throw new RuntimeException();
        }
        
        File mjFile = new File(args[0]);
        run(mjFile);
    }
}