import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public void testEqualToNull() {
    final int myLuckyNumber = 1;

    <caret>assertThat("wrong result", myLuckyNumber, equalTo(null));
}