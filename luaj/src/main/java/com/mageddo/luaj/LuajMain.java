package com.mageddo.luaj;

import java.io.InputStreamReader;

import javax.script.ScriptException;

import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

public class LuajMain {

  public static void main(String[] args) throws ScriptException {
    executeHelloWorld();
    createASumFunctionAndCallIt();
    calcSqrtUsingEval();
    loadLuaFileAndCallFunctionProcessingDataReturn();
  }

  static void loadLuaFileAndCallFunctionProcessingDataReturn() {
    System.out.println("> loadLuaFileAndCallFunctionProcessingDataReturn");
    final var globals = JsePlatform.standardGlobals();

    final var scriptName = "/colour-palette.lua";
    final var scriptReader = new InputStreamReader(LuajMain.class.getResourceAsStream(scriptName));
    globals.load(scriptReader, scriptName).call();

    final var colors = globals
        .invokemethod("getColourPalette")
        .arg1()
        .checktable()
    ;
    final var keys = colors.keys();
    for (int i = 0; i < colors.keyCount(); i++) {
      final var k = keys[i];
      final var color = colors.get(k).tostring();
      System.out.printf("k=%s, v=%s%n", k.tostring(), color);
    }

    System.out.println();
  }

  static void createASumFunctionAndCallIt() {
    System.out.println("> createASumFunctionAndCallIt");
    final var globals = JsePlatform.standardGlobals();
    globals.load("print 'hello, world'; function sum(a, b) return a + b; end").call();
    final var r = globals
        .get("sum")
        .invoke(varArgsToArray(
            LuaValue.valueOf(3),
            LuaValue.valueOf(5)
        ));
    System.out.printf("sum=%d%n", r.arg1().toint());
    System.out.println();
  }

  static void calcSqrtUsingEval() throws ScriptException {
    System.out.println("> calcSqrtUsingEval");
    System.out.printf("sqrt using eval=%d%n", new SqrtUsingEval().sqrt(9));
    System.out.println();
  }

  static void executeHelloWorld() {
    System.out.println("> executeHelloWorld");
    final var globals = JsePlatform.standardGlobals();
    LuaValue chunk = globals.load("print 'hello, world'");
    chunk.call();
    System.out.println();
  }

  static LuaValue[] varArgsToArray(LuaValue... args) {
    return args;
  }
}
