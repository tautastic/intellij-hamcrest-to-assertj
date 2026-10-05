import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;

public void testHasSizeMatcher() {
    final ArrayList<Object> myLuckyNumbers = new ArrayList<>();

    <caret>assertThat("wrong size", myLuckyNumbers, hasSize(greaterThan(2)));
}