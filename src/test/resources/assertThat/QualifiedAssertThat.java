package org.example;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.time.LocalDateTime;

public class CommonUtilsTest {

    private static class CommonUtils {
        public static boolean isLocalDateTimeBetween(final LocalDateTime toCheck, final LocalDateTime from, final LocalDateTime to) {
            if (Objects.isNull(toCheck) || Objects.isNull(from) || Objects.isNull(to)) {
                return false;
            }

            return toCheck.isAfter(from) && toCheck.isBefore(to);
        }
    }

    @Test
    public void testIsLocalDateTimeBetween() {
        final boolean result = CommonUtils.isLocalDateTimeBetween(null, null, null);

        MatcherAssert.assertThat("wrong result", result, Matchers.is(false));
    }

}