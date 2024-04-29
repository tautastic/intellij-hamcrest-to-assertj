import java.lang.Boolean;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public void testEqualToBool() {
    final boolean falseStatement = 1 == 2;

    <caret>assertThat("wrong result", falseStatement, equalTo(false));
    <caret>assertThat("wrong result", falseStatement, equalTo(Boolean.FALSE));
}