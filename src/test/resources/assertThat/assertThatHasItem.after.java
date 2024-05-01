import org.assertj.core.api.Assertions;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;

public void testHasItem() {
    final ArrayList<Object> myLuckyNumbers = new ArrayList<>();
    myLuckyNumbers.add(13);
    myLuckyNumbers.add(420);

    <caret>Assertions.assertThat(myLuckyNumbers).contains(13);
}