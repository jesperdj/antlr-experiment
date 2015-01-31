package com.jesperdj.antlr.arithmetic;

import org.antlr.v4.runtime.misc.NotNull;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public final class EvalVisitor extends ArithmeticBaseVisitor<Integer> {

    private final Map<String, Integer> environment = new HashMap<>();

    private final PrintStream out;

    public EvalVisitor(PrintStream out) {
        this.out = out;
    }

    public Map<String, Integer> getEnvironment() {
        return environment;
    }

    @Override
    public Integer visitAssign(@NotNull ArithmeticParser.AssignContext ctx) {
        String id = ctx.ID().getText();
        int value = visit(ctx.expr());
        environment.put(id, value);
        return value;
    }

    @Override
    public Integer visitPrintExpr(@NotNull ArithmeticParser.PrintExprContext ctx) {
        int value = visit(ctx.expr());
        out.println(value);
        return 0;
    }

    @Override
    public Integer visitInt(@NotNull ArithmeticParser.IntContext ctx) {
        return Integer.valueOf(ctx.INT().getText());
    }

    @Override
    public Integer visitId(@NotNull ArithmeticParser.IdContext ctx) {
        String id = ctx.ID().getText();

        if (!environment.containsKey(id)) {
            throw new IllegalArgumentException("Undefined variable: " + id);
        }

        return environment.get(id);
    }

    @Override
    public Integer visitMulDiv(@NotNull ArithmeticParser.MulDivContext ctx) {
        int left = visit(ctx.expr(0));
        int right = visit(ctx.expr(1));

        switch (ctx.op.getType()) {
            case ArithmeticParser.MUL:
                return left * right;

            case ArithmeticParser.DIV:
                return left / right;

            default:
                throw new IllegalStateException("Unexpected operator in visitMulDiv: " + ctx.op);
        }
    }

    @Override
    public Integer visitAddSub(@NotNull ArithmeticParser.AddSubContext ctx) {
        int left = visit(ctx.expr(0));
        int right = visit(ctx.expr(1));

        switch (ctx.op.getType()) {
            case ArithmeticParser.ADD:
                return left + right;

            case ArithmeticParser.SUB:
                return left - right;

            default:
                throw new IllegalStateException("Unexpected operator in visitAddSub: " + ctx.op);
        }
    }

    @Override
    public Integer visitParens(@NotNull ArithmeticParser.ParensContext ctx) {
        return visit(ctx.expr());
    }
}
