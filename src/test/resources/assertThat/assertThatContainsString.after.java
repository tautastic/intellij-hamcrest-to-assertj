import org.assertj.core.api.Assertions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;

public void testContainsString() {
    <caret>Assertions.assertThat("there is no success without succ").contains("succ");
}