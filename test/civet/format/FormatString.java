package format;

import java.io.IOException;
import java.util.Locale;

interface FormatString {
    int index();
    void print(Object arg, Locale l, Appendable a) throws IOException;
    String toString();
}