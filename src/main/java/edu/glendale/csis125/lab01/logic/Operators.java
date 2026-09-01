package edu.glendale.csis125.lab01.logic;

/**
 * The five truth-functional connectives of propositional logic.
 *
 * <p>=== PART 1 OF YOUR LAB. Four methods to write. ===
 *
 * <p>Each method takes the truth values of the operands and returns the truth
 * value of the compound proposition. Two are done for you as worked examples.
 *
 * <p>A rule for this lab: express each connective using only {@code &&}, {@code ||},
 * and {@code !}, or by calling the methods above it in this file. Do not use
 * {@code ==} or {@code !=} on booleans as a shortcut — the point is to write down
 * the definition you would give on paper, not the shortest Java that passes.
 */
public final class Operators {

    private Operators() {
        // Utility class: never instantiated.
    }

    /**
     * Negation, ¬p. True exactly when p is false.
     *
     * <p>WORKED EXAMPLE — nothing to do here.
     */
    public static boolean not(boolean p) {
        return !p;
    }

    /**
     * Conjunction, p ∧ q. True exactly when both operands are true.
     *
     * <p>WORKED EXAMPLE — nothing to do here.
     *
     * <pre>
     *   p | q | p ∧ q
     *   --+---+------
     *   T | T |   T
     *   T | F |   F
     *   F | T |   F
     *   F | F |   F
     * </pre>
     */
    public static boolean and(boolean p, boolean q) {
        return p && q;
    }

    /**
     * Disjunction, p ∨ q. True when at least one operand is true.
     *
     * <p>This is <em>inclusive</em> or: p ∨ q is true when both are true.
     * That is the one place where the logician's "or" differs from the way
     * "or" is often used in English.
     *
     * <p>TODO 1: replace the exception below with the correct return statement.
     *
     * <pre>
     *   p | q | p ∨ q
     *   --+---+------
     *   T | T |   T
     *   T | F |   T
     *   F | T |   T
     *   F | F |   F
     * </pre>
     */
    public static boolean or(boolean p, boolean q) {
        throw new UnsupportedOperationException("TODO 1: implement or(p, q)");
    }

    /**
     * Implication, p → q. Read "if p then q".
     *
     * <p>TODO 2: replace the exception below with the correct return statement.
     *
     * <p>This is the one students find strange, so read the table carefully.
     * An implication makes a promise: "if p happens, q will follow." The promise
     * is broken only in one situation — p happened and q did not. In every other
     * row nothing was broken, so the implication is true. In particular, when p
     * is false the implication is true no matter what q is. Logicians call that
     * <em>vacuous truth</em>.
     *
     * <p>HINT: there is a one-line way to say "it is not the case that p is true
     * while q is false" using only the operators above.
     *
     * <pre>
     *   p | q | p → q
     *   --+---+------
     *   T | T |   T
     *   T | F |   F     &lt;-- the only false row
     *   F | T |   T
     *   F | F |   T
     * </pre>
     */
    public static boolean implies(boolean p, boolean q) {
        throw new UnsupportedOperationException("TODO 2: implement implies(p, q)");
    }

    /**
     * Biconditional, p ↔ q. Read "p if and only if q".
     *
     * <p>TODO 3: replace the exception below with the correct return statement.
     *
     * <p>True exactly when both operands have the same truth value. The name is a
     * hint about how to build it: a biconditional is an implication going both
     * ways at once. Try writing it as a conjunction of two calls to
     * {@link #implies}, which is literally what "if and only if" abbreviates.
     *
     * <pre>
     *   p | q | p ↔ q
     *   --+---+------
     *   T | T |   T
     *   T | F |   F
     *   F | T |   F
     *   F | F |   T
     * </pre>
     */
    public static boolean iff(boolean p, boolean q) {
        throw new UnsupportedOperationException("TODO 3: implement iff(p, q)");
    }

    /**
     * Exclusive or, p ⊕ q. True when the operands differ.
     *
     * <p>TODO 4: replace the exception below with the correct return statement.
     *
     * <p>This is the "or" of "soup or salad" — one, but not both. Note that it is
     * the exact opposite of {@link #iff}, which is worth thinking about for a
     * moment before you write anything: if you already have iff working, this
     * method is very short.
     *
     * <pre>
     *   p | q | p ⊕ q
     *   --+---+------
     *   T | T |   F
     *   T | F |   T
     *   F | T |   T
     *   F | F |   F
     * </pre>
     */
    public static boolean xor(boolean p, boolean q) {
        throw new UnsupportedOperationException("TODO 4: implement xor(p, q)");
    }
}
