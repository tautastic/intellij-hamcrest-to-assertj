import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public void testIsValue() {
    final int myLuckyNumber = 13;

    <caret>assertThat("wrong number", myLuckyNumber, is(13));
}