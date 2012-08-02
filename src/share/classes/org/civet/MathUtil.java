package org.civet;

import java.math.BigDecimal;

import org.civet.HPEObject.Kind;
import org.civet.Primitive.NUMBER;
import org.civet.Primitive.NUMBER.NumType;
import org.civet.Primitive.*;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class MathUtil {

  static String objectString(Object o) {
    if (o == null)
      return "null";
    return o.toString();
  }

  static NUMBER convertNumberType(BigDecimal r, NUMBER p1, NUMBER p2) {
    NUMBER.NumType resNumType = null;

    if (p1.numType == NumType.DOUBLE || p2.numType == NumType.DOUBLE) {
      resNumType = NumType.DOUBLE;
    } else if (p1.numType == NumType.FLOAT || p2.numType == NumType.FLOAT) {
      resNumType = NumType.FLOAT;
    } else if (p1.numType == NumType.LONG || p2.numType == NumType.LONG) {
      resNumType = NumType.LONG;
    } else {
      resNumType = NumType.INT;
    }
    switch (resNumType) {
      case INT:
        return NUMBER.INT(r.intValue());
      case LONG:
        return NUMBER.LONG(r.longValue());
      case FLOAT:
        return NUMBER.FLOAT(r.floatValue());
      case DOUBLE:
        return NUMBER.DOUBLE(r.doubleValue());
      default:
        return null; // FIXME
    }
  }

  static NUMBER convertNumberType(BigDecimal r, NUMBER p1) {
    switch (p1.numType) {
      case INT:
        return NUMBER.INT(r.intValue());
      case LONG:
        return NUMBER.LONG(r.longValue());
      case FLOAT:
        return NUMBER.FLOAT(r.floatValue());
      case DOUBLE:
        return NUMBER.DOUBLE(r.doubleValue());
      default:
        return null; // FIXME
    }
  }

  public static HPEObject plus(HPEObject n1, HPEObject n2) {
    if (n1.kind == HPEObject.Kind.STRING || n2.kind == HPEObject.Kind.STRING) {
      return new STRING(objectString(n1.object) + objectString(n2.object));
    }
    return convertNumberType(new BigDecimal(Double.valueOf(n1.toString())
        + Double.valueOf(n2.toString())), (NUMBER) n1, (NUMBER) n2);
  }

  public static NUMBER inc(HPEObject n1) {
    return convertNumberType(new BigDecimal(Double.valueOf(n1.toString()) + 1),
        (NUMBER) n1);
  }

  public static NUMBER dec(HPEObject n1) {
    return convertNumberType(new BigDecimal(Double.valueOf(n1.toString()) - 1),
        (NUMBER) n1);
  }

  public static NUMBER minus(HPEObject n1, HPEObject n2) {
    return convertNumberType(new BigDecimal(Double.valueOf(n1.toString())
        - Double.valueOf(n2.toString())), (NUMBER) n1, (NUMBER) n2);
  }

  public static NUMBER mult(HPEObject n1, HPEObject n2) {
    return convertNumberType(new BigDecimal(Double.valueOf(n1.toString())
        * Double.valueOf(n2.toString())), (NUMBER) n1, (NUMBER) n2);
  }

  public static NUMBER div(HPEObject n1, HPEObject n2) {
    return convertNumberType(new BigDecimal(Double.valueOf(n1.toString())
        / Double.valueOf(n2.toString())), (NUMBER) n1, (NUMBER) n2);
  }

  public static NUMBER neg(HPEObject n1) {
    return convertNumberType(new BigDecimal(-Double.valueOf(n1.toString())),
        (NUMBER) n1);
  }

  public static BOOLEAN lt(HPEObject n1, HPEObject n2) {
    return new BOOLEAN(Double.valueOf(n1.toString()) < Double.valueOf(n2
        .toString()));
  }

  public static BOOLEAN le(HPEObject n1, HPEObject n2) {
    return new BOOLEAN(Double.valueOf(n1.toString()) <= Double.valueOf(n2
        .toString()));
  }

  public static BOOLEAN gt(HPEObject n1, HPEObject n2) {
    return new BOOLEAN(Double.valueOf(n1.toString()) > Double.valueOf(n2
        .toString()));
  }

  public static BOOLEAN ge(HPEObject n1, HPEObject n2) {
    return new BOOLEAN(Double.valueOf(n1.toString()) >= Double.valueOf(n2
        .toString()));
  }

  public static BOOLEAN eq(HPEObject n1, HPEObject n2) throws HPEException {
    if ((n1.kind == Kind.STRING && n2.kind == Kind.STRING)
        || (n1.kind == Kind.CHARACTER && n2.kind == Kind.CHARACTER)) {
      return new BOOLEAN(n1.object.equals(n2.object));
    }
    if (n1.kind == Kind.BOOLEAN && n2.kind == Kind.BOOLEAN) {
      return new BOOLEAN(n1.object.equals(n2.object));
    } else if (n1.kind == Kind.NUMBER && n2.kind == Kind.NUMBER) {
      return new BOOLEAN(Double.valueOf(n1.toString()).equals(
          Double.valueOf(n2.toString())));
    } else if (n1.kind == Kind.NULL && n2.kind == Kind.NULL) {
      return new BOOLEAN(true);
    } else if (n1.kind == Kind.NULL || n2.kind == Kind.NULL) {
      return new BOOLEAN(false);
    } else if (n1.kind == Kind.NULL && n2.kind == Kind.PARTIAL) {
      return new BOOLEAN(false);
    } else if (n1.kind == Kind.PARTIAL && n2.kind == Kind.NULL) {
      return new BOOLEAN(false);
    } else if (n1.kind == Kind.OBJECT && n2.kind == Kind.OBJECT) {
      return new BOOLEAN(n1.object.equals(n2.object));
    } else if (n1.kind == Kind.PARTIAL && n2.kind == Kind.PARTIAL) {
      throw new NotImplementedException();
    } else {
      return new BOOLEAN(n1.value() == n2.value());
    }
    // throw new HPEException("Invalid evaluation state.");
  }

  public static BOOLEAN ne(HPEObject n1, HPEObject n2) throws HPEException {
    return not(eq(n1, n2));
  }

  public static BOOLEAN not(HPEObject n1) throws HPEException {
    return new BOOLEAN(!Boolean.valueOf(n1.object.toString()));
  }

  public static NUMBER mod(HPEObject n1, HPEObject n2) {
    return convertNumberType(new BigDecimal(Double.valueOf(n1.toString())
        % Double.valueOf(n2.toString())), (NUMBER) n1, (NUMBER) n2);
  }

  public static NUMBER sl(HPEObject lhs, HPEObject rhs) {
    return convertNumberType(new BigDecimal(
        Long.valueOf(lhs.toString()) << Long.valueOf(rhs.toString())),
        (NUMBER) rhs, (NUMBER) lhs);
  }

  public static NUMBER sr(HPEObject lhs, HPEObject rhs) {
    return convertNumberType(new BigDecimal(
        Long.valueOf(lhs.toString()) >> Long.valueOf(rhs.toString())),
        (NUMBER) rhs, (NUMBER) lhs);
  }

  public static HPEObject bitand(HPEObject lhs, HPEObject rhs) {
    return convertNumberType(
        new BigDecimal(Long.valueOf(lhs.toString())
            & Long.valueOf(rhs.toString())), (NUMBER) rhs, (NUMBER) lhs);
  }

  public static HPEObject bitor(HPEObject lhs, HPEObject rhs) {
    return convertNumberType(
        new BigDecimal(Long.valueOf(lhs.toString())
            | Long.valueOf(rhs.toString())), (NUMBER) rhs, (NUMBER) lhs);
  }

  public static HPEObject bitxor(HPEObject lhs, HPEObject rhs) {
    return convertNumberType(
        new BigDecimal(Long.valueOf(lhs.toString())
            ^ Long.valueOf(rhs.toString())), (NUMBER) rhs, (NUMBER) lhs);
  }

  public static HPEObject or(HPEObject lhs, HPEObject rhs) {
    return new BOOLEAN(Boolean.valueOf(lhs.object.toString())
        || Boolean.valueOf(rhs.object.toString()));
  }

  public static HPEObject and(HPEObject lhs, HPEObject rhs) {
    return new BOOLEAN(Boolean.valueOf(lhs.object.toString())
        && Boolean.valueOf(rhs.object.toString()));
  }

  public static HPEObject bitnot(HPEObject lhs) {
    return convertNumberType(new BigDecimal(~Long.valueOf(lhs.toString())),
        (NUMBER) lhs);
  }

  public static HPEObject usr(HPEObject lhs, HPEObject rhs) {
    return convertNumberType(new BigDecimal(
        Long.valueOf(lhs.toString()) >>> Long.valueOf(rhs.toString())),
        (NUMBER) rhs, (NUMBER) lhs);
  }
}
