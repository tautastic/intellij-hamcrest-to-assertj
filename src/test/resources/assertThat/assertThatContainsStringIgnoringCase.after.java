import org.assertj.core.api.Assertions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;

public void testContainsStringIgnoringCase() {
    <caret>Assertions.assertThat("there is no SUCCess without SUCC").containsIgnoringCase("succ");
}