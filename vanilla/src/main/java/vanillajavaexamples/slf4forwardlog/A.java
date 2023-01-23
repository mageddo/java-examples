package vanillajavaexamples.slf4forwardlog;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class A {

  public static void main(String[] args) {
    log.info("oi");
    B.doLogOnB(log);
  }

}
