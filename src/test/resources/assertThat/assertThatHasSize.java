import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public void testHasSize() {
    final ArrayList<Object> myLuckyNumbers = new ArrayList<>();

    <caret>assertThat("wrong size", myLuckyNumbers, hasSize(2));
}