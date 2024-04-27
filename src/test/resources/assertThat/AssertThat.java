import static mock.hamcrest.MatcherAssert.assertThat;

public void testIsFalse() {
    assertThat("wrong result", false, Matchers.is(false));
}