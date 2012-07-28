package org.civet;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class HPEStack {

  /**
   * A pointer to the 'this' object. It is null by default. If non-null, it can
   * be an ObjectLiteral or a PartialObject.
   */
  public HPEObject thisObject;

  private HPEObject returnValue = new HPEObject.Bottom();

  public void setReturnValue(HPEObject v) {
    if (returnValue.isBottom()) {
      returnValue = v;
    } else
      returnValue = HPEObject.Top;
  }

  public HPEStack(HPEObject thiz) {
    this(new HPEFrame(), thiz);
  }

  public HPEStack(HPEFrame currentFrame, HPEObject thiz) {
    super();
    this.currentFrame = currentFrame;
    thisObject = thiz;
  }

  private HPEFrame currentFrame;

  public HPEObject lookup(HPESymbol n) {
    HPEObject e = currentFrame.lookup(n);
    if (e == null && thisObject != null) {
      e = thisObject.getFieldValue(n.name);
    }
    return e;
  }

  public boolean contains(HPESymbol n) {
    boolean c = currentFrame.contains(n);
    if (!c && thisObject != null) {
      c = thisObject.hasField(n.name);
    }
    return c;
  }

  public void add(HPESymbol n, HPEObject v) {
    currentFrame.add(n, v);
  }

  public void remove(HPESymbol n) {
    throw new Error("should not be in remove");
  }

  public HPEFrame findOwnerFrame(HPESymbol n) {
    return currentFrame.findOwnerFrame(n);
  }

  public void inFrame() {
    HPEFrame newFrame = new HPEFrame(currentFrame);
    currentFrame = newFrame;
  }

  public void outFrame() {
    currentFrame = currentFrame.getParentFrame();
  }

  @Override
  public String toString() {
    return "[" + currentFrame.toString() + "]";
  }

  public HPEStack clone() {
    throw new NotImplementedException();
  }

}
