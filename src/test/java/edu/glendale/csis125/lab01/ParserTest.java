package edu.glendale.csis125.lab01;

import edu.glendale.csis125.lab01.logic.Expression;
import edu.glendale.csis125.lab01.logic.ParseException;
import edu.glendale.csis125.lab01.logic.Parser;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Checks the parser, which is provided code.
 *
 * <p>These tests should pass the moment you open the repository, before you have
 * written anything. If they fail on a fresh clone, something is wrong with your
 * environment rather than with your work — tell your instructor.
 */
@DisplayName("The parser (provided code — these should already pass)")
class ParserTest {

    private static String round(String formula) {
        return Expression.render(Parser.parse(formula));
    }

    @Test
    @DisplayName("∧ binds tighter than ∨")
    void conjunctionBindsTighter() {
        assertEquals("p ∨ (q ∧ r)", round("p | q & r"));
    }

    @Test
    @DisplayName("∨ binds tighter than →")
    void disjunctionBindsTighterThanImplication() {
        assertEquals("(p ∨ q) → r", round("p | q -> r"));
    }

    @Test
    @DisplayName("→ binds tighter than ↔")
    void implicationBindsTighterThanBiconditional() {
        assertEquals("(p → q) ↔ r", round("p -> q <-> r"));
    }

    @Test
    @DisplayName("→ groups to the right")
    void implicationIsRightAssociative() {
        assertEquals("p → (q → r)", round("p -> q -> r"));
    }

    @Test
    @DisplayName("∧ groups to the left")
    void conjunctionIsLeftAssociative() {
        assertEquals("(p ∧ q) ∧ r", round("p & q & r"));
    }

    @Test
    @DisplayName("¬ applies only to what follows it")
    void negationBindsTightest() {
        assertEquals("¬p ∨ q", round("~p | q"));
    }

    @Test
    @DisplayName("parentheses override binding order")
    void parenthesesWin() {
        assertEquals("¬(p ∨ q)", round("~(p | q)"));
    }

    @Test
    @DisplayName("word forms work: 'p and q' is the same as 'p & q'")
    void wordForms() {
        assertEquals(round("p & q"), round("p and q"));
        assertEquals(round("p -> q"), round("p implies q"));
        assertEquals(round("~p"), round("not p"));
    }

    @Test
    @DisplayName("logic symbols can be pasted straight from the textbook")
    void unicodeSymbols() {
        assertEquals(round("p -> q"), round("p → q"));
        assertEquals(round("~p & q"), round("¬p ∧ q"));
        assertEquals(round("p <-> q"), round("p ↔ q"));
    }

    @Test
    @DisplayName("variables may be words, not just single letters")
    void multiLetterVariables() {
        assertEquals("raining → wet", round("raining -> wet"));
    }

    @Test
    @DisplayName("an unclosed parenthesis is reported, not ignored")
    void unclosedParenthesis() {
        ParseException thrown = assertThrows(ParseException.class, () -> Parser.parse("(p & q"));
        assertEquals(true, thrown.getMessage().contains("closing parenthesis"));
    }

    @Test
    @DisplayName("a dangling operator is reported")
    void danglingOperator() {
        assertThrows(ParseException.class, () -> Parser.parse("p &"));
    }

    @Test
    @DisplayName("an empty formula is reported")
    void emptyInput() {
        assertThrows(ParseException.class, () -> Parser.parse("   "));
    }
}
