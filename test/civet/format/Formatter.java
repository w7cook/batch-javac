package format;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.text.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.civet.Civet;
import org.civet.Civet.Compile;

public class Formatter {
  private Appendable a;
  private final Locale l;

  private IOException lastException;

  private final char zero;
  static double scaleUp;

  // 1 (sign) + 19 (max # sig digits) + 1 ('.') + 1 ('e') + 1 (sign)
  // + 3 (max # exp digits) + 4 (error) = 30
  static final int MAX_FD_CHARS = 30;

  /**
   * Returns a charset object for the given charset name.
   * 
   * @throws NullPointerException
   *           is csn is null
   * @throws UnsupportedEncodingException
   *           if the charset is not supported
   */
  private static Charset toCharset(String csn)
      throws UnsupportedEncodingException {
    Objects.requireNonNull(csn, "charsetName");
    try {
      return Charset.forName(csn);
    } catch (IllegalCharsetNameException | UnsupportedCharsetException unused) {
      // UnsupportedEncodingException should be thrown
      throw new UnsupportedEncodingException(csn);
    }
  }

  private static final Appendable nonNullAppendable(Appendable a) {
    if (a == null)
      return new StringBuilder();

    return a;
  }

  /* Private constructors */
  private Formatter(Locale l, Appendable a) {
    this.a = a;
    this.l = l;
    this.zero = getZero(l);
  }

  private Formatter(Charset charset, Locale l, File file)
      throws FileNotFoundException {
    this(l, new BufferedWriter(new OutputStreamWriter(
        new FileOutputStream(file), charset)));
  }

  /**
   * Constructs a new formatter.
   * 
   * <p>
   * The destination of the formatted output is a {@link StringBuilder} which
   * may be retrieved by invoking {@link #out out()} and whose current content
   * may be converted into a string by invoking {@link #toString toString()}.
   * The locale used is the {@linkplain Locale#getDefault() default locale} for
   * this instance of the Java virtual machine.
   */
  public Formatter() {
    this(Locale.getDefault(Locale.Category.FORMAT), new StringBuilder());
  }

  /**
   * Constructs a new formatter with the specified destination.
   * 
   * <p>
   * The locale used is the {@linkplain Locale#getDefault() default locale} for
   * this instance of the Java virtual machine.
   * 
   * @param a
   *          Destination for the formatted output. If {@code a} is {@code null}
   *          then a {@link StringBuilder} will be created.
   */
  public Formatter(Appendable a) {
    this(Locale.getDefault(Locale.Category.FORMAT), nonNullAppendable(a));
  }

  /**
   * Constructs a new formatter with the specified locale.
   * 
   * <p>
   * The destination of the formatted output is a {@link StringBuilder} which
   * may be retrieved by invoking {@link #out out()} and whose current content
   * may be converted into a string by invoking {@link #toString toString()}.
   * 
   * @param l
   *          The {@linkplain java.util.Locale locale} to apply during
   *          formatting. If {@code l} is {@code null} then no localization is
   *          applied.
   */
  public Formatter(Locale l) {
    this(l, new StringBuilder());
  }

  /**
   * Constructs a new formatter with the specified destination and locale.
   * 
   * @param a
   *          Destination for the formatted output. If {@code a} is {@code null}
   *          then a {@link StringBuilder} will be created.
   * 
   * @param l
   *          The {@linkplain java.util.Locale locale} to apply during
   *          formatting. If {@code l} is {@code null} then no localization is
   *          applied.
   */
  public Formatter(Appendable a, Locale l) {
    this(l, nonNullAppendable(a));
  }

  /**
   * Constructs a new formatter with the specified file name.
   * 
   * <p>
   * The charset used is the
   * {@linkplain java.nio.charset.Charset#defaultCharset() default charset} for
   * this instance of the Java virtual machine.
   * 
   * <p>
   * The locale used is the {@linkplain Locale#getDefault() default locale} for
   * this instance of the Java virtual machine.
   * 
   * @param fileName
   *          The name of the file to use as the destination of this formatter.
   *          If the file exists then it will be truncated to zero size;
   *          otherwise, a new file will be created. The output will be written
   *          to the file and is buffered.
   * 
   * @throws SecurityException
   *           If a security manager is present and
   *           {@link SecurityManager#checkWrite checkWrite(fileName)} denies
   *           write access to the file
   * 
   * @throws FileNotFoundException
   *           If the given file name does not denote an existing, writable
   *           regular file and a new regular file of that name cannot be
   *           created, or if some other error occurs while opening or creating
   *           the file
   */
  public Formatter(String fileName) throws FileNotFoundException {
    this(Locale.getDefault(Locale.Category.FORMAT), new BufferedWriter(
        new OutputStreamWriter(new FileOutputStream(fileName))));
  }

