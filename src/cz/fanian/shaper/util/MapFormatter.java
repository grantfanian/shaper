package cz.fanian.shaper.util;

import java.util.Map;

public class MapFormatter {
    private MapFormatter() {
        // static class lol
    }

    public static String formatMap(Map<?, ?> map) {
        var sb = new StringBuilder();
        map.forEach((k, v) -> sb.append(String.format("%s: %s\n", k.toString(), v.toString())));
        return sb.toString();
    }
}
