import org.assertj.core.api.Assertions;

public void testContainsStringIgnoringCase() {
    <caret>Assertions.assertThat("there is no SUCCess without SUCC").containsIgnoringCase("succ");
}