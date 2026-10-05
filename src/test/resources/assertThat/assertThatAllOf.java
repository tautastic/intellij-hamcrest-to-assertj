import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;

public void testAllOf() {
    final int myLuckyNumber = 13;

    <caret>assertThat("wrong number", myLuckyNumber, allOf(equalTo(11), equalTo(13)));
}