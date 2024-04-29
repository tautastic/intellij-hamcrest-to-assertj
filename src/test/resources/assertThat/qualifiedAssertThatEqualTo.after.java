import org.assertj.core.api.Assertions;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;

public void testEqualTo() {
    final int myLuckyNumber = 9;

    Assertions.assertThat(myLuckyNumber).isEqualTo(9);
}