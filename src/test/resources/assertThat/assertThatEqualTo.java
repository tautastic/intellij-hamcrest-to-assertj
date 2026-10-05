import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public void testEqualTo() {
    final int myLuckyNumber = 9;

    <caret>assertThat("wrong lucky number", myLuckyNumber, equalTo(9));
}