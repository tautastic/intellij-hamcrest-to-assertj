import org.assertj.core.api.Assertions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public void testEqualToPrimitiveBool() {
    final boolean falseStatement = 1 == 2;

    <caret>Assertions.assertThat(falseStatement).isFalse();
}