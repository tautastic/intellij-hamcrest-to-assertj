import org.assertj.core.api.Assertions;

import java.util.ArrayList;

public void testHasSize() {
    final ArrayList<Object> myLuckyNumbers = new ArrayList<>();

    <caret>Assertions.assertThat(myLuckyNumbers).hasSize(2);
}