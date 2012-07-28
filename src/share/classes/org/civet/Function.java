package org.civet;

import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

public abstract class Function<R, T> {
  public abstract R apply(T t);

  public static <S, P> List<S> map(Function<S, P> f, List<? extends P> l) {
    if (l == null)
      return null;
    ListBuffer<S> r = new ListBuffer<>();
    for (P t : l) {
      r.append(f.apply(t));
    }
    return r.toList();
  }

  public static <S, P> List<S> map(Function<S, P> f,
      Function<Boolean, P> filter, List<? extends P> l) {
    if (l == null)
      return null;
    ListBuffer<S> r = new ListBuffer<>();
    for (P t : l) {
      if (!filter.apply(t))
        r.append(f.apply(t));
    }
    return r.toList();
  }

  public static <S> List<S> filter(Function<Boolean, S> f, List<? extends S> l) {
    if (l == null)
      return null;
    ListBuffer<S> r = new ListBuffer<>();
    for (S t : l) {
      if (!f.apply(t))
        r.append(t);
    }
    return r.toList();
  }
}
