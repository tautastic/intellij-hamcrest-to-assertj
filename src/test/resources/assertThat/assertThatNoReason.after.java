import org.assertj.core.api.Assertions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public void testNoReason() {
    final int myLuckyNumber = 13;

    <caret>Assertions.assertThat(myLuckyNumber).isEqualTo(13);
}