  /**
   * Constructs a new formatter with the specified file name and charset.
   * 
   * <p>
   * The locale used is the {@linkplain Locale#getDefault default locale} for
   * this instance of the Java virtual machine.
   * 
   * @param fileName
   *          The name of the file to use as the destination of this formatter.
   *          If the file exists then it will be truncated to zero size;
   *          otherwise, a new file will be created. The output will be written
   *          to the file and is buffered.
   * 
   * @param csn
   *          The name of a supported {@linkplain java.nio.charset.Charset
   *          charset}
   * 
   * @throws FileNotFoundException
   *           If the given file name does not denote an existing, writable
   *           regular file and a new regular file of that name cannot be
   *           created, or if some other error occurs while opening or creating
   *           the file
   * 
   * @throws SecurityException
   *           If a security manager is present and
   *           {@link SecurityManager#checkWrite checkWrite(fileName)} denies
   *           write access to the file
   * 
   * @throws UnsupportedEncodingException
   *           If the named charset is not supported
   */
  public Formatter(String fileName, String csn) throws FileNotFoundException,
      UnsupportedEncodingException {
    this(fileName, csn, Locale.getDefault(Locale.Category.FORMAT));
  }

  /**
   * Constructs a new formatter with the specified file name, charset, and
   * locale.
   * 
   * @param fileName
   *          The name of the file to use as the destination of this formatter.
   *          If the file exists then it will be truncated to zero size;
   *          otherwise, a new file will be created. The output will be written
   *          to the file and is buffered.
   * 
   * @param csn
   *          The name of a supported {@linkplain java.nio.charset.Charset
   *          charset}
   * 
   * @param l
   *          The {@linkplain java.util.Locale locale} to apply during
   *          formatting. If {@code l} is {@code null} then no localization is
   *          applied.
   * 
   * @throws FileNotFoundException
   *           If the given file name does not denote an existing, writable
   *           regular file and a new regular file of that name cannot be
   *           created, or if some other error occurs while opening or creating
   *           the file
   * 
   * @throws SecurityException
   *           If a security manager is present and
   *           {@link SecurityManager#checkWrite checkWrite(fileName)} denies
   *           write access to the file
   * 
   * @throws UnsupportedEncodingException
   *           If the named charset is not supported
   */
  public Formatter(String fileName, String csn, Locale l)
      throws FileNotFoundException, UnsupportedEncodingException {
    this(toCharset(csn), l, new File(fileName));
  }

  /**
   * Constructs a new formatter with the specified file.
   * 
   * <p>
   * The charset used is the
   * {@linkplain java.nio.charset.Charset#defaultCharset() default charset} for
   * this instance of the Java virtual machine.
   * 
   * <p>
   * The locale used is the {@linkplain Locale#getDefault() default locale} for
   * this instance of the Java virtual machine.
   * 
   * @param file
   *          The file to use as the destination of this formatter. If the file
   *          exists then it will be truncated to zero size; otherwise, a new
   *          file will be created. The output will be written to the file and
   *          is buffered.
   * 
   * @throws SecurityException
   *           If a security manager is present and
   *           {@link SecurityManager#checkWrite checkWrite(file.getPath())}
   *           denies write access to the file
   * 
   * @throws FileNotFoundException
   *           If the given file object does not denote an existing, writable
   *           regular file and a new regular file of that name cannot be
   *           created, or if some other error occurs while opening or creating
   *           the file
   */
  public Formatter(File file) throws FileNotFoundException {
    this(Locale.getDefault(Locale.Category.FORMAT), new BufferedWriter(
        new OutputStreamWriter(new FileOutputStream(file))));
  }

  /**
   * Constructs a new formatter with the specified file and charset.
   * 
   * <p>
   * The locale used is the {@linkplain Locale#getDefault default locale} for
   * this instance of the Java virtual machine.
   * 
   * @param file
   *          The file to use as the destination of this formatter. If the file
   *          exists then it will be truncated to zero size; otherwise, a new
   *          file will be created. The output will be written to the file and
   *          is buffered.
   * 
   * @param csn
   *          The name of a supported {@linkplain java.nio.charset.Charset
   *          charset}
   * 
   * @throws FileNotFoundException
   *           If the given file object does not denote an existing, writable
   *           regular file and a new regular file of that name cannot be
   *           created, or if some other error occurs while opening or creating
   *           the file
   * 
   * @throws SecurityException
   *           If a security manager is present and
   *           {@link SecurityManager#checkWrite checkWrite(file.getPath())}
   *           denies write access to the file
   * 
   * @throws UnsupportedEncodingException
   *           If the named charset is not supported
   */
  public Formatter(File file, String csn) throws FileNotFoundException,
      UnsupportedEncodingException {
    this(file, csn, Locale.getDefault(Locale.Category.FORMAT));
  }

