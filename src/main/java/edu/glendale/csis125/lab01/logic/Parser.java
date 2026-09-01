package edu.glendale.csis125.lab01.logic;

import java.util.ArrayList;
import java.util.List;

/**
 * Turns text like {@code "p -> (q & r)"} into an {@link Expression} tree.
 *
 * <p>YOU DO NOT NEED TO EDIT THIS FILE — it is provided so that you can spend the
 * lab on the logic rather than on string handling. You are welcome to read it.
 * It is a hand-written recursive-descent parser, which is the standard way to
 * parse a small grammar, and each method below corresponds to one line of the
 * grammar in the README.
 *
 * <p>Binding tightness, loosest first: ↔, then →, then ⊕, then ∨, then ∧, then ¬.
 * So {@code p | q -> r} parses as {@code (p | q) -> r}, and → groups to the right,
 * so {@code p -> q -> r} parses as {@code p -> (q -> r)}.
 */
public final class Parser {

    private final List<Token> tokens;
    private int position;

    private Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.position = 0;
    }

    /**
     * Parses a propositional formula.
     *
     * @throws ParseException if the text is not a well-formed formula
     */
    public static Expression parse(String input) {
        if (input == null || input.isBlank()) {
            throw new ParseException("Enter a formula, for example: p -> q");
        }
        Parser parser = new Parser(tokenize(input));
        Expression expression = parser.parseIff();
        parser.expect(Kind.END, "end of formula");
        return expression;
    }

    // ---------------------------------------------------------------------
    // Grammar
    // ---------------------------------------------------------------------

    private Expression parseIff() {
        Expression left = parseImplies();
        while (peek() == Kind.IFF) {
            advance();
            left = new Expression.Iff(left, parseImplies());
        }
        return left;
    }

    private Expression parseImplies() {
        Expression left = parseXor();
        if (peek() == Kind.IMPLIES) {
            advance();
            // Right-associative: recurse rather than loop.
            return new Expression.Implies(left, parseImplies());
        }
        return left;
    }

    private Expression parseXor() {
        Expression left = parseOr();
        while (peek() == Kind.XOR) {
            advance();
            left = new Expression.Xor(left, parseOr());
        }
        return left;
    }

    private Expression parseOr() {
        Expression left = parseAnd();
        while (peek() == Kind.OR) {
            advance();
            left = new Expression.Or(left, parseAnd());
        }
        return left;
    }

    private Expression parseAnd() {
        Expression left = parseUnary();
        while (peek() == Kind.AND) {
            advance();
            left = new Expression.And(left, parseUnary());
        }
        return left;
    }

    private Expression parseUnary() {
        if (peek() == Kind.NOT) {
            advance();
            return new Expression.Not(parseUnary());
        }
        return parseAtom();
    }

    private Expression parseAtom() {
        Token token = tokens.get(position);
        switch (token.kind()) {
            case VARIABLE -> {
                advance();
                return new Expression.Var(token.text());
            }
            case LEFT_PAREN -> {
                advance();
                Expression inner = parseIff();
                expect(Kind.RIGHT_PAREN, "a closing parenthesis");
                return inner;
            }
            default -> throw new ParseException(
                    "Expected a variable or '(' but found " + describe(token) + " at position " + token.start());
        }
    }

    // ---------------------------------------------------------------------
    // Token plumbing
    // ---------------------------------------------------------------------

    private Kind peek() {
        return tokens.get(position).kind();
    }

    private void advance() {
        if (position < tokens.size() - 1) {
            position++;
        }
    }

    private void expect(Kind kind, String description) {
        Token token = tokens.get(position);
        if (token.kind() != kind) {
            throw new ParseException(
                    "Expected " + description + " but found " + describe(token) + " at position " + token.start());
        }
        advance();
    }

    private static String describe(Token token) {
        return token.kind() == Kind.END ? "the end of the formula" : "'" + token.text() + "'";
    }

    private enum Kind {
        VARIABLE, NOT, AND, OR, XOR, IMPLIES, IFF, LEFT_PAREN, RIGHT_PAREN, END
    }

    private record Token(Kind kind, String text, int start) {}

    // ---------------------------------------------------------------------
    // Tokenizer
    // ---------------------------------------------------------------------

    private static List<Token> tokenize(String input) {
        List<Token> tokens = new ArrayList<>();
        int i = 0;
        int n = input.length();

        while (i < n) {
            char c = input.charAt(i);

            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }

            // Multi-character symbolic operators first, longest match wins.
            if (input.startsWith("<->", i) || input.startsWith("<=>", i)) {
                tokens.add(new Token(Kind.IFF, input.substring(i, i + 3), i));
                i += 3;
            } else if (input.startsWith("->", i) || input.startsWith("=>", i)) {
                tokens.add(new Token(Kind.IMPLIES, input.substring(i, i + 2), i));
                i += 2;
            } else if (input.startsWith("/\\", i)) {
                tokens.add(new Token(Kind.AND, "/\\", i));
                i += 2;
            } else if (input.startsWith("\\/", i)) {
                tokens.add(new Token(Kind.OR, "\\/", i));
                i += 2;
            } else if (input.startsWith("&&", i)) {
                tokens.add(new Token(Kind.AND, "&&", i));
                i += 2;
            } else if (input.startsWith("||", i)) {
                tokens.add(new Token(Kind.OR, "||", i));
                i += 2;
            } else if (c == '¬' || c == '~' || c == '!') {
                tokens.add(new Token(Kind.NOT, String.valueOf(c), i));
                i++;
            } else if (c == '∧' || c == '&') {
                tokens.add(new Token(Kind.AND, String.valueOf(c), i));
                i++;
            } else if (c == '∨' || c == '|') {
                tokens.add(new Token(Kind.OR, String.valueOf(c), i));
                i++;
            } else if (c == '⊕' || c == '^') {
                tokens.add(new Token(Kind.XOR, String.valueOf(c), i));
                i++;
            } else if (c == '→' || c == '⊃') {
                tokens.add(new Token(Kind.IMPLIES, String.valueOf(c), i));
                i++;
            } else if (c == '↔' || c == '≡') {
                tokens.add(new Token(Kind.IFF, String.valueOf(c), i));
                i++;
            } else if (c == '(') {
                tokens.add(new Token(Kind.LEFT_PAREN, "(", i));
                i++;
            } else if (c == ')') {
                tokens.add(new Token(Kind.RIGHT_PAREN, ")", i));
                i++;
            } else if (Character.isLetter(c)) {
                int start = i;
                while (i < n && (Character.isLetterOrDigit(input.charAt(i)) || input.charAt(i) == '_')) {
                    i++;
                }
                String word = input.substring(start, i);
                tokens.add(new Token(keywordKind(word), word, start));
            } else {
                throw new ParseException("Unexpected character '" + c + "' at position " + i);
            }
        }

        tokens.add(new Token(Kind.END, "", n));
        return tokens;
    }

    /** Word forms of the operators, so students can type "p and q" if they prefer. */
    private static Kind keywordKind(String word) {
        return switch (word.toLowerCase()) {
            case "not" -> Kind.NOT;
            case "and" -> Kind.AND;
            case "or" -> Kind.OR;
            case "xor" -> Kind.XOR;
            case "implies" -> Kind.IMPLIES;
            case "iff" -> Kind.IFF;
            default -> Kind.VARIABLE;
        };
    }
}
