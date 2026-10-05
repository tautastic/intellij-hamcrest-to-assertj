import org.assertj.core.api.Assertions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;

public void testAllOf() {
    final int myLuckyNumber = 13;

    <caret>Assertions.assertThat(myLuckyNumber).as("wrong number").satisfies(actual -> Assertions.assertThat(actual).isEqualTo(11), actual -> Assertions.assertThat(actual).isEqualTo(13));
}