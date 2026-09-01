package edu.glendale.csis125.lab01;

import edu.glendale.csis125.lab01.logic.Operators;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Checks each connective against its truth table, one row at a time.
 *
 * <p>These tests are the grade for Part 1. Run them with: ./mvnw test
 */
@DisplayName("Part 1: the truth-functional connectives")
class OperatorsTest {

    @Nested
    @DisplayName("TODO 1 — disjunction, p ∨ q")
    class Disjunction {

        @Test
        @DisplayName("T ∨ T is true")
        void trueOrTrue() {
            assertTrue(Operators.or(true, true),
                    "Inclusive or is true when both operands are true. This is the row where "
                            + "the logician's 'or' differs from everyday English.");
        }

        @Test
        @DisplayName("T ∨ F is true")
        void trueOrFalse() {
            assertTrue(Operators.or(true, false));
        }

        @Test
        @DisplayName("F ∨ T is true")
        void falseOrTrue() {
            assertTrue(Operators.or(false, true));
        }

        @Test
        @DisplayName("F ∨ F is false")
        void falseOrFalse() {
            assertFalse(Operators.or(false, false),
                    "A disjunction is false only when both operands are false.");
        }
    }

    @Nested
    @DisplayName("TODO 2 — implication, p → q")
    class Implication {

        @Test
        @DisplayName("T → T is true")
        void trueImpliesTrue() {
            assertTrue(Operators.implies(true, true));
        }

        @Test
        @DisplayName("T → F is false — the only false row")
        void trueImpliesFalse() {
            assertFalse(Operators.implies(true, false),
                    "This is the one row where an implication fails: the hypothesis held "
                            + "and the conclusion did not follow.");
        }

        @Test
        @DisplayName("F → T is true (vacuously)")
        void falseImpliesTrue() {
            assertTrue(Operators.implies(false, true),
                    "When the hypothesis is false, the implication is true. Nothing was promised, "
                            + "so nothing was broken.");
        }

        @Test
        @DisplayName("F → F is true (vacuously)")
        void falseImpliesFalse() {
            assertTrue(Operators.implies(false, false),
                    "Also vacuously true. If you returned false here, you may have implemented "
                            + "'and' or 'iff' by mistake.");
        }
    }

    @Nested
    @DisplayName("TODO 3 — biconditional, p ↔ q")
    class Biconditional {

        @Test
        @DisplayName("T ↔ T is true")
        void trueIffTrue() {
            assertTrue(Operators.iff(true, true));
        }

        @Test
        @DisplayName("T ↔ F is false")
        void trueIffFalse() {
            assertFalse(Operators.iff(true, false));
        }

        @Test
        @DisplayName("F ↔ T is false")
        void falseIffTrue() {
            assertFalse(Operators.iff(false, true));
        }

        @Test
        @DisplayName("F ↔ F is true")
        void falseIffFalse() {
            assertTrue(Operators.iff(false, false),
                    "Two false statements still have the same truth value, so the biconditional holds.");
        }
    }

    @Nested
    @DisplayName("TODO 4 — exclusive or, p ⊕ q")
    class ExclusiveOr {

        @Test
        @DisplayName("T ⊕ T is false")
        void trueXorTrue() {
            assertFalse(Operators.xor(true, true),
                    "Exclusive or means one or the other, but not both. If this returned true, "
                            + "you may have implemented inclusive or.");
        }

        @Test
        @DisplayName("T ⊕ F is true")
        void trueXorFalse() {
            assertTrue(Operators.xor(true, false));
        }

        @Test
        @DisplayName("F ⊕ T is true")
        void falseXorTrue() {
            assertTrue(Operators.xor(false, true));
        }

        @Test
        @DisplayName("F ⊕ F is false")
        void falseXorFalse() {
            assertFalse(Operators.xor(false, false));
        }
    }

    @Nested
    @DisplayName("Relationships between the connectives")
    class Relationships {

        @Test
        @DisplayName("⊕ is the negation of ↔ on every row")
        void xorIsNegationOfIff() {
            for (boolean p : new boolean[] {true, false}) {
                for (boolean q : new boolean[] {true, false}) {
                    assertTrue(Operators.xor(p, q) != Operators.iff(p, q),
                            "xor and iff disagreed nowhere at p=" + p + ", q=" + q
                                    + ". They should be opposites on every row.");
                }
            }
        }

        @Test
        @DisplayName("p → q agrees with ¬p ∨ q on every row")
        void implicationMatchesDisjunctiveForm() {
            for (boolean p : new boolean[] {true, false}) {
                for (boolean q : new boolean[] {true, false}) {
                    assertTrue(Operators.implies(p, q) == Operators.or(Operators.not(p), q),
                            "implies(" + p + ", " + q + ") should equal or(not(" + p + "), " + q + "). "
                                    + "This identity is how implication is usually rewritten.");
                }
            }
        }

        @Test
        @DisplayName("p ↔ q agrees with (p → q) ∧ (q → p) on every row")
        void biconditionalIsTwoImplications() {
            for (boolean p : new boolean[] {true, false}) {
                for (boolean q : new boolean[] {true, false}) {
                    boolean bothWays = Operators.and(Operators.implies(p, q), Operators.implies(q, p));
                    assertTrue(Operators.iff(p, q) == bothWays,
                            "iff(" + p + ", " + q + ") should equal implies both ways. "
                                    + "That is what 'if and only if' abbreviates.");
                }
            }
        }
    }
}
