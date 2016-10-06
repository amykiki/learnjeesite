package exercise;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Amysue on 2016/10/3.
 */
public class Cases {
    @Test
    public void testPattern() {
        Pattern p = Pattern.compile("cat");
        Matcher m = p.matcher("one cat two cats in the yard");
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "dog");
            System.out.println(sb.toString());
        }
        m.appendTail(sb);
        System.out.println(sb.toString());

        p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
        m = p.matcher("select * from user order by age, salary");
        sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "");
            System.out.println(sb.toString());
        }
        m.appendTail(sb);
        System.out.println(sb.toString());
    }
}
