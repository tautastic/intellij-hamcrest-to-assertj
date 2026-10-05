import org.assertj.core.api.Assertions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

public void testNestedAllOf() {
    final int actual = 13;

    <caret>Assertions.assertThat(actual).as("wrong number").satisfies(actual1 -> Assertions.assertThat(actual1).satisfiesAnyOf(actual11 -> Assertions.assertThat(actual11).isEqualTo(11), actual11 -> Assertions.assertThat(actual11).isEqualTo(13)), actual1 -> Assertions.assertThat(actual1).isEqualTo(13));
}