  /**
   * Constructs a new formatter with the specified file, charset, and locale.
   * 
   * @param file
   *          The file to use as the destination of this formatter. If the file
   *          exists then it will be truncated to zero size; otherwise, a new
   *          file will be created. The output will be written to the file and
   *          is buffered.
   * 
   * @param csn
   *          The name of a supported {@linkplain java.nio.charset.Charset
   *          charset}
   * 
   * @param l
   *          The {@linkplain java.util.Locale locale} to apply during
   *          formatting. If {@code l} is {@code null} then no localization is
   *          applied.
   * 
   * @throws FileNotFoundException
   *           If the given file object does not denote an existing, writable
   *           regular file and a new regular file of that name cannot be
   *           created, or if some other error occurs while opening or creating
   *           the file
   * 
   * @throws SecurityException
   *           If a security manager is present and
   *           {@link SecurityManager#checkWrite checkWrite(file.getPath())}
   *           denies write access to the file
   * 
   * @throws UnsupportedEncodingException
   *           If the named charset is not supported
   */
  public Formatter(File file, String csn, Locale l)
      throws FileNotFoundException, UnsupportedEncodingException {
    this(toCharset(csn), l, file);
  }

  /**
   * Constructs a new formatter with the specified print stream.
   * 
   * <p>
   * The locale used is the {@linkplain Locale#getDefault() default locale} for
   * this instance of the Java virtual machine.
   * 
   * <p>
   * Characters are written to the given {@link java.io.PrintStream PrintStream}
   * object and are therefore encoded using that object's charset.
   * 
   * @param ps
   *          The stream to use as the destination of this formatter.
   */
  public Formatter(PrintStream ps) {
    this(Locale.getDefault(Locale.Category.FORMAT), (Appendable) Objects
        .requireNonNull(ps));
  }

  /**
   * Constructs a new formatter with the specified output stream.
   * 
   * <p>
   * The charset used is the
   * {@linkplain java.nio.charset.Charset#defaultCharset() default charset} for
   * this instance of the Java virtual machine.
   * 
   * <p>
   * The locale used is the {@linkplain Locale#getDefault() default locale} for
   * this instance of the Java virtual machine.
   * 
   * @param os
   *          The output stream to use as the destination of this formatter. The
   *          output will be buffered.
   */
  public Formatter(OutputStream os) {
    this(Locale.getDefault(Locale.Category.FORMAT), new BufferedWriter(
        new OutputStreamWriter(os)));
  }

  /**
   * Constructs a new formatter with the specified output stream and charset.
   * 
   * <p>
   * The locale used is the {@linkplain Locale#getDefault default locale} for
   * this instance of the Java virtual machine.
   * 
   * @param os
   *          The output stream to use as the destination of this formatter. The
   *          output will be buffered.
   * 
   * @param csn
   *          The name of a supported {@linkplain java.nio.charset.Charset
   *          charset}
   * 
   * @throws UnsupportedEncodingException
   *           If the named charset is not supported
   */
  public Formatter(OutputStream os, String csn)
      throws UnsupportedEncodingException {
    this(os, csn, Locale.getDefault(Locale.Category.FORMAT));
  }

  /**
   * Constructs a new formatter with the specified output stream, charset, and
   * locale.
   * 
   * @param os
   *          The output stream to use as the destination of this formatter. The
   *          output will be buffered.
   * 
   * @param csn
   *          The name of a supported {@linkplain java.nio.charset.Charset
   *          charset}
   * 
   * @param l
   *          The {@linkplain java.util.Locale locale} to apply during
   *          formatting. If {@code l} is {@code null} then no localization is
   *          applied.
   * 
   * @throws UnsupportedEncodingException
   *           If the named charset is not supported
   */
  public Formatter(OutputStream os, String csn, Locale l)
      throws UnsupportedEncodingException {
    this(l, new BufferedWriter(new OutputStreamWriter(os, csn)));
  }

  private static char getZero(Locale l) {
    if ((l != null) && !l.equals(Locale.US)) {
      DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance(l);
      return dfs.getZeroDigit();
    } else {
      return '0';
    }
  }

