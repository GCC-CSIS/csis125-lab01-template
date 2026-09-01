package edu.glendale.csis125.lab01;

import edu.glendale.csis125.lab01.logic.Expression;
import edu.glendale.csis125.lab01.logic.LogicAnalyzer;
import edu.glendale.csis125.lab01.logic.Parser;
import edu.glendale.csis125.lab01.logic.TruthTableService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Checks classification and equivalence against laws from the textbook.
 *
 * <p>These tests are the grade for Part 2. Each equivalence test names the law it
 * is checking, so a failure tells you which identity your code got wrong.
 */
@DisplayName("Part 2: classifying formulas and proving equivalences")
class LogicAnalyzerTest {

    private final LogicAnalyzer analyzer = new LogicAnalyzer(new TruthTableService());

    private static Expression parse(String formula) {
        return Parser.parse(formula);
    }

    @Nested
    @DisplayName("TODO 5 — tautologies")
    class Tautologies {

        @Test
        @DisplayName("p ∨ ¬p is a tautology (law of the excluded middle)")
        void excludedMiddle() {
            assertTrue(analyzer.isTautology(parse("p | ~p")));
        }

        @Test
        @DisplayName("p → p is a tautology")
        void selfImplication() {
            assertTrue(analyzer.isTautology(parse("p -> p")));
        }

        @Test
        @DisplayName("(p ∧ (p → q)) → q is a tautology (modus ponens)")
        void modusPonens() {
            assertTrue(analyzer.isTautology(parse("(p & (p -> q)) -> q")),
                    "Modus ponens is valid, so this formula holds under every assignment.");
        }

        @Test
        @DisplayName("p ∧ q is not a tautology")
        void conjunctionIsNotATautology() {
            assertFalse(analyzer.isTautology(parse("p & q")),
                    "This is false when either operand is false, so it cannot be a tautology. "
                            + "If this passed, check that you return false as soon as you find "
                            + "one row that evaluates to false.");
        }

        @Test
        @DisplayName("p ∧ ¬p is not a tautology")
        void contradictionIsNotATautology() {
            assertFalse(analyzer.isTautology(parse("p & ~p")));
        }
    }

    @Nested
    @DisplayName("TODO 6 — contradictions")
    class Contradictions {

        @Test
        @DisplayName("p ∧ ¬p is a contradiction")
        void basicContradiction() {
            assertTrue(analyzer.isContradiction(parse("p & ~p")));
        }

        @Test
        @DisplayName("(p ↔ q) ∧ (p ⊕ q) is a contradiction")
        void sameAndDifferentAtOnce() {
            assertTrue(analyzer.isContradiction(parse("(p <-> q) & (p ^ q)")),
                    "Two formulas cannot agree and disagree at the same time.");
        }

        @Test
        @DisplayName("p ∨ ¬p is not a contradiction")
        void tautologyIsNotAContradiction() {
            assertFalse(analyzer.isContradiction(parse("p | ~p")));
        }

        @Test
        @DisplayName("p ∧ q is not a contradiction")
        void contingencyIsNotAContradiction() {
            assertFalse(analyzer.isContradiction(parse("p & q")),
                    "This is true when both operands are true, so it is not a contradiction.");
        }
    }

    @Nested
    @DisplayName("TODO 7 — satisfiability")
    class Satisfiability {

        @Test
        @DisplayName("p ∧ q is satisfiable")
        void conjunctionIsSatisfiable() {
            assertTrue(analyzer.isSatisfiable(parse("p & q")),
                    "Setting both to true satisfies it. Being satisfiable is much weaker than "
                            + "being a tautology.");
        }

        @Test
        @DisplayName("p ∧ ¬p is not satisfiable")
        void contradictionIsNotSatisfiable() {
            assertFalse(analyzer.isSatisfiable(parse("p & ~p")));
        }

        @Test
        @DisplayName("every tautology is satisfiable")
        void tautologyIsSatisfiable() {
            assertTrue(analyzer.isSatisfiable(parse("p | ~p")));
        }

