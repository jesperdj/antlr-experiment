package com.jesperdj.antlr.arithmetic;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class ExampleTest {

    @Test
    public void testEval() throws IOException {
        testEval("2 + 3\n", "5\n");
        testEval("2 + 4 * 3\n", "14\n");

        final EvalVisitor evalVisitor = testEval("x = 5\ny = 3\n2 * x + y\n", "13\n");
        assertThat(evalVisitor.getEnvironment())
                .containsEntry("x", 5)
                .containsEntry("y", 3);

        testEval("x = 5\ny = 3\n2 * (x + y)\n", "16\n");
    }

    private EvalVisitor testEval(String input, String expectedOutput) throws IOException {
        final InputStream in = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        final ArithmeticLexer lexer = new ArithmeticLexer(new ANTLRInputStream(in));
        final CommonTokenStream tokens = new CommonTokenStream(lexer);
        final ArithmeticParser parser = new ArithmeticParser(tokens);

        final ParseTree tree = parser.prog();

        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final PrintStream ps = new PrintStream(out, true, StandardCharsets.UTF_8);

        final EvalVisitor evalVisitor = new EvalVisitor(ps);
        evalVisitor.visit(tree);

        final String actualOutput = out.toString(StandardCharsets.UTF_8).replaceAll("\r\n", "\n");

        assertThat(actualOutput).isEqualTo(expectedOutput);

        return evalVisitor;
    }
}
