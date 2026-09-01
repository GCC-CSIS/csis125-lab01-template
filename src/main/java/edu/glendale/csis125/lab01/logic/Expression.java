package edu.glendale.csis125.lab01.logic;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * A parsed propositional formula.
 *
 * <p>This is a <em>sealed</em> interface: the complete list of things an Expression
 * can be is fixed below, which is what lets {@link #evaluate} use a switch with no
 * default branch. If you add a new record here, the compiler will force you to
 * handle it in every switch — that is the point of sealing.
 *
 * <p>YOU DO NOT NEED TO EDIT THIS FILE. Read it, though: the evaluate method is
 * where your work in Operators actually gets used.
 */
public sealed interface Expression {

    /** A propositional variable, such as p or q. */
    record Var(String name) implements Expression {}

    /** Negation: ¬p */
    record Not(Expression operand) implements Expression {}

    /** Conjunction: p ∧ q */
    record And(Expression left, Expression right) implements Expression {}

    /** Disjunction: p ∨ q */
    record Or(Expression left, Expression right) implements Expression {}

    /** Exclusive or: p ⊕ q */
    record Xor(Expression left, Expression right) implements Expression {}

    /** Implication: p → q */
    record Implies(Expression left, Expression right) implements Expression {}

    /** Biconditional: p ↔ q */
    record Iff(Expression left, Expression right) implements Expression {}

    /**
     * Computes the truth value of this formula under one assignment of truth
     * values to variables.
     *
     * <p>Notice that every case delegates to {@link Operators}. Until you implement
     * those methods, this will throw. Once you do, the whole application works.
     *
     * @param expression the formula to evaluate
     * @param environment maps each variable name to true or false
     * @return the truth value of the formula under that assignment
     */
    static boolean evaluate(Expression expression, Map<String, Boolean> environment) {
        return switch (expression) {
            case Var v -> {
                Boolean value = environment.get(v.name());
                if (value == null) {
                    throw new IllegalArgumentException("No truth value assigned to variable: " + v.name());
                }
                yield value;
            }
            case Not n -> Operators.not(evaluate(n.operand(), environment));
            case And a -> Operators.and(evaluate(a.left(), environment), evaluate(a.right(), environment));
            case Or o -> Operators.or(evaluate(o.left(), environment), evaluate(o.right(), environment));
            case Xor x -> Operators.xor(evaluate(x.left(), environment), evaluate(x.right(), environment));
            case Implies i -> Operators.implies(evaluate(i.left(), environment), evaluate(i.right(), environment));
            case Iff f -> Operators.iff(evaluate(f.left(), environment), evaluate(f.right(), environment));
        };
    }

    /** Collects every variable appearing in the given formulas, in alphabetical order. */
    static Set<String> variablesOf(Expression... expressions) {
        Set<String> names = new TreeSet<>();
        for (Expression expression : expressions) {
            collect(expression, names);
        }
        return names;
    }

    private static void collect(Expression expression, Set<String> names) {
        switch (expression) {
            case Var v -> names.add(v.name());
            case Not n -> collect(n.operand(), names);
            case And a -> { collect(a.left(), names); collect(a.right(), names); }
            case Or o -> { collect(o.left(), names); collect(o.right(), names); }
            case Xor x -> { collect(x.left(), names); collect(x.right(), names); }
            case Implies i -> { collect(i.left(), names); collect(i.right(), names); }
            case Iff f -> { collect(f.left(), names); collect(f.right(), names); }
        }
    }

    /** Renders the formula using standard logic symbols, with parentheses where needed. */
    static String render(Expression expression) {
        return switch (expression) {
            case Var v -> v.name();
            case Not n -> "¬" + parenthesize(n.operand(), true);
            case And a -> parenthesize(a.left(), false) + " ∧ " + parenthesize(a.right(), false);
            case Or o -> parenthesize(o.left(), false) + " ∨ " + parenthesize(o.right(), false);
            case Xor x -> parenthesize(x.left(), false) + " ⊕ " + parenthesize(x.right(), false);
            case Implies i -> parenthesize(i.left(), false) + " → " + parenthesize(i.right(), false);
            case Iff f -> parenthesize(f.left(), false) + " ↔ " + parenthesize(f.right(), false);
        };
    }

    private static String parenthesize(Expression expression, boolean underNegation) {
        boolean atomic = expression instanceof Var || expression instanceof Not;
        if (atomic || (!underNegation && expression instanceof Var)) {
            return render(expression);
        }
        return "(" + render(expression) + ")";
    }
}
