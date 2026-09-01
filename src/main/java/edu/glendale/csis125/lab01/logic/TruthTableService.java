package edu.glendale.csis125.lab01.logic;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

/**
 * Builds truth tables and enumerates truth-value assignments.
 *
 * <p>YOU DO NOT NEED TO EDIT THIS FILE, but you will call {@link #assignments}
 * from {@link LogicAnalyzer}, so read that method's documentation.
 *
 * <p>The {@code @Service} annotation tells Spring to create one instance of this
 * class at startup and hand it to anything that needs one. That is why
 * {@link LogicAnalyzer} can simply ask for a TruthTableService in its constructor
 * without ever calling {@code new}.
 */
@Service
public class TruthTableService {

    /** A formula with more variables than this would produce an unreadable table. */
    public static final int MAX_VARIABLES = 10;

    /**
     * Lists every possible assignment of truth values to the given variables.
     *
     * <p>With n variables there are 2^n assignments, which is exactly the number of
     * rows in a truth table. The order matches the convention used in most textbooks:
     * the first variable changes slowest, all-true comes first, all-false comes last.
     *
     * <pre>
     *   assignments(List.of("p", "q")) returns, in order:
     *     {p=true,  q=true}
     *     {p=true,  q=false}
     *     {p=false, q=true}
     *     {p=false, q=false}
     * </pre>
     *
     * @param variables the variable names, in the order they should appear as columns
     * @return all 2^n assignments
     */
    public List<Map<String, Boolean>> assignments(List<String> variables) {
        int variableCount = variables.size();
        if (variableCount > MAX_VARIABLES) {
            throw new IllegalArgumentException(
                    "That formula has " + variableCount + " variables, which needs "
                            + (1L << variableCount) + " rows. The limit is " + MAX_VARIABLES + ".");
        }

        int rowCount = 1 << variableCount;
        List<Map<String, Boolean>> result = new ArrayList<>(rowCount);

        for (int row = 0; row < rowCount; row++) {
            Map<String, Boolean> assignment = new LinkedHashMap<>();
            for (int column = 0; column < variableCount; column++) {
                // Read the bits of the row index from the left so that the leftmost
                // column alternates slowest, as it does in a printed truth table.
                int bit = (row >> (variableCount - 1 - column)) & 1;
                assignment.put(variables.get(column), bit == 0);
            }
            result.add(assignment);
        }

        return result;
    }

    /** Convenience overload that reads the variables straight off the formulas. */
    public List<Map<String, Boolean>> assignments(Expression... expressions) {
        Set<String> names = Expression.variablesOf(expressions);
        return assignments(new ArrayList<>(names));
    }

    /**
     * Builds the complete truth table for one formula.
     *
     * @param expression the formula
     * @param classification how the formula was classified, shown as a caption
     */
    public TruthTable build(Expression expression, String classification) {
        List<String> variables = new ArrayList<>(Expression.variablesOf(expression));
        List<TruthTable.Row> rows = new ArrayList<>();

        for (Map<String, Boolean> assignment : assignments(variables)) {
            List<Boolean> values = variables.stream().map(assignment::get).toList();
            rows.add(new TruthTable.Row(values, Expression.evaluate(expression, assignment)));
        }

        return new TruthTable(Expression.render(expression), variables, rows, classification);
    }
}
