package io.elsci.intellijchemplugin.action;

class StringUtil {
    public static String trimQuotesAndWhitespaces(String value) {
        if (value == null) return null;
        int start = 0;
        while (start < value.length() && isQuoteOrWhitespace(value.charAt(start)))
            start++;
        if (start == value.length())
            return null;
        int end = value.length() - 1;
        while (end >= start && isQuoteOrWhitespace(value.charAt(end)))
            end--;
        return value.substring(start, end + 1);
    }

    private static boolean isQuoteOrWhitespace(char c) {
        return c == '"' || c == '\'' || Character.isWhitespace(c);
    }
}
