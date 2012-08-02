package org.civet;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HPEFrame {
  /**
   * A null parentFrame means that this frame is the most outer one.
   */
  private HPEFrame parentFrame = null;
  private Map<HPESymbol, HPEObject> map = new HashMap<>();

  public HPEFrame getParentFrame() {
    return parentFrame;
  }

  public HPEFrame() {
    super();
  }

  public HPEFrame(HPEFrame parentFrame, Map<HPESymbol, HPEObject> map) {
    super();
    this.parentFrame = parentFrame;
    this.map = map;
  }

  public HPEFrame(HPEFrame parentFrame) {
    super();
    this.parentFrame = parentFrame;
  }

  public HPEObject lookup(HPESymbol n) {
    HPEObject e = map.get(n);
    if (e == null && parentFrame != null)
      return parentFrame.lookup(n);
    else
      return e;
  }

  public boolean contains(HPESymbol n) {
    boolean b = map.containsKey(n);
    if (!b && parentFrame != null)
      return parentFrame.contains(n);
    return b;
  }

  public HPEFrame findOwnerFrame(HPESymbol n) {
    if (map.containsKey(n)) {
      return this;
    }
    if (parentFrame != null) {
      return parentFrame.findOwnerFrame(n);
    }
    return null;
  }

  public void add(HPESymbol n, HPEObject v) {
    HPEFrame f = findOwnerFrame(n);
    if (f == null)
      this.addToMap(n, v);
    else
      f.addToMap(n, v);
  }

  private void addToMap(HPESymbol n, HPEObject v) {
    map.put(n, v);
    n.set(v);
  }

  /*
   * Removing a variable-literal mapping from the environment makes that
   * variable dynamic. So before doing this, we make sure that the variable was
   * not annotated with
   * 
   * @CompileTime.
   * 
   * An object literal that is removed is marked as 'dynamic'.
   */
  public void remove(HPESymbol n) {
    throw new Error("should not be in remove");
  }

  @Override
  public String toString() {
    StringBuffer strBuffer = new StringBuffer("{");
    Iterator<Map.Entry<HPESymbol, HPEObject>> i = map.entrySet().iterator();
    while (i.hasNext()) {
      Map.Entry<HPESymbol, HPEObject> entry = i.next();
      HPESymbol v = entry.getKey();
      strBuffer.append(v.name.toString() + "=" + entry.getValue() + ",");
    }
    if (parentFrame != null) {
      strBuffer.append(parentFrame.toString());
    }
    strBuffer.append("}");
    return strBuffer.toString();
  }
}
