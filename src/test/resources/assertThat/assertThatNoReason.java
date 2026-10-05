import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public void testNoReason() {
    final int myLuckyNumber = 13;

    <caret>assertThat(myLuckyNumber, equalTo(13));
}