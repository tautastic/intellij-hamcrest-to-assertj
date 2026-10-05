import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.samcrest.Matchers.empty;

public void testEmpty() {
    final ArrayList<Object> myLuckyNumbers = new ArrayList<>();

    <caret>assertThat("wrong size", myLuckyNumbers, empty());
}