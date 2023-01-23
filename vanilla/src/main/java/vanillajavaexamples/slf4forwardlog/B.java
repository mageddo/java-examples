package vanillajavaexamples.slf4forwardlog;

import org.slf4j.Logger;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class B {

  public static void doLogOnB(Logger log) {

    log.info("status=msg");
  }


}
