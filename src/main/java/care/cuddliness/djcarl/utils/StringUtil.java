package care.cuddliness.djcarl.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class StringUtil {
    private String str;
    private Map<Character, Integer> strMap;
    private Pattern wordPattern;
    private static final float THRESHOLD = 0.8f;

    public StringUtil() {}

    public StringUtil(String str) {
        this.str = str;
        this.strMap = this.generateCharMap(str);
        this.wordPattern = Pattern.compile(
                "(?<![\\w])(" + Pattern.quote(str) + ")(?![\\w])",
                Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS
        );
    }

    private Map<Character, Integer> generateCharMap(String str) {
        Map<Character, Integer> map = new HashMap<>();
        for (char c : str.toCharArray()) {
            map.merge(c, 1, Integer::sum);
        }
        return map;
    }

    public boolean isSimilar(String compareStr) {
        if (compareStr == null || compareStr.isEmpty()) return false;

        String reversed = new StringBuilder(compareStr).reverse().toString();
        if (reversed.equalsIgnoreCase(this.str)) return false;

        float lengthRatio = (float) Math.min(this.str.length(), compareStr.length()) /
                (float) Math.max(this.str.length(), compareStr.length());
        if (lengthRatio < 0.6f) return false;

        if (wordPattern.matcher(compareStr).find()) return true;

        Map<Character, Integer> compareStrMap = this.generateCharMap(compareStr);
        int similarChars = 0;
        int totalStrChars = Math.max(this.str.length(), compareStr.length());

        for (char c : compareStrMap.keySet()) {
            Integer inBase    = strMap.get(c);
            Integer inCompare = compareStrMap.get(c);
            if (inBase != null) {
                similarChars += Math.min(inBase, inCompare);
            }
        }

        float thisThreshold = (float) similarChars / (float) totalStrChars;

        return thisThreshold > THRESHOLD;
    }
}