  /**
   * Returns the locale set by the construction of this formatter.
   * 
   * <p>
   * The {@link #format(java.util.Locale,String,Object...) format} method for
   * this object which has a locale argument does not change this value.
   * 
   * @return {@code null} if no localization is applied, otherwise a locale
   * 
   * @throws FormatterClosedException
   *           If this formatter has been closed by invoking its
   *           {@link #close()} method
   */
  public Locale locale() {
    ensureOpen();
    return l;
  }

  /**
   * Returns the destination for the output.
   * 
   * @return The destination for the output
   * 
   * @throws FormatterClosedException
   *           If this formatter has been closed by invoking its
   *           {@link #close()} method
   */
  public Appendable out() {
    ensureOpen();
    return a;
  }

  /**
   * Returns the result of invoking {@code toString()} on the destination for
   * the output. For example, the following code formats text into a
   * {@link StringBuilder} then retrieves the resultant string:
   * 
   * <blockquote>
   * 
   * <pre>
   * Formatter f = new Formatter();
   * f.format(&quot;Last reboot at %tc&quot;, lastRebootDate);
   * String s = f.toString();
   * // -&gt; s == &quot;Last reboot at Sat Jan 01 00:00:00 PST 2000&quot;
   * </pre>
   * 
   * </blockquote>
   * 
   * <p>
   * An invocation of this method behaves in exactly the same way as the
   * invocation
   * 
   * <pre>
   * out().toString()
   * </pre>
   * 
   * <p>
   * Depending on the specification of {@code toString} for the
   * {@link Appendable}, the returned string may or may not contain the
   * characters written to the destination. For instance, buffers typically
   * return their contents in {@code toString()}, but streams cannot since the
   * data is discarded.
   * 
   * @return The result of invoking {@code toString()} on the destination for
   *         the output
   * 
   * @throws FormatterClosedException
   *           If this formatter has been closed by invoking its
   *           {@link #close()} method
   */
  public String toString() {
    ensureOpen();
    return a.toString();
  }

  /**
   * Flushes this formatter. If the destination implements the
   * {@link java.io.Flushable} interface, its {@code flush} method will be
   * invoked.
   * 
   * <p>
   * Flushing a formatter writes any buffered output in the destination to the
   * underlying stream.
   * 
   * @throws FormatterClosedException
   *           If this formatter has been closed by invoking its
   *           {@link #close()} method
   */
  public void flush() {
    ensureOpen();
    if (a instanceof Flushable) {
      try {
        ((Flushable) a).flush();
      } catch (IOException ioe) {
        lastException = ioe;
      }
    }
  }

  /**
   * Closes this formatter. If the destination implements the
   * {@link java.io.Closeable} interface, its {@code close} method will be
   * invoked.
   * 
   * <p>
   * Closing a formatter allows it to release resources it may be holding (such
   * as open files). If the formatter is already closed, then invoking this
   * method has no effect.
   * 
   * <p>
   * Attempting to invoke any methods except {@link #ioException()} in this
   * formatter after it has been closed will result in a
   * {@link FormatterClosedException}.
   */
  public void close() {
    if (a == null)
      return;
    try {
      if (a instanceof Closeable)
        ((Closeable) a).close();
    } catch (IOException ioe) {
      lastException = ioe;
    } finally {
      a = null;
    }
  }

  private void ensureOpen() {
    if (a == null)
      throw new FormatterClosedException();
  }

  /**
   * Returns the {@code IOException} last thrown by this formatter's
   * {@link Appendable}.
   * 
   * <p>
   * If the destination's {@code append()} method never throws
   * {@code IOException}, then this method will always return {@code null}.
   * 
   * @return The last exception thrown by the Appendable or {@code null} if no
   *         such exception exists.
   */
  public IOException ioException() {
    return lastException;
  }

  /**
   * Writes a formatted string to this object's destination using the specified
   * format string and arguments. The locale used is the one defined during the
   * construction of this formatter.
   * 
   * @param format
   *          A format string as described in <a href="#syntax">Format string
   *          syntax</a>.
   * 
   * @param args
   *          Arguments referenced by the format specifiers in the format
   *          string. If there are more arguments than format specifiers, the
   *          extra arguments are ignored. The maximum number of arguments is
   *          limited by the maximum dimension of a Java array as defined by
   *          <cite>The Java&trade; Virtual Machine Specification</cite>.
   * 
   * @throws IllegalFormatException
   *           If a format string contains an illegal syntax, a format specifier
   *           that is incompatible with the given arguments, insufficient
   *           arguments given the format string, or other illegal conditions.
   *           For specification of all possible formatting errors, see the <a
   *           href="#detail">Details</a> section of the formatter class
   *           specification.
   * 
   * @throws FormatterClosedException
   *           If this formatter has been closed by invoking its
   *           {@link #close()} method
   * 
   * @return This formatter
   */
  public Formatter format(String format, Object[] args) {
    return format(l, format, args);
  }

