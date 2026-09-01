package edu.glendale.csis125.lab01.logic;

import java.util.List;

/**
 * A completed truth table, ready to be sent to the browser as JSON.
 *
 * @param formula     the formula rendered with logic symbols
 * @param variables   the variable names, alphabetically, matching the order of each row's values
 * @param rows        one row per assignment of truth values
 * @param classification "tautology", "contradiction", or "contingency"
 */
public record TruthTable(
        String formula,
        List<String> variables,
        List<Row> rows,
        String classification) {

    /**
     * One line of the table.
     *
     * @param values the truth values of the variables, in the same order as {@code variables}
     * @param result the truth value of the whole formula under that assignment
     */
    public record Row(List<Boolean> values, boolean result) {}
}
