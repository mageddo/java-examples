package com.mageddo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class ProducerRecordDelegate {

  public void sendSync(RecordVO record) {
   log.debug("status=fallbackWorked, record={}", record);
  }
}
