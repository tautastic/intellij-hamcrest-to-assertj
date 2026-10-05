import org.assertj.core.api.Assertions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public void testIsValue() {
    final int myLuckyNumber = 13;

    <caret>Assertions.assertThat(myLuckyNumber).as("wrong number").isEqualTo(13);
}