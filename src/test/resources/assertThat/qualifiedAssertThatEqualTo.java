import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

public void testEqualTo() {
    final int myLuckyNumber = 9;

    <caret>MatcherAssert.assertThat("wrong lucky number", myLuckyNumber, Matchers.equalTo(9));
}