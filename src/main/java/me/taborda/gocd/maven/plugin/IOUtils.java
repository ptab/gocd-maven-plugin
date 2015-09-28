package me.taborda.gocd.maven.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

/**
 * @author ruckc
 */
public class IOUtils {

    public static String toString(InputStream is) throws IOException {
        return toString(is, 16384);
    }

    static String toString(InputStream is, int bufsize) throws IOException {
        InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
        StringWriter sw = new StringWriter();
        copy(isr, sw, bufsize);
        return sw.toString();
    }
    
    static void copy(Reader r, Writer w, int bufsize) throws IOException {
        char[] buf = new char[bufsize];
        int read;
        while ((read = r.read(buf)) > 0) {
            w.write(buf, 0, read);
        }
    }
}
