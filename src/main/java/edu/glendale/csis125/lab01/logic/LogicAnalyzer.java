package edu.glendale.csis125.lab01.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * Classifies formulas and decides logical equivalence.
 *
 * <p>=== PART 2 OF YOUR LAB. Four methods to write. ===
 *
 * <p>Every method here works the same way: walk through all 2^n rows of the truth
 * table and look at the results. That is the whole idea — these properties are
 * defined by what happens across <em>every</em> assignment, not by any clever
 * manipulation of the formula.
 *
 * <p>The tool you need is {@code tables.assignments(...)}, which hands you the list
 * of rows, and {@code Expression.evaluate(formula, assignment)}, which gives you the
 * truth value for one row. A loop over the first, calling the second, is the shape
 * of all four answers.
 */
@Service
public class LogicAnalyzer {

    private final TruthTableService tables;

    /**
     * Spring passes in the TruthTableService automatically when it builds this object.
     * This is constructor injection, and it is why the field above can be final.
     */
    public LogicAnalyzer(TruthTableService tables) {
        this.tables = tables;
    }

    /**
     * Is this formula a tautology — true under every assignment?
     *
     * <p>TODO 5: replace the exception below with a working implementation.
     *
     * <p>Example: {@code p ∨ ¬p} is a tautology. So is {@code p → p}.
     *
     * <p>APPROACH: get the assignments with {@code tables.assignments(formula)}, then
     * loop over them. If you ever find an assignment where the formula evaluates to
     * false, you can stop immediately and return false — one counterexample is enough
     * to disprove a "for all" claim. If the loop finishes without finding one, return true.
     */
    public boolean isTautology(Expression formula) {
        throw new UnsupportedOperationException("TODO 5: implement isTautology(formula)");
    }

    /**
     * Is this formula a contradiction — false under every assignment?
     *
     * <p>TODO 6: replace the exception below with a working implementation.
     *
     * <p>Example: {@code p ∧ ¬p} is a contradiction.
     *
     * <p>APPROACH: the same loop as isTautology with the comparison flipped. There is
     * also a one-line answer here: a formula is a contradiction exactly when its
     * negation is a tautology, and you can build that negation with
     * {@code new Expression.Not(formula)}. Either solution is accepted. If you write
     * the one-liner, make sure you can explain why it is correct.
     */
    public boolean isContradiction(Expression formula) {
        throw new UnsupportedOperationException("TODO 6: implement isContradiction(formula)");
    }

    /**
     * Is this formula satisfiable — true under at least one assignment?
     *
     * <p>TODO 7: replace the exception below with a working implementation.
     *
     * <p>Example: {@code p ∧ q} is satisfiable (take p and q both true), but it is not
     * a tautology. Satisfiability is a much weaker property than being a tautology.
     *
     * <p>APPROACH: this is the exact opposite of being a contradiction. You can write
     * the loop, or you can write one line in terms of {@link #isContradiction}.
     *
     * <p>WORTH KNOWING: deciding satisfiability for large formulas is the SAT problem,
     * the first problem ever proved NP-complete. Your version checks all 2^n rows,
     * which is fine for the handful of variables in this lab and hopeless for
     * thousands. Real SAT solvers are far smarter, and they run inside chip
     * verification tools and program analyzers every day.
     */
    public boolean isSatisfiable(Expression formula) {
        throw new UnsupportedOperationException("TODO 7: implement isSatisfiable(formula)");
    }

    /**
     * Are these two formulas logically equivalent — do they agree on every assignment?
     *
     * <p>TODO 8: replace the exception below with a working implementation.
     *
     * <p>Example: {@code ¬(p ∧ q)} and {@code ¬p ∨ ¬q} are equivalent. That is
     * De Morgan's law, and your finished code will confirm it.
     *
     * <p>CAREFUL: the two formulas may not use the same variables. {@code p} and
     * {@code p ∧ q} must be compared across assignments to <em>both</em> p and q. Use
     * {@code tables.assignments(left, right)}, which collects the variables from both
     * formulas, rather than taking the variables from just one of them.
     *
     * <p>APPROACH: for each assignment, evaluate both formulas. If the two results ever
     * differ, the formulas are not equivalent. If they agree on every row, they are.
     */
    public boolean areEquivalent(Expression left, Expression right) {
        throw new UnsupportedOperationException("TODO 8: implement areEquivalent(left, right)");
    }

    // -------------------------------------------------------------------------
    // Provided below this line — no changes needed.
    // -------------------------------------------------------------------------

    /** Returns "tautology", "contradiction", or "contingency". Used by the web page. */
    public String classify(Expression formula) {
        if (isTautology(formula)) {
            return "tautology";
        }
        if (isContradiction(formula)) {
            return "contradiction";
        }
        return "contingency";
    }

    /**
     * Finds the assignments where two formulas disagree, so the web page can show a
     * student exactly which row broke a claimed equivalence.
     */
    public List<Map<String, Boolean>> counterexamples(Expression left, Expression right) {
        List<Map<String, Boolean>> disagreements = new ArrayList<>();
        for (Map<String, Boolean> assignment : tables.assignments(left, right)) {
            if (Expression.evaluate(left, assignment) != Expression.evaluate(right, assignment)) {
                disagreements.add(assignment);
            }
        }
        return disagreements;
    }
}