  /**
   * Writes a formatted string to this object's destination using the specified
   * locale, format string, and arguments.
   * 
   * @param l
   *          The {@linkplain java.util.Locale locale} to apply during
   *          formatting. If {@code l} is {@code null} then no localization is
   *          applied. This does not change this object's locale that was set
   *          during construction.
   * 
   * @param format
   *          A format string as described in <a href="#syntax">Format string
   *          syntax</a>
   * 
   * @param args
   *          Arguments referenced by the format specifiers in the format
   *          string. If there are more arguments than format specifiers, the
   *          extra arguments are ignored. The maximum number of arguments is
   *          limited by the maximum dimension of a Java array as defined by
   *          <cite>The Java&trade; Virtual Machine Specification</cite>.
   * 
   * @throws IllegalFormatException
   *           If a format string contains an illegal syntax, a format specifier
   *           that is incompatible with the given arguments, insufficient
   *           arguments given the format string, or other illegal conditions.
   *           For specification of all possible formatting errors, see the <a
   *           href="#detail">Details</a> section of the formatter class
   *           specification.
   * 
   * @throws FormatterClosedException
   *           If this formatter has been closed by invoking its
   *           {@link #close()} method
   * 
   * @return This formatter
   */
  public Formatter format(Locale l, String format, Object[] args) {
    ensureOpen();

    // index of last argument referenced
    int last = -1;
    // last ordinary index
    int lasto = -1;

    FormatString[] fsa = parse(format);
    for (int i = 0; i < fsa.length; i++) {
      FormatString fs = fsa[i];
      int index = fs.index();
      try {
        if (index == -2) {
          // fixed string, "%n", or "%%"
          fs.print(null, l, a);
        } else if (index == -1) {
          // relative index
          if (last < 0 || (args != null && last > args.length - 1))
            throw new MissingFormatArgumentException(fs.toString());
          fs.print((args == null ? null : args[last]), l, a);
        } else if (index == 0) {// ordinary index
          lasto++;
          last = lasto;
          if (args != null && lasto > args.length - 1)
            throw new MissingFormatArgumentException(fs.toString());
          fs.print((args == null ? null : args[lasto]), l, a);
        } else { // explicit index
          last = index - 1;
          if (args != null && last > args.length - 1)
            throw new MissingFormatArgumentException(fs.toString());
          fs.print((args == null ? null : args[last]), l, a);
        }
      } catch (IOException x) {
        lastException = x;
      }
    }
    return this;
  }

  // %[argument_index$][flags][width][.precision][t]conversion
  private static final String formatSpecifier = "%(\\d+\\$)?([-#+ 0,(\\<]*)?(\\d+)?(\\.\\d+)?([tT])?([a-zA-Z%])";

  private static Pattern fsPattern = Pattern.compile(formatSpecifier);

  /**
   * Finds format specifiers in the format string.
   */
  private static FormatString[] parse(String s) {
    ArrayList<FormatString> al = new ArrayList<>();
    Matcher m = fsPattern.matcher(s);
    for (int i = 0, len = s.length(); i < len;) {
      if (m.find(i)) {
        // Anything between the start of the string and the beginning
        // of the format specifier is either fixed text or contains
        // an invalid format string.
        if (m.start() != i) {
          // Make sure we didn't miss any invalid format specifiers
          checkText(s, i, m.start());
          // Assume previous characters were fixed text
          al.add(new FixedString(s.substring(i, m.start())));
        }

        al.add(new FormatSpecifier(m));
        i = m.end();
      } else {
        // No more valid format specifiers. Check for possible invalid
        // format specifiers.
        checkText(s, i, len);
        // The rest of the string is fixed text
        al.add(new FixedString(s.substring(i)));
        break;
      }
    }
    return al.toArray(new FormatString[al.size()]);
  }

  private static void checkText(String s, int start, int end) {
    for (int i = start; i < end; i++) {
      // Any '%' found in the region starts an invalid format specifier.
      if (s.charAt(i) == '%') {
        char c = (i == end - 1) ? '%' : s.charAt(i + 1);
        throw new UnknownFormatConversionException(String.valueOf(c));
      }
    }
  }

  public enum BigDecimalLayoutForm {
    SCIENTIFIC, DECIMAL_FLOAT
  };

  @Compile
  public static void main(String[] args) {
    Formatter fmt = new Formatter(System.out);

    fmt.format(Civet.CT("%s %5.3f"), new Object[] { "aaaa", 5.6 });
  }

}
