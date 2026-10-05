import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public void testIsNested() {
    final Object myObject = new Object();

    <caret>assertThat("wrong object", myObject, is(notNullValue()));
}