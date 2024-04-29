import org.assertj.core.api.Assertions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;

public void testNull() {
    final int myLuckyNumber = 13;

    <caret>Assertions.assertThat(myLuckyNumber).isNull();
}