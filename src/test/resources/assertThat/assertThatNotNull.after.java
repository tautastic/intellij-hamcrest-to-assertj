import org.assertj.core.api.Assertions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

public void testNotNull() {
    final int myLuckyNumber = 13;

    <caret>Assertions.assertThat(myLuckyNumber).isNotNull();
}