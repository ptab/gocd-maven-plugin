package me.taborda.gocd.maven.plugin;

/**
 *
 * @author ruckc
 */
public class StringUtils {

    
    public static boolean equals(String a, String b) {
        if(a == null && b == null) {
            return true;
        }
        if(a == null || b == null) {
            return false;
        }
        return a.equals(b);
    }
}
