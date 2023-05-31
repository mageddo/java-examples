package vanillajavaexamples.resources;

import java.util.Set;

public class Threads {
  public static Set<Thread> findAllThreads(){
    return Thread.getAllStackTraces().keySet();
  }

  public static void printAllThreads(){
    System.out.println("> All threads list");
    findAllThreads().forEach(it -> {
      System.out.printf("name=%s, state=%s%n", it.getName(), it.getState().name());
    });
  }
}
