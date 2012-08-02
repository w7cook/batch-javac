package org.civet;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Civet {

  public static <T> T CT(T t) {
    return t;
  }

  public static int CT(int t) {
    return t;
  }

  public static short CT(short t) {
    return t;
  }

  public static double CT(double t) {
    return t;
  }

  public static long CT(long t) {
    return t;
  }

  public static byte CT(byte t) {
    return t;
  }

  public static char CT(char t) {
    return t;
  }

  public static boolean CT(boolean t) {
    return t;
  }

  public static float CT(float t) {
    return t;
  }

  /////////////////
  public static <T> T CT(T t, Boolean b) {
    return t;
  }

  public static int CT(int t, Boolean b) {
    return t;
  }

  public static short CT(short t, Boolean b) {
    return t;
  }

  public static double CT(double t, Boolean b) {
    return t;
  }

  public static long CT(long t, Boolean b) {
    return t;
  }

  public static byte CT(byte t, Boolean b) {
    return t;
  }

  public static char CT(char t, Boolean b) {
    return t;
  }

  public static boolean CT(boolean t, Boolean b) {
    return t;
  }

  public static float CT(float t, Boolean b) {
    return t;
  }
  
  /////////////////
  
  public static <T> T RT(T t) {
    return t;
  }

  public static int RT(int t) {
    return t;
  }

  public static short RT(short t) {
    return t;
  }

  public static double RT(double t) {
    return t;
  }

  public static long RT(long t) {
    return t;
  }

  public static byte RT(byte t) {
    return t;
  }

  public static char RT(char t) {
    return t;
  }

  public static boolean RT(boolean t) {
    return t;
  }

  public static float RT(float t) {
    return t;
  }

  public static <T> Boolean IsCT(T o) {
    return Boolean.TRUE;
  }

  public static Boolean IsCT(byte o) {
    return Boolean.TRUE;
  }

  public static Boolean IsCT(char o) {
    return Boolean.TRUE;
  }

  public static Boolean IsCT(short o) {
    return Boolean.TRUE;
  }

  public static Boolean IsCT(int o) {
    return Boolean.TRUE;
  }

  public static Boolean IsCT(long o) {
    return Boolean.TRUE;
  }

  public static Boolean IsCT(float o) {
    return Boolean.TRUE;
  }

  public static Boolean IsCT(double o) {
    return Boolean.TRUE;
  }

  public static Boolean IsCT(boolean o) {
    return Boolean.TRUE;
  }

  @Retention(RetentionPolicy.CLASS)
  @Target(ElementType.METHOD)
  public static @interface Compile {
    
  }
}
