package format;

public interface Formattable {
  void formatTo(Formatter formatter, int flags, int width, int precision);

}
