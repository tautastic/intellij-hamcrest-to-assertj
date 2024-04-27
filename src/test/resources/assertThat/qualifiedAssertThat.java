import mock.hamcrest.MatcherAssert;

public void testIsFalse() {
    MatcherAssert.assertThat("wrong result", false, Matchers.is(false));
}