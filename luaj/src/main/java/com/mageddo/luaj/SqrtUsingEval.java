package com.mageddo.luaj;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class SqrtUsingEval {

  private ScriptEngine luaj = new ScriptEngineManager()
      .getEngineByName("luaj");

  public int sqrt(int n) throws ScriptException {
    luaj.put("n", n);
    luaj.eval("r = math.sqrt(n)");
    return (int) luaj.get("r");
  }
}
