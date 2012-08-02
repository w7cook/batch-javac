package benchmark.arith_int;

import java.util.*;

public class Timer {
  protected long t_start, t_end = -1;
  protected String message;

  public Timer(String message) {
    this.message = message;
    start();
  }

  public Timer() {
    this("Time taken: ");
  }

  public void start() {
    t_start = (new Date()).getTime();
    t_end = -1;
  }

  public void stop() {
    t_end = (new Date()).getTime();
  }

  public long diff() {
    long end = t_end;
    if (t_end == -1)
      end = (new Date()).getTime();
    return end - t_start;
  }

  public void report() {
    System.out.println(message + diff() + "ms");
  }
}
