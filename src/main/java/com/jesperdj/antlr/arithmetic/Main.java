package com.jesperdj.antlr.arithmetic;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class Main {

    public static void main(String[] args) throws IOException {
        final InputStream in = args.length > 0 ? new FileInputStream(args[0]) : System.in;

        final ArithmeticLexer lexer = new ArithmeticLexer(new ANTLRInputStream(in));
        final CommonTokenStream tokens = new CommonTokenStream(lexer);
        final ArithmeticParser parser = new ArithmeticParser(tokens);

        final ParseTree tree = parser.prog();

        System.out.println(tree.toStringTree(parser));

        new EvalVisitor().visit(tree);
    }
}
