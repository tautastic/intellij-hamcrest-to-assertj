import mock.hamcrest.MatcherAssert;

public void testIsFalse() {
    <caret>MatcherAssert.assertThat("wrong result", false, Matchers.is(false));
}