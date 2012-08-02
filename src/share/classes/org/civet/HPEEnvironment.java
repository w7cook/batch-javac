package org.civet;

import java.util.Iterator;
import java.util.Stack;

import com.sun.tools.javac.code.Symbol;


public class HPEEnvironment {
  /**
   * This is a stack of method calls.
   */
  private Stack<HPEStack> stack = new Stack<HPEStack>();

  public HPEObject getCurrentThis() {
    return getCurrentStack().thisObject;
  }

  public HPEEnvironment() {

  }

  /**
   * create a new environment with the heap and the given PE stack.
   *
   * @param heap
   * @param peStack
   */
  public HPEEnvironment(HPEStack peStack) {
    super();
    this.stack.push(peStack);
  }

  /**
   * this method should be called upon calling a method. it creates a new stack
   * and push it into environment.
   */
  public void inStack(HPEObject thiz) {
    stack.push(new HPEStack(thiz));
  }

  /**
   * this method clone the last method call stack and return a new environment
   * with the current heap. This is useful when dealing with branches.
   */
  public HPEEnvironment cloneCurrentStackEnv() {
    return new HPEEnvironment(stack.peek().clone());
  }

  /**
   * This method should be called upon returning from a method call.
   */
  public void outStack() {
    stack.pop();
  }

  /**
   * This method should be called upon going into a new block. It calls the
   * inFrame method from the current stack.
   */
  public void inFrame() {
    getCurrentStack().inFrame();
  }

  /**
   * This method should be called upon exiting a block.
   */
  public void outFrame() {
    getCurrentStack().outFrame();
  }

  /**
   *
   * @return Returns the current stack
   */
  public HPEStack getCurrentStack() {
    return stack.peek();
  }

  public HPEObject lookup(HPESymbol n) {
    HPEObject e = stack.peek().lookup(n);
    return e;
  }

  public boolean contains(HPESymbol n) {
    return stack.peek().contains(n);
  }

  public void add(HPESymbol n, HPEObject v) {
    if (n.isField) {
      n.owner.setFieldValue(n.name, v);
    }
    else stack.peek().add(n, v);
    
  }

  public void remove(Symbol n) {
    throw new Error("should not be in remove");
  }

  public String toString() {
    Iterator<HPEStack> i = stack.iterator();
    StringBuffer s = new StringBuffer("(\n");
    while (i.hasNext()) {
      s.append(i.next() + ",\n");
    }
    s.append(")");
    return s.toString();
  }

  public void removeAltered(HPEEnvironment env) {
    // this.getCurrentStack().removeAltered(env.getCurrentStack());
    throw new Error("should not be in removeAltered");
  }
}
