import org.assertj.core.api.Assertions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public void testIsNested() {
    final Object myObject = new Object();

    <caret>Assertions.assertThat(myObject).as("wrong object").isNotNull();
}