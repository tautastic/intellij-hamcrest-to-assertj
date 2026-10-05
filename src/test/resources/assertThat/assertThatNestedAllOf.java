import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

public void testNestedAllOf() {
    final int actual = 13;

    <caret>assertThat("wrong number", actual, allOf(anyOf(equalTo(11), equalTo(13)), equalTo(13)));
}