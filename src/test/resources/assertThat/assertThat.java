import static mock.hamcrest.MatcherAssert.assertThat;

public void testIsFalse() {
    <caret>assertThat("wrong result", false, Matchers.is(false));
}