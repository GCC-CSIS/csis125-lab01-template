package edu.glendale.csis125.lab01;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Checks that Spring can build the application.
 *
 * <p>This will fail while Part 2 is unfinished only if you delete a bean or change a
 * constructor. It does not call your logic, so it should pass on a fresh clone.
 */
@SpringBootTest
@DisplayName("The application starts up")
class Lab01ApplicationTests {

    @Test
    @DisplayName("Spring wires LogicAnalyzer, TruthTableService and the controller together")
    void contextLoads() {
        // Empty on purpose: if the context cannot be built, this test fails.
    }
}
