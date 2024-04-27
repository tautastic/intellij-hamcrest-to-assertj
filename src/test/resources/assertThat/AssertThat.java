import static org.hamcrest.MatcherAssert.assertThat;

public void testIsFalse() {
    assertThat("wrong result", result, Matchers.is(false));
}