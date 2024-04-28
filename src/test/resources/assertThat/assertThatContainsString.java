import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public void testContainsString() {
    <caret>assertThat("wrong string", "there is no success without succ", containsString("succ"));
}