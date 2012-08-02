package com.sun.tools.javac.batch;

import java.util.List;

import batch.partition.DynamicCallInfo;
import batch.partition.Place;

import com.sun.tools.javac.code.Symbol.MethodSymbol;

public class JCDynamicCallInfo extends DynamicCallInfo {

  MethodSymbol meth;
  
  public JCDynamicCallInfo(Place returns, List<Place> arguments, MethodSymbol meth) {
    super(returns, arguments);
    this.meth = meth;
  }

}
