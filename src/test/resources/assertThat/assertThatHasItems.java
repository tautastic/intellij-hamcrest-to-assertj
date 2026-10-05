import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;

public void testHasItems() {
    final ArrayList<Object> myLuckyNumbers = new ArrayList<>();
    myLuckyNumbers.add(13);
    myLuckyNumbers.add(420);

    <caret>assertThat("wrong elements", myLuckyNumbers, hasItems(13, 420));
}