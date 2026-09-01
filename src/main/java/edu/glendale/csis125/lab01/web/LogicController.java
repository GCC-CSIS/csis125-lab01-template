package edu.glendale.csis125.lab01.web;

import java.util.List;
import java.util.Map;

import edu.glendale.csis125.lab01.logic.Expression;
import edu.glendale.csis125.lab01.logic.LogicAnalyzer;
import edu.glendale.csis125.lab01.logic.Parser;
import edu.glendale.csis125.lab01.logic.TruthTable;
import edu.glendale.csis125.lab01.logic.TruthTableService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The HTTP layer. Provided — no changes needed.
 *
 * <p>Each method below becomes a URL. The annotations do the work: {@code @GetMapping}
 * says which path and HTTP method to answer, {@code @RequestParam} pulls values out of
 * the query string, and the returned object is converted to JSON automatically.
 *
 * <p>Try them directly in the browser once your code compiles:
 * <ul>
 *   <li>/api/logic/truth-table?formula=p -&gt; q</li>
 *   <li>/api/logic/classify?formula=p | ~p</li>
 *   <li>/api/logic/equivalent?left=~(p %26 q)&amp;right=~p | ~q</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/logic")
public class LogicController {

    private final TruthTableService tables;
    private final LogicAnalyzer analyzer;

    public LogicController(TruthTableService tables, LogicAnalyzer analyzer) {
        this.tables = tables;
        this.analyzer = analyzer;
    }

    /** Full truth table for one formula. */
    @GetMapping("/truth-table")
    public TruthTable truthTable(@RequestParam String formula) {
        Expression expression = Parser.parse(formula);
        return tables.build(expression, analyzer.classify(expression));
    }

    /** Tautology, contradiction, or contingency — plus satisfiability. */
    @GetMapping("/classify")
    public Map<String, Object> classify(@RequestParam String formula) {
        Expression expression = Parser.parse(formula);
        return Map.of(
                "formula", Expression.render(expression),
                "classification", analyzer.classify(expression),
                "satisfiable", analyzer.isSatisfiable(expression));
    }

    /** Are two formulas logically equivalent? If not, which rows disagree? */
    @GetMapping("/equivalent")
    public Map<String, Object> equivalent(@RequestParam String left, @RequestParam String right) {
        Expression leftExpression = Parser.parse(left);
        Expression rightExpression = Parser.parse(right);

        boolean equivalent = analyzer.areEquivalent(leftExpression, rightExpression);
        List<Map<String, Boolean>> disagreements =
                equivalent ? List.of() : analyzer.counterexamples(leftExpression, rightExpression);

        return Map.of(
                "left", Expression.render(leftExpression),
                "right", Expression.render(rightExpression),
                "equivalent", equivalent,
                "counterexamples", disagreements);
    }
}
