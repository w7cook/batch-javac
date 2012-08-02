package format;

import java.io.IOException;
import java.util.Locale;

class FixedString implements FormatString {
    private String s;
    FixedString(String s) { this.s = s; }
    public int index() { return -2; }
    public void print(Object arg, Locale l, Appendable a)
        throws IOException { a.append(s); }
    public String toString() { return s; }
}