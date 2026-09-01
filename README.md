# Lab 1 — Propositional Logic

**CS/IS 125, Discrete Structures — Glendale Community College**

In this lab you will implement the logical connectives and the definitions of tautology,
contradiction, satisfiability, and logical equivalence. When you are finished, you will
have a working web application that builds truth tables for any formula you type, and
that can check whether two formulas are equivalent.

You write eight short methods. Everything else — the parser, the truth-table generator,
the web layer, the page — is already written for you.

---

## Getting started

You do not need to install anything on your own computer.

1. On the repository page, click **Use this template → Create a new repository**.
   Name it `lab01-yourlastname` and keep it private.
2. In your new repository, click the green **Code** button, choose the **Codespaces**
   tab, and click **Create codespace on main**.
3. Wait. The first build takes two to four minutes because it is downloading Java,
   Maven, and the project's dependencies. After that, opening it again takes seconds.

When the editor loads, you have a full Linux machine with Java 21 and Maven already
installed. Nothing to configure.

### Check that it works before you start

In the terminal at the bottom of the editor, run:

```bash
mvn test
```

You should see failures in `OperatorsTest` and `LogicAnalyzerTest` — that is expected,
because you have not written those methods yet. But `ParserTest` should pass completely.
If `ParserTest` fails on a fresh codespace, something is wrong with the environment
rather than with your work. Post on the discussion board or come to office hours.

---

## What you are writing

### Part 1 — `src/main/java/edu/glendale/csis125/lab01/logic/Operators.java`

Four methods, one line each. Each one takes truth values and returns a truth value,
exactly as the truth table in the comment specifies.

| | Method | Symbol |
|---|---|---|
| TODO 1 | `or` | p ∨ q |
| TODO 2 | `implies` | p → q |
| TODO 3 | `iff` | p ↔ q |
| TODO 4 | `xor` | p ⊕ q |

`not` and `and` are already written as worked examples. Read them first.

Write each connective using only `&&`, `||`, `!`, or calls to the methods above it in
the file. Do not use `==` or `!=` on booleans to shortcut your way to a passing test —
the point is to write down the definition you would give on paper.

### Part 2 — `src/main/java/edu/glendale/csis125/lab01/logic/LogicAnalyzer.java`

Four methods, a few lines each. All four work the same way: loop over every row of the
truth table and look at the results.

| | Method | Question it answers |
|---|---|---|
| TODO 5 | `isTautology` | Is it true in every row? |
| TODO 6 | `isContradiction` | Is it false in every row? |
| TODO 7 | `isSatisfiable` | Is it true in at least one row? |
| TODO 8 | `areEquivalent` | Do two formulas agree in every row? |

The two tools you need are already provided:

- `tables.assignments(formula)` gives you the list of all 2ⁿ assignments of truth
  values to the variables — one per row of the table.
- `Expression.evaluate(formula, assignment)` gives you the truth value of the formula
  under one of those assignments.

A loop over the first, calling the second, is the shape of all four answers.

---

## Running the application

Once Part 1 compiles, start the server:

```bash
mvn spring-boot:run
```

Codespaces will pop up a notification offering to open port 8080 in your browser.
Click it. You will get the truth-table page. If you dismiss the notification, the
**Ports** tab next to the terminal has the link.

Type a formula and press Build the table. Until you finish Part 2, the page will tell
you which TODO it hit rather than showing a table — that is the exception handler
doing its job, and it is a useful way to see your progress.

Stop the server with `Ctrl+C`.

### The API underneath

The page is just HTML calling three endpoints. You can hit them directly:

- `/api/logic/truth-table?formula=p -> q`
- `/api/logic/classify?formula=p | ~p`
- `/api/logic/equivalent?left=~(p %26 q)&right=~p | ~q`

Looking at the raw JSON is often the fastest way to debug.

---

## Formula syntax

Three ways to write each operator. Use whichever you find clearest — you can paste the
symbols straight out of the textbook.

| Symbol | ASCII | Word | Name |
|---|---|---|---|
| ¬ | `~` `!` | `not` | negation |
| ∧ | `&` `&&` | `and` | conjunction |
| ∨ | `\|` `\|\|` | `or` | disjunction |
| ⊕ | `^` | `xor` | exclusive or |
| → | `->` `=>` | `implies` | implication |
| ↔ | `<->` `<=>` | `iff` | biconditional |

Binding order, loosest to tightest: ↔, →, ⊕, ∨, ∧, ¬.

Implication groups to the right, so `p -> q -> r` means `p -> (q -> r)`.
Everything else groups to the left. Use parentheses whenever you are unsure — they
always win.

Variables can be single letters or whole words, so `raining -> wet` is a valid formula.

---

## How you are graded

Your grade comes from the tests. Run them as often as you like:

```bash
mvn test                                  # everything
mvn test -Dtest=OperatorsTest             # just Part 1
mvn test -Dtest=LogicAnalyzerTest         # just Part 2
```

Every test has a name that tells you which row of which truth table it checked, and a
failure message that explains what the answer should have been. Read them — they are
written to teach, not just to fail you.

The same tests run automatically on GitHub every time you push. Open the **Actions**
tab in your repository to see the result. The summary tells you how many checks passed
and which ones did not.

There are 59 checks in total. `ParserTest` accounts for 13 of them and should pass
without you doing anything.

---

## Submitting

Commit and push. That is the whole submission.

```bash
git add .
git commit -m "Complete Lab 1"
git push
```

Then paste your repository URL into the Canvas assignment.

**If you run out of Codespaces hours**, you can still submit — committing costs
nothing. Press the `.` key on your repository page on github.com to open a free browser
editor, make your changes there, and commit. The tests still run on GitHub's servers.
See the course page for more on managing your Codespaces quota.

---

## A note on what you are actually building

The four methods in `LogicAnalyzer` are a decision procedure: they answer a question
about a formula by checking every possibility. That is honest and it is correct, and
for the handful of variables in this lab it is instant.

It also does not scale. Ten variables is 1,024 rows. Thirty variables is over a billion.
Deciding whether a formula is satisfiable was the first problem ever proved NP-complete,
and no one knows a method that is fast in general.

And yet industrial SAT solvers routinely handle formulas with millions of variables,
because real formulas are not worst cases. Those solvers sit inside chip verification
tools, compilers, and program analyzers, and they are doing exactly what you are about
to write, only with a great deal more cleverness about which rows they can skip.
