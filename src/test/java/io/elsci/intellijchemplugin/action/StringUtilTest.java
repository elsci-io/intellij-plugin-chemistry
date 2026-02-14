package io.elsci.intellijchemplugin.action;

import org.junit.Test;

import static io.elsci.intellijchemplugin.action.StringUtil.trimQuotesAndWhitespaces;
import static io.qala.datagen.RandomShortApi.alphanumeric;
import static io.qala.datagen.RandomValue.between;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StringUtilTest {
    @Test public void trimming_trimsQuotesAndWhitespaces_onBothEnds() {
        String content = alphanumeric(10, 20);
        String value = "%s%s%s".formatted(withQuotesAndWhitespaces(), content, withQuotesAndWhitespaces());
        assertEquals(content, trimQuotesAndWhitespaces(value));
    }
    @Test public void trimming_returnsNull_whenNullOrEmpty() {
        assertNull(trimQuotesAndWhitespaces(null));
        assertNull(trimQuotesAndWhitespaces(""));
    }
    @Test public void trimming_returnsNull_whenOnlyQuotesAndWhitespaces() {
        assertNull(trimQuotesAndWhitespaces(withQuotesAndWhitespaces()));
    }

    private static String withQuotesAndWhitespaces() {
        return between(0, 10).string('"', '\'', ' ');
    }
}