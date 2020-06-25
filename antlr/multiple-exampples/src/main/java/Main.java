import com.mageddo.antlr.json.BasicJsonListener;
import com.mageddo.antlr.json.JsonToLuaTableMain;

import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    System.out.println("> basic json listener");
    BasicJsonListener.main();
    System.out.println();
    System.out.println("> json to lua listener converter");
    JsonToLuaTableMain.main();
  }
}
