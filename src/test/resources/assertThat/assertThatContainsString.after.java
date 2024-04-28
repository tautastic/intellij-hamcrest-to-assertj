import org.assertj.core.api.Assertions;

public void testContainsString() {
    <caret>Assertions.assertThat("there is no success without succ").contains("succ");
}