        @Test
        @DisplayName("(p ∨ q) ∧ (¬p ∨ r) ∧ (¬q ∨ ¬r) is satisfiable")
        void threeClauseFormula() {
            assertTrue(analyzer.isSatisfiable(parse("(p | q) & (~p | r) & (~q | ~r)")),
                    "This is the shape a SAT solver works on. There is at least one assignment "
                            + "that satisfies all three clauses at once.");
        }
    }

    @Nested
    @DisplayName("TODO 8 — logical equivalence")
    class Equivalences {

        @Test
        @DisplayName("De Morgan: ¬(p ∧ q) ≡ ¬p ∨ ¬q")
        void deMorganOverConjunction() {
            assertTrue(analyzer.areEquivalent(parse("~(p & q)"), parse("~p | ~q")));
        }

        @Test
        @DisplayName("De Morgan: ¬(p ∨ q) ≡ ¬p ∧ ¬q")
        void deMorganOverDisjunction() {
            assertTrue(analyzer.areEquivalent(parse("~(p | q)"), parse("~p & ~q")));
        }

        @Test
        @DisplayName("Contrapositive: p → q ≡ ¬q → ¬p")
        void contrapositive() {
            assertTrue(analyzer.areEquivalent(parse("p -> q"), parse("~q -> ~p")),
                    "A conditional and its contrapositive are always equivalent. This is why "
                            + "proof by contraposition works.");
        }

        @Test
        @DisplayName("Implication as disjunction: p → q ≡ ¬p ∨ q")
        void implicationAsDisjunction() {
            assertTrue(analyzer.areEquivalent(parse("p -> q"), parse("~p | q")));
        }

        @Test
        @DisplayName("Double negation: ¬¬p ≡ p")
        void doubleNegation() {
            assertTrue(analyzer.areEquivalent(parse("~~p"), parse("p")));
        }

        @Test
        @DisplayName("Distributive: p ∧ (q ∨ r) ≡ (p ∧ q) ∨ (p ∧ r)")
        void distributive() {
            assertTrue(analyzer.areEquivalent(parse("p & (q | r)"), parse("(p & q) | (p & r)")));
        }

        @Test
        @DisplayName("The converse is NOT equivalent: p → q is not ≡ q → p")
        void converseIsNotEquivalent() {
            assertFalse(analyzer.areEquivalent(parse("p -> q"), parse("q -> p")),
                    "Confusing a conditional with its converse is the most common mistake in "
                            + "this chapter. They disagree on two of the four rows.");
        }

        @Test
        @DisplayName("Formulas over different variables are compared over both")
        void differentVariableSets() {
            assertFalse(analyzer.areEquivalent(parse("p"), parse("p & q")),
                    "If this passed, you probably took the variables from only one formula. "
                            + "Use tables.assignments(left, right) so that q is included.");
        }

        @Test
        @DisplayName("A formula is equivalent to itself")
        void reflexive() {
            assertTrue(analyzer.areEquivalent(parse("p -> (q & r)"), parse("p -> (q & r)")));
        }
    }

    @Nested
    @DisplayName("Classification used by the web page")
    class Classification {

        @Test
        @DisplayName("p ∨ ¬p is reported as a tautology")
        void tautology() {
            assertEquals("tautology", analyzer.classify(parse("p | ~p")));
        }

        @Test
        @DisplayName("p ∧ ¬p is reported as a contradiction")
        void contradiction() {
            assertEquals("contradiction", analyzer.classify(parse("p & ~p")));
        }

        @Test
        @DisplayName("p → q is reported as a contingency")
        void contingency() {
            assertEquals("contingency", analyzer.classify(parse("p -> q")));
        }

        @Test
        @DisplayName("counterexamples finds the two rows where p → q and q → p disagree")
        void counterexamplesForConverse() {
            assertEquals(2, analyzer.counterexamples(parse("p -> q"), parse("q -> p")).size());
        }
    }
}
