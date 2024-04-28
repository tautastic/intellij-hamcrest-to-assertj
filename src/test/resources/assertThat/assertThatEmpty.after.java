import org.assertj.core.api.Assertions;

import java.util.ArrayList;

public void testEmpty() {
    final ArrayList<Object> myLuckyNumbers = new ArrayList<>();

    <caret>Assertions.assertThat(myLuckyNumbers).isEmpty();
}