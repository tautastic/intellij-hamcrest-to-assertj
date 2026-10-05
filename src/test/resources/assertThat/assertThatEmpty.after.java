import org.assertj.core.api.Assertions;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

public void testEmpty() {
    final ArrayList<Object> myLuckyNumbers = new ArrayList<>();

    <caret>Assertions.assertThat(myLuckyNumbers).as("wrong size").isEmpty();
}