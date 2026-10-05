import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

public void testAnyOf() {
    final int myLuckyNumber = 13;

    <caret>assertThat("wrong number", myLuckyNumber, anyOf(equalTo(11), equalTo(13)));
}