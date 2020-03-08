package 학교;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by ehe12 on 2018-08-23.
 */
public class Main {
    public static void main(String[] args) {
        TreeMap<Integer, String> scores = new TreeMap<>();
        scores.put(90, "김재영");
        scores.put(65, "이호선");
        scores.put(85, "김종수");
        scores.put(97, "이경은");
        scores.put(75, "김성렬");
        scores.put(80, "김혁준");

        Map.Entry<Integer, String> entry = scores.firstEntry();
        System.out.println(entry.getKey() + "-" + entry.getValue());

        entry = scores.lastEntry();
        System.out.println(entry.getKey() + "-" + entry.getValue());

        entry = scores.lowerEntry(95);
        System.out.println(entry.getKey() + "-" + entry.getValue());

        entry = scores.higherEntry(95);
        System.out.println(entry.getKey() + "-" + entry.getValue());

        entry = scores.floorEntry(95);
        System.out.println(entry.getKey() + "-" + entry.getValue());

        entry = scores.ceilingEntry(84);
        System.out.println(entry.getKey() + "-" + entry.getValue());
    }
}
