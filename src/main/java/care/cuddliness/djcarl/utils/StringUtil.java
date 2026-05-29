package care.cuddliness.djcarl.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StringUtil {
    private static final float THRESHOLD = (float) 0.65;

    private final Logger logger = Logger.getLogger(StringUtil.class.getName());

    private String str;
    private Map <Character, Integer> strMap;
    public StringUtil() {}

    public StringUtil(String str){ //java.lang.String is final...
        this.str = str;
        this.strMap = this.generateCharMap(str);
    }
    private Map <Character, Integer> generateCharMap(String str){
        Map <Character, Integer> map = new HashMap<>();
        Integer currentChar;
        for(char c: str.toCharArray()){
            currentChar = map.get(c);
            if(currentChar == null){
                map.put(c, 1);
            } else {
                map.put(c, currentChar+1);
            }
        }
        return map;
    }

    public boolean isSimilar(String compareStr){
        Map<Character, Integer> compareStrMap = this.generateCharMap(compareStr);
        Set<Character> charSet = compareStrMap.keySet();
        int similarChars = 0;
        int totalStrChars = this.str.length();
        float thisThreshold;

        if(totalStrChars < compareStrMap.size()){
            totalStrChars = compareStr.length();
        }

        Iterator it = charSet.iterator();
        char currentChar;
        Integer currentCountStrMap;
        Integer currentCountCompareStrMap;
        while(it.hasNext()){
            currentChar = (Character)it.next();
            currentCountStrMap = strMap.get(currentChar);
            if(currentCountStrMap != null){
                currentCountCompareStrMap = compareStrMap.get(currentChar);
                if (currentCountCompareStrMap >= currentCountStrMap){
                    similarChars += currentCountStrMap;
                } else {
                    similarChars += currentCountCompareStrMap;
                }
            }
        }

        thisThreshold = ((float) similarChars)/((float) totalStrChars);
        Logger.getLogger(StringUtil.class.getName()).log(Level.INFO, "similarChars: {0}, totalStrChars: {1}, thisThreshold: {2}", new Object[]{similarChars, totalStrChars, thisThreshold});
        if(thisThreshold > THRESHOLD){
            return true;
        }
        return false;
    }
}
