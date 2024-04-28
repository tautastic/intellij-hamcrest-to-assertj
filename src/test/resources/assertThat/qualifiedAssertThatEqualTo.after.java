import org.assertj.core.api.Assertions;

public void testEqualTo() {
    final int myLuckyNumber = 9;

    Assertions.assertThat(myLuckyNumber).isEqualTo(9);
}