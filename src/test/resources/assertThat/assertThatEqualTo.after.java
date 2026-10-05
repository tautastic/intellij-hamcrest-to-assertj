import org.assertj.core.api.Assertions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public void testEqualTo() {
    final int myLuckyNumber = 9;

    Assertions.assertThat(myLuckyNumber).as("wrong lucky number").isEqualTo(9);
}