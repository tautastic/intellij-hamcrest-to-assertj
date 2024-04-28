import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;

public void testContainsStringIgnoringCase() {
    <caret>assertThat("wrong string", "there is no SUCCess without SUCC", containsStringIgnoringCase("succ"));
}